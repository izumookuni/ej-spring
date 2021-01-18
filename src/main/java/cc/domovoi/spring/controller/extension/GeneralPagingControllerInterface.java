package cc.domovoi.spring.controller.extension;

import cc.domovoi.spring.controller.GeneralRetrieveControllerInterface;
import cc.domovoi.spring.controller.OriginalControllerInterface;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.extension.PagingEntityInterface;
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
 * GeneralPagingControllerInterface.
 *
 * @param <E> Entity type.
 */

public interface GeneralPagingControllerInterface<E> extends OriginalControllerInterface {

    /**
     * The function that find the Amount of entities that have certain attributes.
     *
     * @param entity entity
     * @return Amount of entities.
     */
    Integer findCountFunction(E entity);

    List<E> findPagingListFunction(E entity);

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
            List<E> entityList = findPagingListFunction(entity);
            Integer total = findCountFunction(entity);
            jsonMap.put("total", total);
            jsonMap.put("rows", entityList);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
