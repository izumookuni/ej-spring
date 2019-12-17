package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.annotation.before.BeforeAdd;
import cc.domovoi.spring.annotation.before.BeforeDelete;
import cc.domovoi.spring.annotation.before.BeforeUpdate;
import cc.domovoi.spring.utils.GeneralUtils;
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
import java.util.Optional;

/**
 * OriginalCRUDControllerInterface.
 *
 * @param <E> Entity type.
 */
public interface GeneralCRUDControllerInterface<K, E> extends GeneralRetrieveControllerInterface<E> {

    /**
     * The function that add Entity.
     *
     * @param entity entity
     * @param request request
     * @param response request
     * @return The number of successful insert operations.
     */
    Try<Tuple2<Integer, K>> addEntityFunction(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response);

    /**
     * The function that update Entity.
     *
     * @param entity entity
     * @param request request
     * @param response request
     * @return The number of successful update operations.
     */
    Try<Integer> updateEntityFunction(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response);

    /**
     * The function that delete Entity.
     *
     * @param entity entity
     * @param request request
     * @param response request
     * @return The number of successful delete operations.
     */
    Try<Integer> deleteEntityFunction(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response);

    default void beforeAdd(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void beforeUpdate(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void beforeDelete(E entity, HttpServletRequest request, HttpServletResponse response) { }

    default void afterAdd(E entity, HttpServletRequest request, HttpServletResponse response, Try<Tuple2<Integer, K>> result) { }

    default void afterUpdate(E entity, HttpServletRequest request, HttpServletResponse response, Try<Integer> result) { }

    default void afterDelete(E entity, HttpServletRequest request, HttpServletResponse response, Try<Integer> result) { }

    default void doBeforeAdd(Integer scope, E entity, HttpServletRequest request, HttpServletResponse response) {
        if (0 == scope) {
            beforeAdd(entity, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, BeforeAdd.class, scope, entity, request, response);
    }

    default void doBeforeUpdate(Integer scope, E entity, HttpServletRequest request, HttpServletResponse response) {
        if (0 == scope) {
            beforeUpdate(entity, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, BeforeUpdate.class, scope, entity, request, response);
    }

    default void doBeforeDelete(Integer scope, E entity, HttpServletRequest request, HttpServletResponse response) {
        if (0 == scope) {
            beforeDelete(entity, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, BeforeDelete.class, scope, entity, request, response);
    }

    default void doAfterAdd(Integer scope, E entity, HttpServletRequest request, HttpServletResponse response, Try<Tuple2<Integer, K>> result) {
        if (0 == scope) {
            afterAdd(entity, request, response, result);
        }
        GeneralUtils.doFindAnnotationMethod(this, AfterAdd.class, scope, entity, request, response, result);
    }

    default void doAfterUpdate(Integer scope, E entity, HttpServletRequest request, HttpServletResponse response, Try<Integer> result) {
        if (0 == scope) {
            afterUpdate(entity, request, response, result);
        }
        GeneralUtils.doFindAnnotationMethod(this, AfterUpdate.class, scope, entity, request, response, result);
    }

    default void doAfterDelete(Integer scope, E entity, HttpServletRequest request, HttpServletResponse response, Try<Integer> result) {
        if (0 == scope) {
            afterDelete(entity, request, response, result);
        }
        GeneralUtils.doFindAnnotationMethod(this, AfterDelete.class, scope, entity, request, response, result);
    }

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
            doBeforeAdd(0, entity, request, response);
//            beforeAdd(entity, request, response);
            Try<Tuple2<Integer, K>> result = addEntityFunction(entity, Optional.of(request), Optional.of(response));
            doAfterAdd(0, entity, request, response, result);
//            afterAdd(entity, request, response);
            if (result.isSuccess()) {
                Tuple2<Integer, K> data = result.get();
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("result", data.v1());
                dataMap.put("id", data.v2());
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, dataMap);
            }
            else {
                result.failed().get().printStackTrace();
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
            doBeforeUpdate(0, entity, request, response);
//            beforeUpdate(entity, request, response);
            Try<Integer> result = updateEntityFunction(entity, Optional.of(request), Optional.of(response));
            doAfterUpdate(0, entity, request, response, result);
//            afterUpdate(entity, request, response);
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.get());
            }
            else {
                result.failed().get().printStackTrace();
                throw new RuntimeException(result.failed().get().getMessage());
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in updateEntity, message: %s", e.getMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
            doBeforeDelete(0, entity, request, response);
//            beforeDelete(entity, request, response);
            Try<Integer> result = deleteEntityFunction(entity, Optional.of(request), Optional.of(response));
            doAfterDelete(0, entity, request, response, result);
//            afterDelete(entity, request, response);
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.get());
            }
            else {
                result.failed().get().printStackTrace();
                throw new RuntimeException(result.failed().get().getMessage());
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in deleteEntity, message: %s", e.getMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
