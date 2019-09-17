package cc.domovoi.spring.entity.audit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "审计请求")
public class AuditRequestModel {

    @ApiModelProperty(value = "关联名称")
    private List<String> contextName;

    @ApiModelProperty(value = "关联ID")
    private List<String> contextId;

    @ApiModelProperty(value = "字段")
    private List<String> auditField;

    public AuditRequestModel() {
    }

    public List<String> getContextName() {
        return contextName;
    }

    public void setContextName(List<String> contextName) {
        this.contextName = contextName;
    }

    public List<String> getContextId() {
        return contextId;
    }

    public void setContextId(List<String> contextId) {
        this.contextId = contextId;
    }

    public List<String> getAuditField() {
        return auditField;
    }

    public void setAuditField(List<String> auditField) {
        this.auditField = auditField;
    }
}
