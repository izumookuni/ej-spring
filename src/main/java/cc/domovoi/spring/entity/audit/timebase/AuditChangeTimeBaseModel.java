package cc.domovoi.spring.entity.audit.timebase;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "审计记录时间变化记录 L2")
public class AuditChangeTimeBaseModel extends AuditDisplayEntity {

    private List<AuditChangeTimeBaseRecordModel> record;

    @Override
    public String toString() {
        return "AuditChangeTimeBaseModel{" +
                "record=" + record +
                ", auditTime=" + getAuditTime() +
                '}';
    }

    public List<AuditChangeTimeBaseRecordModel> getRecord() {
        return record;
    }

    public void setRecord(List<AuditChangeTimeBaseRecordModel> record) {
        this.record = record;
    }
}
