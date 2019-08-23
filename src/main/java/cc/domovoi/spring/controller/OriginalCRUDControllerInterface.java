package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * OriginalCRUDControllerInterface.
 *
 * @param <E> Entity type.
 */
public interface OriginalCRUDControllerInterface<E> extends OriginalRetrieveControllerInterface<E> {

    /**
     * The function that add Entity.
     *
     * @param entity entity
     * @return The number of successful insert operations.
     */
    Try<Tuple2<Integer, String>> addEntityFunction(E entity);

    /**
     * The function that update Entity.
     *
     * @param entity entity
     * @return The number of successful update operations.
     */
    Try<Integer> updateEntityFunction(E entity);

    /**
     * The function that delete Entity.
     *
     * @param entity entity
     * @return The number of successful delete operations.
     */
    Try<Integer> deleteEntityFunction(E entity);

    default void beforeAdd(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void beforeUpdate(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void beforeDelete(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void afterAdd(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void afterUpdate(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void afterDelete(E entity, HttpServletRequest request, HttpServletResponse response) { }

    /**
     * Add Entity.
     *
     * @param entity The entity need to be added.
     * @param request request
     * @param response response
     * @return The number of successful insert operations.
     */
    @ApiOperation(value = "Add entity", notes = "id, creationTime and updateTime will be generated automatically by the system")
    @RequestMapping(
            value = "add",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> addEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("addEntity: %s", entity));
            beforeAdd(entity, request, response);
            Try<Tuple2<Integer, String>> result = addEntityFunction(entity);
            afterAdd(entity, request, response);
            if (result.isSuccess()) {
                Tuple2<Integer, String> data = result.get();
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("result", data.v1());
                dataMap.put("id", data.v2());
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, dataMap);
            }
            else {
                throw new RuntimeException(result.failed().get().getMessage());
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in addEntity, message: %s", e.getMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Update entity.
     *
     * @param entity The entity need to be updated.
     * @param request request
     * @param response response
     * @return The number of successful update operations.
     */
    @ApiOperation(value = "Update entity", notes = "id can't be null")
    @RequestMapping(
            value = "update",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> updateEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("jsonMap: %s", entity));
            beforeUpdate(entity, request, response);
            Try<Integer> result = updateEntityFunction(entity);
            afterUpdate(entity, request, response);
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.get());
            }
            else {
                throw new RuntimeException(result.failed().get().getMessage());
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in updateEntity, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * Delete entity.
     *
     * @param entity The entity need to be deleted.
     * @param request request
     * @param response response
     * @return The number of successful delete operations.
     */
    @ApiOperation(value = "Delete entity", notes = "none")
    @RequestMapping(
            value = "delete",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> deleteEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("deleteEntity: %s", entity));
            beforeDelete(entity, request, response);
            Try<Integer> result = deleteEntityFunction(entity);
            afterDelete(entity, request, response);
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.get());
            }
            else {
                throw new RuntimeException(result.failed().get().getMessage());
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in deleteEntity, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
