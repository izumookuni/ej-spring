package cc.domovoi.spring.controller.audit;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.utils.ControllerUtils;
import io.swagger.annotations.ApiOperation;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface GeneralAuditControllerInterface<K, E extends GeneralAuditEntityInterface<K>> extends GeneralAuditBasicControllerInterface<K, E> {

    @AfterAdd(order = -100)
    default void recordAddAuditEntity(E entity, Try<Tuple2<Integer, K>> result, Map<String, Object> params) {
        recordAddAuditEntity(entity, (HttpServletRequest) params.get("_request"));
    }

    @AfterUpdate(order = -100)
    default void recordUpdateAuditEntity(E entity ,Try<Integer> result, Map<String, Object> params) {
        recordUpdateAuditEntity(entity, (HttpServletRequest) params.get("_request"));
    }

    @AfterDelete(order = -100)
    default void recordDeleteAuditEntity(E entity, Try<Integer> result, Map<String, Object> params) {
        recordDeleteAuditEntity(entity, (HttpServletRequest) params.get("_request"));
    }

    default void recordAddAuditEntity(E entity, HttpServletRequest request) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("add");
            auditEntity.setAuditType("controller");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            auditEntity.setAuditIp(AuditUtils.getIpAddr(request));
            auditEntity.setAuditUri(request.getRequestURI());
        });
        auditService().addAudit(auditDisplayEntity);
    }

    default void recordUpdateAuditEntity(E entity, HttpServletRequest request) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("update");
            auditEntity.setAuditType("controller");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            auditEntity.setAuditIp(AuditUtils.getIpAddr(request));
            auditEntity.setAuditUri(request.getRequestURI());
        });
        auditService().addAudit(auditDisplayEntity);
    }

    default void recordDeleteAuditEntity(E entity, HttpServletRequest request) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("delete");
            auditEntity.setAuditType("controller");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            auditEntity.setAuditIp(AuditUtils.getIpAddr(request));
            auditEntity.setAuditUri(request.getRequestURI());
        });
        auditService().addAudit(auditDisplayEntity);
    }

//    @ApiOperation(value = "find audit change record", notes = "")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK",response = AuditChangeContextGroupModel.class)})
//    @RequestMapping(
//            value = "audit-change-record",
//            method = {RequestMethod.GET, RequestMethod.POST},
//            produces = "application/json")
//    @ResponseBody
//    default Map<String, Object> findAuditChangeContextGroupModel(@RequestBody AuditRequestModel model) {
//         return ControllerUtils.commonFunction(logger(), "findAuditChangeContextGroupModel", () -> this.findAuditChangeContextGroupModel(Optional.ofNullable(model.getContextName()), Optional.ofNullable(model.getScopeId()), Optional.ofNullable(model.getContextId()), Optional.ofNullable(model.getAuditField())));
//    }
//
    @ApiOperation(value = "initAuditRecord", notes = "")
    @RequestMapping(
            value = "init-audit-record",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @Override
    default Map<String, Object> initAuditRecordF() {
        return ControllerUtils.commonTryFunction(logger(), "initAuditRecord", () -> this.initAuditRecord(this::findEntityFunctionForAuditRecord, "controller"));
    }
//
//    default List<E> findEntityFunctionForAuditRecord(E entity) {
//        return findEntityFunction(entity);
//    }
//
//    @ApiOperation(value = "find audit batch change record", notes = "")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK",response = AuditChangeContextGroupBatchModel.class)})
//    @RequestMapping(
//            value = "audit-batch-change-record",
//            method = {RequestMethod.GET, RequestMethod.POST},
//            produces = "application/json")
//    @ResponseBody
//    default Map<String, Object> findAuditChangeContextGroupBatchModel(@RequestBody AuditRequestModel model) {
//        return ControllerUtils.commonFunction(logger(), "findAuditChangeContextGroupBatchModel", () -> this.findAuditChangeContextGroupBatchModel(Optional.ofNullable(model.getContextName()), Optional.ofNullable(model.getScopeId()), Optional.ofNullable(model.getContextId()), Optional.ofNullable(model.getAuditField())));
//    }
}
