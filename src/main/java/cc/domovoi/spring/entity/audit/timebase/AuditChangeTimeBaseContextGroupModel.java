package cc.domovoi.spring.entity.audit.timebase;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(value = "Audit Change Time Base L4")
public class AuditChangeTimeBaseContextGroupModel {

    private String contextName;

    private List<AuditChangeTimeBaseContextModel> changeContext;

    @Override
    public String toString() {
        return "AuditChangeTimeBaseContextGroupModel{" +
                "contextName='" + contextName + '\'' +
                ", changeContext=" + changeContext +
                '}';
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public List<AuditChangeTimeBaseContextModel> getChangeContext() {
        return changeContext;
    }

    public void setChangeContext(List<AuditChangeTimeBaseContextModel> changeContext) {
        this.changeContext = changeContext;
    }
}
