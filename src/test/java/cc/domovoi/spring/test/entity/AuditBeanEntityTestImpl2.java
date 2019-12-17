package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.audit.Audit;
import cc.domovoi.spring.entity.audit.AuditRecord;

import java.time.LocalDateTime;
import java.util.List;

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
    private String v7;

    public AuditBeanEntityTestImpl2() {
    }

    public AuditBeanEntityTestImpl2(String v0, String v1, Integer v2, Double v3, Boolean v4, LocalDateTime v5, List<Integer> v6, String v7) {
        super(v0, v1, v2, v3, v4, v5, v6);
        this.v7 = v7;
    }

    public String getV7() {
        return v7;
    }

    public void setV7(String v7) {
        this.v7 = v7;
    }
}
