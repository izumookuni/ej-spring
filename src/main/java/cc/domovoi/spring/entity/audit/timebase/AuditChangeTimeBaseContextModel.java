package cc.domovoi.spring.entity.audit.timebase;

import java.util.List;

public class AuditChangeTimeBaseContextModel {

    private String contextId;

    private List<AuditChangeTimeBaseModel> change;

    @Override
    public String toString() {
        return "AuditChangeTimeBaseContextModel{" +
                "contextId='" + contextId + '\'' +
                ", changes=" + change +
                '}';
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public List<AuditChangeTimeBaseModel> getChange() {
        return change;
    }

    public void setChange(List<AuditChangeTimeBaseModel> change) {
        this.change = change;
    }
}
