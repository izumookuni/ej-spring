package cc.domovoi.spring.entity.audit.batch;

import cc.domovoi.spring.entity.audit.GeneralAuditEntityInterface;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class AuditChangeScopeBatchModel<T extends GeneralAuditEntityInterface> {

    @ApiModelProperty(value = "before")
    private List<T> before;

    @ApiModelProperty(value = "after")
    private List<T> after;

    public AuditChangeScopeBatchModel() {
    }

    public AuditChangeScopeBatchModel(List<T> before, List<T> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public String toString() {
        return "AuditChangeScopeBatchModel{" +
                "before=" + before +
                ", after=" + after +
                '}';
    }

    public List<T> getBefore() {
        return before;
    }

    public void setBefore(List<T> before) {
        this.before = before;
    }

    public List<T> getAfter() {
        return after;
    }

    public void setAfter(List<T> after) {
        this.after = after;
    }
}
