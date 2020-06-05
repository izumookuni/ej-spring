package cc.domovoi.spring.controller.innerlayer;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.controller.StandardCRUDControllerInterface;
import cc.domovoi.spring.controller.innerlayer.response.InnerLayerAddResultResponse;
import cc.domovoi.spring.controller.innerlayer.response.InnerLayerUpdateDeleteResultResponse;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.entity.audit.AuditUtils;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;
import io.swagger.annotations.ApiOperation;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public interface StandardCRUDInnerLayerControllerInterface<E extends StandardJoiningEntityInterface, S extends StandardJoiningServiceInterface<E>> extends StandardCRUDControllerInterface<E, S>, StandardRetrieveInnerLayerControllerInterface<E, S> {

    @ApiOperation(value = "Add raw entity", notes = "id, creationTime and updateTime will be generated automatically by the system")
    @RequestMapping(
            value = "inner-layer/add",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default InnerLayerAddResultResponse<String, E> addInnerLayerEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger().debug(String.format("addInnerLayerEntity: %s", entity));
        Map<String, Object> params = new HashMap<>();
        params.put("_request", request);
        params.put("_response", response);
        params.put("_auditIp", AuditUtils.getIpAddr(request));
        doBeforeAdd(0, "addInnerLayerEntity", entity, params);
        Try<Tuple2<Integer, String>> result = addEntityFunction(entity, params);
        doAfterAdd(0, "addInnerLayerEntity", entity, result, params);
        if (result.isSuccess()) {
            Tuple2<Integer, String> data = result.get();
            return new InnerLayerAddResultResponse<>(data.v1, data.v2, entity);
        }
        else {
            throw result.failed().get();
        }
    }

    @ApiOperation(value = "Update raw entity", notes = "id can't be null")
    @RequestMapping(
            value = "inner-layer/update",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default InnerLayerUpdateDeleteResultResponse<E> updateInnerLayerEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger().debug(String.format("updateInnerLayerEntity: %s", entity));
        Map<String, Object> params = new HashMap<>();
        params.put("_request", request);
        params.put("_response", response);
        params.put("_auditIp", AuditUtils.getIpAddr(request));
        doBeforeUpdate(0, "updateInnerLayerEntity", entity, params);
        Try<Integer> result = updateEntityFunction(entity, params);
        doAfterUpdate(0, "updateInnerLayerEntity", entity, result, params);
        if (result.isSuccess()) {

            return new InnerLayerUpdateDeleteResultResponse<>(result.get(), entity);
        }
        else {
            throw result.failed().get();
        }
    }

    @ApiOperation(value = "Delete raw entity", notes = "none")
    @RequestMapping(
            value = "inner-layer/delete",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default InnerLayerUpdateDeleteResultResponse<E> deleteInnerLayerEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger().info(String.format("deleteInnerLayerEntity: %s", entity));
        Map<String, Object> params = new HashMap<>();
        params.put("_request", request);
        params.put("_response", response);
        params.put("_auditIp", AuditUtils.getIpAddr(request));
        doBeforeDelete(0, "deleteInnerLayerEntity", entity, params);
        Try<Integer> result = deleteEntityFunction(entity, params);
        doAfterDelete(0, "deleteInnerLayerEntity", entity, result, params);
        if (result.isSuccess()) {
            return new InnerLayerUpdateDeleteResultResponse<>(result.get(), entity);
        }
        else {
            throw result.failed().get();
        }
    }
}