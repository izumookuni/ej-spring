package cc.domovoi.spring.entity.audit.fieldbase;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "Audit Change L4")
public class AuditChangeContextGroupModel {

    private String contextName;

    private List<AuditChangeContextModel> changeContext;

    @Override
    public String toString() {
        return "AuditChangeContextGroupModel{" +
                "contextName='" + contextName + '\'' +
                ", changeContext=" + changeContext +
                '}';
    }

    public AuditChangeContextGroupModel() {
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public List<AuditChangeContextModel> getChangeContext() {
        return changeContext;
    }

    public void setChangeContext(List<AuditChangeContextModel> changeContext) {
        this.changeContext = changeContext;
    }
}
