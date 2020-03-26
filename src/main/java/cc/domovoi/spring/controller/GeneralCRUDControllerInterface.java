package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.annotation.before.BeforeAdd;
import cc.domovoi.spring.annotation.before.BeforeDelete;
import cc.domovoi.spring.annotation.before.BeforeUpdate;
import cc.domovoi.spring.entity.audit.AuditUtils;
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
     * @param params params
     * @return The number of successful insert operations.
     */
    Try<Tuple2<Integer, K>> addEntityFunction(E entity, Map<String, Object> params);

    /**
     * The function that update Entity.
     *
     * @param entity entity
     * @param params params
     * @return The number of successful update operations.
     */
    Try<Integer> updateEntityFunction(E entity, Map<String, Object> params);

    /**
     * The function that delete Entity.
     *
     * @param entity entity
     * @param params params
     * @return The number of successful delete operations.
     */
    Try<Integer> deleteEntityFunction(E entity, Map<String, Object> params);

    default void beforeAdd(E entity, Map<String, Object> params) { }

    default void beforeUpdate(E entity, Map<String, Object> params) { }

    default void beforeDelete(E entity, Map<String, Object> params) { }

    default void afterAdd(E entity, Try<Tuple2<Integer, K>> result, Map<String, Object> params) { }

    default void afterUpdate(E entity, Try<Integer> result, Map<String, Object> params) { }

    default void afterDelete(E entity, Try<Integer> result, Map<String, Object> params) { }

    default void doBeforeAdd(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeAdd(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeAdd.class, scope, name, entity, params);
    }

    default void doBeforeUpdate(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeUpdate(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeUpdate.class, scope, name, entity, params);
    }

    default void doBeforeDelete(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeDelete(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeDelete.class, scope, name, entity, params);
    }

    default void doAfterAdd(Integer scope, String name, E entity, Try<Tuple2<Integer, K>> result, Map<String, Object> params) {
        if (0 == scope) {
            afterAdd(entity, result, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterAdd.class, scope, name, entity, result, params);
    }

    default void doAfterUpdate(Integer scope, String name, E entity, Try<Integer> result, Map<String, Object> params) {
        if (0 == scope) {
            afterUpdate(entity, result, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterUpdate.class, scope, name, entity, result, params);
    }

    default void doAfterDelete(Integer scope, String name, E entity, Try<Integer> result, Map<String, Object> params) {
        if (0 == scope) {
            afterDelete(entity, result, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterDelete.class, scope, name, entity, result, params);
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
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            doBeforeAdd(0, "addEntity", entity, params);
//            beforeAdd(entity, request, response);
            Try<Tuple2<Integer, K>> result = addEntityFunction(entity, params);
            doAfterAdd(0, "addEntity", entity, result, params);
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
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            doBeforeUpdate(0, "updateEntity", entity, params);
//            beforeUpdate(entity, request, response);
            Try<Integer> result = updateEntityFunction(entity, params);
            doAfterUpdate(0, "updateEntity", entity, result, params);
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
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            doBeforeDelete(0, "deleteEntity", entity, params);
//            beforeDelete(entity, request, response);
            Try<Integer> result = deleteEntityFunction(entity, params);
            doAfterDelete(0, "deleteEntity", entity, result, params);
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
