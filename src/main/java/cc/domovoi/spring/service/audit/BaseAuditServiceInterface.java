package cc.domovoi.spring.service.audit;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.entity.audit.AuditEntityInterface;
import cc.domovoi.spring.mapper.BaseMapperInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import cc.domovoi.spring.service.audit.AuditServiceInterface;

public interface BaseAuditServiceInterface<E extends BaseJoiningEntityInterface & AuditEntityInterface, M extends BaseMapperInterface<E>> extends BaseJoiningServiceInterface<E, M> {

    AuditServiceInterface auditService();

    String auditAuthorGetter();

    @Override
    default void afterAdd(E entity) {
        recordAddAuditEntity(entity);
    }

    @Override
    default void afterUpdate(E entity) {
        recordUpdateAuditEntity(entity);
    }

    @Override
    default void afterDelete(E entity) {
        recordDeleteAuditEntity(entity);
    }

    default void recordAddAuditEntity(E entity) {
        entity.setAuditBehavior("add");
        entity.setAuditType("service");
        entity.setAuditLevel("info");
        entity.setAuditAuthor(auditAuthorGetter());
        entity.init();
        auditService().addEntity(entity);
    }

    default void recordUpdateAuditEntity(E entity) {
        entity.setAuditBehavior("update");
        entity.setAuditType("service");
        entity.setAuditLevel("info");
        entity.setAuditAuthor(auditAuthorGetter());
        entity.init();
        auditService().addEntity(entity);
    }

    default void recordDeleteAuditEntity(E entity) {
        entity.setAuditBehavior("delete");
        entity.setAuditType("service");
        entity.setAuditLevel("info");
        entity.setAuditAuthor(auditAuthorGetter());
        entity.init();
        auditService().addEntity(entity);
    }
}
