package cc.domovoi.spring.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "AuditChangeRecord L1")
public class AuditChangeRecordModel extends AuditDisplayEntity {

    @ApiModelProperty(value = "before")
    private Object before;

    @ApiModelProperty(value = "after")
    private Object after;

    @Override
    public String toString() {
        return "AuditChangeRecordModel{" +
                "before=" + before +
                ", after=" + after +
                '}';
    }

    public AuditChangeRecordModel() {
    }

    public AuditChangeRecordModel(AuditDisplayEntity auditDisplayEntity) {
        BeanUtils.copyProperties(auditDisplayEntity, this);
    }

    public Object getBefore() {
        return before;
    }

    public void setBefore(Object before) {
        this.before = before;
    }

    public Object getAfter() {
        return after;
    }

    public void setAfter(Object after) {
        this.after = after;
    }

    @Override
    @JsonIgnore
    public String getAuditId() {
        return super.getAuditId();
    }

    @Override
    @JsonIgnore
    public String getAuditAuthor() {
        return super.getAuditAuthor();
    }

    @Override
    @JsonIgnore
    public String getAuditIp() {
        return super.getAuditIp();
    }

    @Override
    @JsonIgnore
    public String getAuditUri() {
        return super.getAuditUri();
    }

    @Override
    @JsonIgnore
    public String getAuditLevel() {
        return super.getAuditLevel();
    }

    @Override
    @JsonIgnore
    public String getAuditType() {
        return super.getAuditType();
    }

    @Override
    @JsonIgnore
    public String getAuditScope() {
        return super.getAuditScope();
    }

    @Override
//    @JsonIgnore
    public String getAuditBehavior() {
        return super.getAuditBehavior();
    }

    @Override
    @Deprecated
    @JsonIgnore
    public String getScopeId() {
        return super.getScopeId();
    }

    @Override
    @JsonIgnore
    public String getContextId() {
        return super.getContextId();
    }

    @Override
    @JsonIgnore
    public String getContextPid() {
        return super.getContextPid();
    }

    @Override
    @JsonIgnore
    public String getContextName() {
        return super.getContextName();
    }

    @Override
    @JsonIgnore
    public String getAuditField() {
        return super.getAuditField();
    }

    @Override
    @JsonIgnore
    public String getAuditContent() {
        return super.getAuditContent();
    }

    @Override
    @JsonIgnore
    public LocalDateTime getAuditTime() {
        return super.getAuditTime();
    }

    @Override
    @JsonIgnore
    public List<String> getScopeIdList() {
        return super.getScopeIdList();
    }
}
