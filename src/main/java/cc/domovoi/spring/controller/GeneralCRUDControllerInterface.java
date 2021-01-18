package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Either;
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
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * The function that add or update Entity.
     *
     * @param entity entity
     * @param params params
     * @return The number of successful insert or update operations.
     */
    Try<Either<Integer, Tuple2<Integer, K>>> addOrUpdateEntityFunction(E entity, Map<String, Object> params);

    /**
     * The function that delete Entity by id.
     *
     * @param idList id list
     * @param params params
     * @return The number of successful delete operations.
     */
    Try<Integer> deleteEntityByIdFunction(List<K> idList, Map<String, Object> params);

    /**
     * The function that Check entity exists.
     *
     * @param entity Entity
     * @return Whether entity exists.
     */
    Boolean checkEntityExistsFunction(E entity);

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
     * @throws Exception exception
     */
    @ApiOperation(value = "Add entity", notes = "id, creationTime and updateTime will be generated automatically by the system")
    @RequestMapping(
            value = "add",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> addEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
//                result.failed().get().printStackTrace();
                throw result.failed().get();
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
//            e.printStackTrace();
            logger().error(String.format("error in addEntity, message: %s", e.getMessage()));
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw e;
        }
    }

    /**
     * Update entity.
     *
     * @param entity The entity need to be updated.
     * @param request request
     * @param response response
     * @param forced forced
     * @param sn setNull
     * @return The number of successful update operations.
     * @throws Exception exception
     */
    @ApiOperation(value = "Update entity", notes = "id can't be null")
    @RequestMapping(
            value = "update",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> updateEntity(@RequestBody E entity, @RequestParam(required = false) Boolean forced, @RequestParam(required = false) List<String> sn, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("updateEntity: %s", entity));
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            if (Objects.nonNull(forced)) {
                params.put("forced", forced);
            }
            if (Objects.nonNull(sn)) {
                params.put("setNull", sn);
            }
            doBeforeUpdate(0, "updateEntity", entity, params);
//            beforeUpdate(entity, request, response);
            Try<Integer> result = updateEntityFunction(entity, params);
            doAfterUpdate(0, "updateEntity", entity, result, params);
//            afterUpdate(entity, request, response);
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.get());
            }
            else {
//                result.failed().get().printStackTrace();
                throw result.failed().get();
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
//            e.printStackTrace();
            logger().error(String.format("error in updateEntity, message: %s", e.getMessage()));
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw e;
        }
    }

    /**
     * Delete entity.
     *
     * @param entity The entity need to be deleted.
     * @param request request
     * @param response response
     * @return The number of successful delete operations.
     * @throws Exception exception
     */
    @ApiOperation(value = "Delete entity", notes = "none")
    @RequestMapping(
            value = "delete",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> deleteEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
//                result.failed().get().printStackTrace();
                throw result.failed().get();
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
//            e.printStackTrace();
            logger().error(String.format("error in deleteEntity, message: %s", e.getMessage()));
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw e;
        }
    }

    default Try<Either<Integer, Tuple2<Integer, K>>> addOrUpdateEntity(@RequestBody E entity, Map<String, Object> params) {
        Boolean existsFlag = checkEntityExistsFunction(entity);
        if (!existsFlag) {
            // add
            doBeforeAdd(0, "addEntity", entity, params);
        }
        else {
            // update
            doBeforeUpdate(0, "updateEntity", entity, params);
        }
        Try<Either<Integer, Tuple2<Integer, K>>> result = addOrUpdateEntityFunction(entity, params);
        if (!existsFlag) {
            // add
            doAfterAdd(0, "addEntity", entity, result.map(either -> either.right().get()), params);
        }
        else {
            // update
            doAfterUpdate(0, "updateEntity", entity, result.map(either -> either.left().get()), params);
        }
        return result;
    }

    /**
     * And or Update entity.
     *
     * @param entity The entity need to be add or updated.
     * @param request request
     * @param response response
     * @return The number of successful add or update operations.
     * @throws Exception exception
     */
    @ApiOperation(value = "Add or Update entity", notes = "")
    @RequestMapping(
            value = "add-or-update",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> addOrUpdateEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("addOrUpdateEntity: %s", entity));
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            Try<Either<Integer, Tuple2<Integer, K>>> result = addOrUpdateEntity(entity, params);
            if (result.isSuccess()) {
                return result.get().fold(l -> RestfulUtils.fillOk(jsonMap, HttpStatus.OK, l), r -> {
                    Tuple2<Integer, K> data = r;
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("result", data.v1());
                    dataMap.put("id", data.v2());
                    return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, dataMap);
                });
            }
            else {
                throw result.failed().get();
            }

        } catch (Exception e) {
            logger().error(String.format("error in addOrUpdateEntity, message: %s", e.getMessage()));
            throw e;
        }
    }

    /**
     * And or Update entity.
     *
     * @param entityList The entityList need to be add or updated.
     * @param request request
     * @param response response
     * @return The number of successful add or update operations.
     * @throws Exception exception
     */
    @ApiOperation(value = "Add or Update entity", notes = "")
    @RequestMapping(
            value = "add-or-update/batch",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> addOrUpdateEntityBatch(@RequestBody List<E> entityList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            List<Try<Either<Integer, Tuple2<Integer, K>>>> result0 = entityList.stream().map(entity -> addOrUpdateEntity(entity, params)).collect(Collectors.toList());
            Try<List<Either<Integer, Tuple2<Integer, K>>>> result = result0.stream().reduce(Try.apply(() -> new ArrayList<>()), (z, e) -> z.flatMap(l -> e.map(ee -> {
                l.add(ee);
                return l;
            })), (l, r) -> l.flatMap(ll -> r.map(rr -> {
                ll.addAll(rr);
                return ll;
            })));
            if (result.isSuccess()) {
                List<Either<Integer, Tuple2<Integer, K>>> data = result.get();
                Tuple2<List<Integer>, List<Tuple2<Integer, K>>> result2 = Seq.partition(data.stream(), Either::isLeft)
                        .map1(eithers -> eithers.map(either -> either.left().get()).collect(Collectors.toList()))
                        .map2(eithers -> eithers.map(either -> either.right().get()).collect(Collectors.toList()));
                Map<String, Object> updateDataMap = new HashMap<>();
                updateDataMap.put("result", result2.v1().stream().mapToInt(e -> e).sum());
                Map<String, Object> addDataMap = new HashMap<>();
                addDataMap.put("result", result2.v2().stream().mapToInt(Tuple2::v1).sum());
                addDataMap.put("id", result2.v2().stream().map(Tuple2::v2).collect(Collectors.toList()));
                jsonMap.put("addResult", addDataMap);
                jsonMap.put("updateResult", updateDataMap);
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK);
            }
            else {
                throw result.failed().get();
            }
        } catch (Exception e) {
            logger().error(String.format("error in addOrUpdateEntityBatch, message: %s", e.getMessage()));
            throw e;
        }
    }

    /**
     * Delete entity by id.
     *
     * @param idList The id of entity need to be deleted.
     * @param request request
     * @param response response
     * @return The number of successful delete operations.
     * @throws Exception exception
     */
    @ApiOperation(value = "Delete entity by id", notes = "none")
    @RequestMapping(
            value = "delete-by-id",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> deleteEntityById(@RequestBody List<K> idList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("deleteEntityById: %s", idList));
            Map<String, Object> params = new HashMap<>();
            params.put("_request", request);
            params.put("_response", response);
            params.put("_auditIp", AuditUtils.getIpAddr(request));
            params.put("_idList", idList);
            E entity = Reflect.onClass(entityClass()).create().get();
            doBeforeDelete(0, "deleteEntity", entity, params);
//            beforeDelete(entity, request, response);
            Try<Integer> result = deleteEntityByIdFunction(idList, params);
            doAfterDelete(0, "deleteEntity", entity, result, params);
//            afterDelete(entity, request, response);
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.get());
            }
            else {
//                result.failed().get().printStackTrace();
                throw result.failed().get();
            }
//            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
//            e.printStackTrace();
            logger().error(String.format("error in deleteEntity, message: %s", e.getMessage()));
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw e;
        }
    }
}
