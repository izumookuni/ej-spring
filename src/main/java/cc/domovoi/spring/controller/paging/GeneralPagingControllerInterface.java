package cc.domovoi.spring.controller.paging;

import cc.domovoi.spring.controller.GeneralRetrieveControllerInterface;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.service.paging.GeneralPagingServiceInterface;
import cc.domovoi.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GeneralPagingControllerInterface<K, E extends GeneralJoiningEntityInterface<K>, S extends GeneralPagingServiceInterface<K, E>> extends GeneralRetrieveControllerInterface<E> {

    Integer findCountFunction(E entity);

    List<E> findPagingListFunction(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder);

    @ApiOperation(value = "Find amount of entityList", notes = "")
    @RequestMapping(
            value = "count",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findCount(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        Integer total = findCountFunction(entity);
        return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, total);
    }

    @ApiOperation(value = "Paging query", notes = "")
    @RequestMapping(
            value = "paging",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findPagingList(@RequestBody E entity, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) List<String> sortBy, @RequestParam(required = false) List<String> sortOrder) {
        Map<String, Object> jsonMap = new HashMap<>();
        Integer total = findCountFunction(entity);
        List<E> entityList = findPagingListFunction(entity, pageNum, pageSize, sortBy, sortOrder);
        jsonMap.put("total", total);
        jsonMap.put("rows", entityList);
        return RestfulUtils.fillOk(jsonMap, HttpStatus.OK);
    }
}
