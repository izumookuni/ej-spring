package cc.domovoi.spring.entity.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApiModel(value = "Audit")
public class AuditDisplayEntity implements AuditInterface {

    @ApiModelProperty(value = "ID")
    private String auditId;

    @ApiModelProperty(value = "Author")
    private String auditAuthor;

    @ApiModelProperty(value = "IP")
    private String auditIp;

    @ApiModelProperty(value = "Uri")
    private String auditUri;

    @ApiModelProperty(value = "Level", notes = "info;warn;error")
    private String auditLevel;

    @ApiModelProperty(value = "Type")
    private String auditType;

    @ApiModelProperty(value = "Scope", notes = "mobile;pc")
    private String auditScope;

    @ApiModelProperty(value = "Behavior", notes = "add;update;delete")
    private String auditBehavior;

    @Deprecated
    @ApiModelProperty(value = "scope ID", notes = "project ID")
    private String scopeId;

    @ApiModelProperty(value = "context ID")
    private String contextId;

    @ApiModelProperty(value = "context parent ID")
    private String contextPid;

    @ApiModelProperty(value = "context Name")
    private String contextName;

    @ApiModelProperty(value = "audit Field")
    private String auditField;

    @ApiModelProperty(value = "Content")
    private String auditContent;

    @ApiModelProperty(value = "Time")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "scope ID List", notes = "project ID")
    private List<String> scopeIdList;

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
                ", scopeIdList=" + scopeIdList +
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

    @Deprecated
    public String getScopeId() {
        return scopeId;
    }

    @Deprecated
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

    public List<String> getScopeIdList() {
        return scopeIdList;
    }

    public void setScopeIdList(List<String> scopeIdList) {
        this.scopeIdList = scopeIdList;
    }
}
