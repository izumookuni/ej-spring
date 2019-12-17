package cc.domovoi.spring.entity.audit.batch;

import cc.domovoi.spring.entity.audit.GeneralAuditEntityInterface;

import java.util.List;

public class AuditChangeContextGroupBatchModel<T extends GeneralAuditEntityInterface> {

    private String contextName;

    private List<AuditChangeScopeBatchModel<T>> changeContext;

    @Override
    public String toString() {
        return "AuditChangeContextGroupBatchModel{" +
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

    public List<AuditChangeScopeBatchModel<T>> getChangeContext() {
        return changeContext;
    }

    public void setChangeContext(List<AuditChangeScopeBatchModel<T>> changeContext) {
        this.changeContext = changeContext;
    }
}
