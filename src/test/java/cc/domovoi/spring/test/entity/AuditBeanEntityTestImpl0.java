package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.audit.AuditRecord;

public class AuditBeanEntityTestImpl0 {

    @AuditRecord(value = "值0", key = "VALUE0")
    private String v0;

    public AuditBeanEntityTestImpl0() {
    }

    public AuditBeanEntityTestImpl0(String v0) {
        this.v0 = v0;
    }

    public String getV0() {
        return v0;
    }

    public void setV0(String v0) {
        this.v0 = v0;
    }
}
