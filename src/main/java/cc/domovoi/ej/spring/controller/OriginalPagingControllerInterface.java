package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.entity.BaseEntityPagingInterface;
import cc.domovoi.ej.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface OriginalPagingControllerInterface<E extends BaseEntityPagingInterface> extends OriginalRetrieveControllerInterface<E> {

    Function<E, Integer> findCountFunction();

    @ApiOperation(value="Paging query", notes="")
    @RequestMapping(
            value = "paging",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findPagingEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("findPagingEntity: %s", entity));
            List<E> entityList = findEntityFunction().apply(entity);
            Integer total = findCountFunction().apply(entity);
            jsonMap.put("total", total);
            jsonMap.put("rows", entityList);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
