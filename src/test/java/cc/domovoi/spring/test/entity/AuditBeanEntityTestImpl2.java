package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.audit.Audit;
import cc.domovoi.spring.entity.audit.AuditRecord;

import java.time.LocalDateTime;

@Audit(value = "日志记录测试2", record = {
        @AuditRecord(target = "v1", value = "值1", key = "VALUE1"),
        @AuditRecord(target = "v2"),
        @AuditRecord(target = "v3"),
        @AuditRecord(target = "v4", value = "值4", key = "VALUE4"),
        @AuditRecord(target = "v5", value = "值5"),
        @AuditRecord(target = "v6", value = "值6"),
})
public class AuditBeanEntityTestImpl2 extends AuditBeanEntityTestImpl {

//    @AuditRecord("值6")
    private String v6;

    public AuditBeanEntityTestImpl2() {
    }

    public AuditBeanEntityTestImpl2(String v1, Integer v2, Double v3, Boolean v4, LocalDateTime v5, String v6) {
        super(v1, v2, v3, v4, v5);
        this.v6 = v6;
    }

    public String getV6() {
        return v6;
    }

    public void setV6(String v6) {
        this.v6 = v6;
    }
}
