package cc.domovoi.spring.controller.audit;

import cc.domovoi.spring.controller.GeneralCRUDControllerInterface;
import cc.domovoi.spring.entity.audit.fieldbase.AuditChangeContextGroupModel;
import cc.domovoi.spring.entity.audit.AuditRequestModel;
import cc.domovoi.spring.entity.audit.GeneralAuditEntityInterface;
import cc.domovoi.spring.entity.audit.batch.AuditChangeContextGroupBatchModel;
import cc.domovoi.spring.utils.ControllerUtils;
import cc.domovoi.spring.utils.audit.GeneralAuditInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GeneralAuditBasicControllerInterface<K, E extends GeneralAuditEntityInterface<K>> extends GeneralCRUDControllerInterface<K, E>, GeneralAuditInterface<E> {

    @ApiOperation(value = "find audit change record", notes = "")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK",response = AuditChangeContextGroupModel.class)})
    @RequestMapping(
            value = "audit-change-record",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findAuditChangeContextGroupModel(@RequestBody AuditRequestModel model) throws Exception {
        return ControllerUtils.commonFunction(logger(), "findAuditChangeContextGroupModel", () -> this.findAuditChangeContextGroupModel(Optional.ofNullable(model.getContextName()), Optional.ofNullable(model.getScopeId()), Optional.ofNullable(model.getContextId()), Optional.ofNullable(model.getAuditField())));
    }

    @ApiOperation(value = "initAuditRecord", notes = "")
    @RequestMapping(
            value = "init-audit-record",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> initAuditRecordF() throws Exception {
        return ControllerUtils.commonTryFunction(logger(), "initAuditRecord", () -> this.initAuditRecord(this::findEntityFunctionForAuditRecord, "service"));
    }

    default List<E> findEntityFunctionForAuditRecord(E entity) {
        return findEntityFunction(entity);
    }

    @ApiOperation(value = "find audit batch change record", notes = "")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK",response = AuditChangeContextGroupBatchModel.class)})
    @RequestMapping(
            value = "audit-batch-change-record",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findAuditChangeContextGroupBatchModel(@RequestBody AuditRequestModel model) throws Exception {
        return ControllerUtils.commonFunction(logger(), "findAuditChangeContextGroupBatchModel", () -> this.findAuditChangeContextGroupBatchModel(Optional.ofNullable(model.getContextName()), Optional.ofNullable(model.getScopeId()), Optional.ofNullable(model.getContextId()), Optional.ofNullable(model.getAuditField())));
    }
}
