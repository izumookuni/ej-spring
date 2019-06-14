package cc.domovoi.spring.controller;

import cc.domovoi.spring.format.json.DateTimeObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import cc.domovoi.spring.entity.BaseExportEntityInterface;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public interface GeneralExportControllerInterface<C, E extends BaseExportEntityInterface<C>> extends OriginalRetrieveControllerInterface<E> {

    @ApiOperation(value = "Export content to Excel file", notes = "")
    @RequestMapping(
            value = "export/excel",
            method = {RequestMethod.POST})
    default void exportExcel(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) {
        logger().info(String.format("exportExcel: %s", entity));
        try(Workbook wb = new XSSFWorkbook(); ServletOutputStream outputStream = response.getOutputStream()) {
            if (entity == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is null");
                return;
            }
            List<E> eList = findEntityFunction(entity);
            if (eList == null || eList.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "There is nothing to export");
                return;
            }
            ObjectMapper objectMapper = DateTimeObjectMapper.dateTimeObjectMapper;
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            IntStream.range(0, eList.size()).forEach(sheetIndex -> {
                // Each Sheet
                E sheetEntity = eList.get(sheetIndex);
                List<C> contentEntityList = sheetEntity.exportContent().get();
                if (contentEntityList == null) {
                    return;
                }
                String sheetName = sheetEntity.exportSheetName().get();
                Sheet sheet = wb.createSheet(sheetName != null ? sheetName : "Sheet" + (sheetIndex + 1));
                Row titleRow = sheet.createRow(0);
                List<Tuple2<String, Function<? super C, ? extends Object>>> columnMeta = sheetEntity.exportColumnMeta();
                IntStream.range(0, columnMeta.size()).forEach(cellIndex -> {
                    Cell titleCell = titleRow.createCell(cellIndex);
                    titleCell.setCellValue(columnMeta.get(cellIndex).v1());
                });
                IntStream.range(0, contentEntityList.size()).forEach(rowIndex -> {
                    // Each Row
                    // Start from row 1
                    Row contentRow = sheet.createRow(rowIndex + 1);
                    C contentEntity = contentEntityList.get(rowIndex);
                    IntStream.range(0, columnMeta.size()).forEach(cellIndex -> {
                        // Each Cell
                        Object content = columnMeta.get(cellIndex).v2().apply(contentEntity);
                        Cell contentCell = contentRow.createCell(cellIndex);
                        try {
                            String contentValue = objectMapper.writeValueAsString(content);
                            if (contentValue.startsWith("\"") && contentValue.endsWith("\"")) {
                                contentValue = contentValue.substring(1, contentValue.length() - 1);
                            }
                            contentCell.setCellValue(contentValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger().error(String.format("error in setCellValue(%d, %d), message: %s", rowIndex + 1, cellIndex, e.getMessage()));
                        }
                    });
                });
            });
            String fileName = (eList.size() == 1 ?
                    eList.get(0).exportSheetName().get() :
                    (entity.exportTitle().get() != null ?
                            entity.exportTitle().get() :
                            "Untitled-" + LocalDateTime.now().format(DateTimeObjectMapper.dateTimeFormatter)
                    )) + ".xlsx";
            response.setHeader("Content-Disposition", String.format("attachment;filename*=utf-8''%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())));
            response.setHeader("Cache-Control", "max-age=86400");
            response.setHeader("File-Name-Base64", Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8)));
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // xlsx
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in exportExcel, message: %s", e.getMessage()));
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
                logger().error(String.format("error in exportExcel, message: %s", e1.getMessage()));
            }
        }
    }

//    BeanMap beanMap = BeanMap.create(entity);
//    List<Tuple3<String, String, Function<? super E, ? extends Object>>> columns = Optional.ofNullable(entity.exportColumnMeta()).orElseGet(() -> {
//        List<Tuple3<String, String, Function<? super E, ? extends Object>>> exportColumnMeta = ((Set<String>) beanMap.keySet()).stream().map(key -> new Tuple3(key, key, new Function<E, Object>() {
//            @Override
//            public Object apply(E e) {
//                return new BeanWrapperImpl(e).getPropertyValue(key);
//            }
//        })).collect(Collectors.toList());
//        return new ArrayList<>(exportColumnMeta);
//    });
}
