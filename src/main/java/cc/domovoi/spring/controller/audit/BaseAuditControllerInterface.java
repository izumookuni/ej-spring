package cc.domovoi.spring.controller.audit;

import cc.domovoi.spring.controller.OriginalCRUDControllerInterface;
import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import cc.domovoi.spring.utils.ControllerUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public interface BaseAuditControllerInterface<E extends AuditEntityInterface, S extends BaseJoiningServiceInterface<E, ?>> extends OriginalCRUDControllerInterface<E>, BaseAuditInterface<E> {

    @Override
    default void afterAdd(E entity, HttpServletRequest request, HttpServletResponse response) {
        recordAddAuditEntity(entity, request);
    }

    @Override
    default void afterUpdate(E entity, HttpServletRequest request, HttpServletResponse response) {
        recordUpdateAuditEntity(entity, request);
    }

    @Override
    default void afterDelete(E entity, HttpServletRequest request, HttpServletResponse response) {
        recordDeleteAuditEntity(entity, request);
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

    @ApiOperation(value = "initAuditRecord", notes = "")
    @RequestMapping(
            value = "init-audit-record",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findAuditChangeContextGroupModel(@RequestBody AuditRequestModel model) {
         return ControllerUtils.commonFunction(logger(), "findAuditChangeContextGroupModel", () -> this.findAuditChangeContextGroupModel(Optional.ofNullable(model.getContextName()), Optional.ofNullable(model.getContextId()), Optional.ofNullable(model.getAuditField())));
    }

    @ApiOperation(value = "initAuditRecord", notes = "")
    @RequestMapping(
            value = "init-audit-record",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> initAuditRecordF() {
        return ControllerUtils.commonTryFunction(logger(), "initAuditRecord", () -> this.initAuditRecord(this::findEntityFunction, "controller"));
    }
}
