package cc.domovoi.spring.service.audit;

import cc.domovoi.spring.entity.audit.AuditEntityInterface;

public interface StandardAuditServiceInterface<E extends AuditEntityInterface> extends GeneralAuditServiceInterface<String, E> {
}
