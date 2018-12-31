package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
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
     * @return The number of successful insert operations.
     */
    Function<E, Integer> addEntityFunction();

    /**
     * The function that update Entity.
     *
     * @return The number of successful update operations.
     */
    Function<E, Integer> updateEntityFunction();

    /**
     * The function that delete Entity.
     *
     * @return The number of successful delete operations.
     */
    Function<E, Integer> deleteEntityFunction();

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
            Integer result = addEntityFunction().apply(entity);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in addEntity, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
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
            Integer result = updateEntityFunction().apply(entity);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
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
    @ApiOperation(value = "Delete entity", notes = "")
    @RequestMapping(
            value = "delete",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> deleteEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("deleteEntity: %s", entity));
            Integer result = deleteEntityFunction().apply(entity);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in deleteEntity, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
