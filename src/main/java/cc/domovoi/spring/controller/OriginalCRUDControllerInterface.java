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

    /**
     * Add Entity.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    @ApiOperation(value = "Add entity", notes = "id, creationTime and updateTime will be generated automatically by the system")
    @RequestMapping(
            value = "add",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> addEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("addEntity: %s", entity));
            Try<Tuple2<Integer, String>> result = addEntityFunction(entity);
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
     * @return The number of successful update operations.
     */
    @ApiOperation(value = "Update entity", notes = "id can't be null")
    @RequestMapping(
            value = "update",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> updateEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("jsonMap: %s", entity));
            Try<Integer> result = updateEntityFunction(entity);
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
     * @return The number of successful delete operations.
     */
    @ApiOperation(value = "Delete entity", notes = "none")
    @RequestMapping(
            value = "delete",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> deleteEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("deleteEntity: %s", entity));
            Try<Integer> result = deleteEntityFunction(entity);
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
