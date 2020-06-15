package cc.domovoi.spring.entity.audit.timebase;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "审计记录变化记录 L3")
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
