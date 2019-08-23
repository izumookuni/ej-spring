package cc.domovoi.spring.entity.audit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "审计")
public class AuditDisplayEntity implements AuditEntityInterface {

    @ApiModelProperty(value = "ID")
    private String auditId;

    @ApiModelProperty(value = "作者")
    private String auditAuthor;

    @ApiModelProperty(value = "IP")
    private String auditIp;

    @ApiModelProperty(value = "Uri")
    private String auditUri;

    @ApiModelProperty(value = "等级", notes = "info;warn;error")
    private String auditLevel;

    @ApiModelProperty(value = "类型", notes = "用户;系统")
    private String auditType;

    @ApiModelProperty(value = "范围", notes = "移动端;pc")
    private String auditScope;

    @ApiModelProperty(value = "行为", notes = "add;update;delete")
    private String auditBehavior;

    @ApiModelProperty(value = "关联ID")
    private String contextId;

    @ApiModelProperty(value = "关联上级ID")
    private String contextPid;

    @ApiModelProperty(value = "关联名称")
    private String contextName;

    @ApiModelProperty(value = "字段")
    private String auditField;

    @ApiModelProperty(value = "内容")
    private String auditContent;

    @ApiModelProperty(value = "时间")
    private LocalDateTime auditTime;

    @Override
    public String toString() {
        return "AuditDisplayEntity{" +
                "auditId='" + auditId + '\'' +
                ", auditAuthor='" + auditAuthor + '\'' +
                ", auditIp='" + auditIp + '\'' +
                ", auditUri='" + auditUri + '\'' +
                ", auditLevel='" + auditLevel + '\'' +
                ", auditType='" + auditType + '\'' +
                ", auditScope='" + auditScope + '\'' +
                ", auditBehavior='" + auditBehavior + '\'' +
                ", auditField='" + auditField + '\'' +
                ", auditContent='" + auditContent + '\'' +
                ", auditTime=" + auditTime +
                '}';
    }

    public AuditDisplayEntity() {
    }

    @Override
    public String getAuditId() {
        return auditId;
    }

    @Override
    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    @Override
    public String getAuditAuthor() {
        return auditAuthor;
    }

    @Override
    public void setAuditAuthor(String auditAuthor) {
        this.auditAuthor = auditAuthor;
    }

    @Override
    public String getAuditIp() {
        return auditIp;
    }

    @Override
    public void setAuditIp(String auditIp) {
        this.auditIp = auditIp;
    }

    @Override
    public String getAuditUri() {
        return auditUri;
    }

    @Override
    public void setAuditUri(String auditUri) {
        this.auditUri = auditUri;
    }

    @Override
    public String getAuditLevel() {
        return auditLevel;
    }

    @Override
    public void setAuditLevel(String auditLevel) {
        this.auditLevel = auditLevel;
    }

    @Override
    public String getAuditType() {
        return auditType;
    }

    @Override
    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditScope() {
        return auditScope;
    }

    public void setAuditScope(String auditScope) {
        this.auditScope = auditScope;
    }

    @Override
    public String getAuditBehavior() {
        return auditBehavior;
    }

    @Override
    public void setAuditBehavior(String auditBehavior) {
        this.auditBehavior = auditBehavior;
    }

    public String getAuditField() {
        return auditField;
    }

    public void setAuditField(String auditField) {
        this.auditField = auditField;
    }

    @Override
    public String getAuditContent() {
        return auditContent;
    }

    @Override
    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    @Override
    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    @Override
    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getContextPid() {
        return contextPid;
    }

    public void setContextPid(String contextPid) {
        this.contextPid = contextPid;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }
}
