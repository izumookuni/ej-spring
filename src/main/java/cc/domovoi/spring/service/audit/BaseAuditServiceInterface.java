package cc.domovoi.spring.service.audit;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.entity.audit.AuditDisplayEntity;
import cc.domovoi.spring.entity.audit.AuditEntityInterface;
import cc.domovoi.spring.mapper.BaseMapperInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;

import java.util.List;

public interface BaseAuditServiceInterface<E extends BaseJoiningEntityInterface & AuditEntityInterface, M extends BaseMapperInterface<E>> extends BaseJoiningServiceInterface<E, M> {

    AuditServiceInterface auditService();

    String auditAuthorGetter();

    Class<E> auditClass();

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
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("add");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
        });
        auditService().addAudit(auditDisplayEntity);
    }

    default void recordUpdateAuditEntity(E entity) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("update");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
        });
        auditService().addAudit(auditDisplayEntity);
    }

    default void recordDeleteAuditEntity(E entity) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("delete");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
        });
        auditService().addAudit(auditDisplayEntity);
    }

    default List<Object> findAuditField() {
        // Todo: ...
        return null;
    }
}
