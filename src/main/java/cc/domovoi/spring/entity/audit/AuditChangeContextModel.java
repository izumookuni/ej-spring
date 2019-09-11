package cc.domovoi.spring.entity.audit;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "审计记录变化记录 L3")
public class AuditChangeContextModel {

    private String contextId;

    private List<AuditChangeFieldModel> changeField;

    @Override
    public String toString() {
        return "AuditChangeContextModel{" +
                "contextId='" + contextId + '\'' +
                ", changeField=" + changeField +
                '}';
    }

    public AuditChangeContextModel() {
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public List<AuditChangeFieldModel> getChangeField() {
        return changeField;
    }

    public void setChangeField(List<AuditChangeFieldModel> changeField) {
        this.changeField = changeField;
    }
}
