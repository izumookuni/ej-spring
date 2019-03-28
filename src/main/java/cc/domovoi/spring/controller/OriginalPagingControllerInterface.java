package cc.domovoi.spring.controller;

import cc.domovoi.spring.entity.BasePagingEntityInterface;
import cc.domovoi.spring.utils.RestfulUtils;
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

/**
 * OriginalPagingControllerInterface.
 *
 * @param <E> Entity type.
 */
public interface OriginalPagingControllerInterface<E extends BasePagingEntityInterface> extends OriginalRetrieveControllerInterface<E> {

    /**
     * The function that find the Amount of entities that have certain attributes.
     *
     * @return Amount of entities.
     */
    Function<E, Integer> findCountFunction();

    /**
     * Find entity list, and paging return.
     *
     * @param entity Query conditions.
     * @return Paging entity list.
     */
    @ApiOperation(value = "Paging query", notes = "")
    @RequestMapping(
            value = "paging",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findPagingEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("findPagingEntity: %s", entity));
            List<E> entityList = findEntityFunction(entity);
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
