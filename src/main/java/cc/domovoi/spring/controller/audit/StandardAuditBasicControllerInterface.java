package cc.domovoi.spring.controller.audit;

import cc.domovoi.spring.entity.audit.AuditEntityInterface;

public interface StandardAuditBasicControllerInterface<E extends AuditEntityInterface> extends GeneralAuditBasicControllerInterface<String, E> {
}
