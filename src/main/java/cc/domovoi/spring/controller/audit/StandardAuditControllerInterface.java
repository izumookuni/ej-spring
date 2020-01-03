package cc.domovoi.spring.controller.audit;

import cc.domovoi.spring.entity.audit.AuditEntityInterface;

public interface StandardAuditControllerInterface<E extends AuditEntityInterface> extends GeneralAuditControllerInterface<String, E> {
}
