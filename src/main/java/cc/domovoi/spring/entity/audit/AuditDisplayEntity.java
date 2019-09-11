package cc.domovoi.spring.entity.audit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@ApiModel(value = "审计")
public class AuditDisplayEntity implements AuditInterface {

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

    @ApiModelProperty(value = "类型")
    private String auditType;

    @ApiModelProperty(value = "范围", notes = "移动端;pc")
    private String auditScope;

    @ApiModelProperty(value = "行为", notes = "add;update;delete")
    private String auditBehavior;

    @ApiModelProperty(value = "范围ID", notes = "项目ID")
    private String scopeId;

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

    public void init() {
        if (Objects.isNull(auditId)) {
            auditId = UUID.randomUUID().toString();
        }
        if (Objects.isNull(auditTime)) {
            auditTime = LocalDateTime.now();
        }
    }

//    @Override
//    public String toString() {
//        return "AuditDisplayEntity{" +
//                "auditId='" + auditId + '\'' +
//                ", auditContent='" + (Objects.nonNull(auditContent) ? (auditContent.length() <= 300) ? auditContent : auditContent.substring(0, 300) + "..." : null) + '\'' +
//                '}';
//    }


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
                ", scopeId='" + scopeId + '\'' +
                ", contextId='" + contextId + '\'' +
                ", contextPid='" + contextPid + '\'' +
                ", contextName='" + contextName + '\'' +
                ", auditField='" + auditField + '\'' +
                ", auditContent='" + (Objects.nonNull(auditContent) ? (auditContent.length() <= 300) ? auditContent : auditContent.substring(0, 300) + "..." : null) + '\'' +
                ", auditTime=" + auditTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditDisplayEntity that = (AuditDisplayEntity) o;
        return Objects.equals(auditId, that.auditId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auditId);
    }

    public AuditDisplayEntity() {
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getAuditAuthor() {
        return auditAuthor;
    }

    public void setAuditAuthor(String auditAuthor) {
        this.auditAuthor = auditAuthor;
    }

    public String getAuditIp() {
        return auditIp;
    }

    public void setAuditIp(String auditIp) {
        this.auditIp = auditIp;
    }

    public String getAuditUri() {
        return auditUri;
    }

    public void setAuditUri(String auditUri) {
        this.auditUri = auditUri;
    }

    public String getAuditLevel() {
        return auditLevel;
    }

    public void setAuditLevel(String auditLevel) {
        this.auditLevel = auditLevel;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditScope() {
        return auditScope;
    }

    public void setAuditScope(String auditScope) {
        this.auditScope = auditScope;
    }

    public String getAuditBehavior() {
        return auditBehavior;
    }

    public void setAuditBehavior(String auditBehavior) {
        this.auditBehavior = auditBehavior;
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
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

    public String getAuditField() {
        return auditField;
    }

    public void setAuditField(String auditField) {
        this.auditField = auditField;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }
}
