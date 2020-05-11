package cc.domovoi.spring.entity.audit.fieldbase;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "审计字段变化记录 L2")
public class AuditChangeFieldModel {

    private String field;

    private List<AuditChangeRecordModel> changeRecord;

    @Override
    public String toString() {
        return "AuditChangeFieldModel{" +
                "field='" + field + '\'' +
                ", changeRecord=" + changeRecord +
                '}';
    }

    public AuditChangeFieldModel() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<AuditChangeRecordModel> getChangeRecord() {
        return changeRecord;
    }

    public void setChangeRecord(List<AuditChangeRecordModel> changeRecord) {
        this.changeRecord = changeRecord;
    }
}
