package cc.domovoi.spring.entity.audit.timebase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "审计字段变化记录 L1")
public class AuditChangeTimeBaseRecordModel {

    @ApiModelProperty(value = "field")
    private String field;

    @ApiModelProperty(value = "before")
    private Object before;

    @ApiModelProperty(value = "after")
    private Object after;

    @Override
    public String toString() {
        return "AuditChangeTimeBaseRecordModel{" +
                "field='" + field + '\'' +
                ", before=" + before +
                ", after=" + after +
                '}';
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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
}
