package cc.domovoi.spring.service.audit;

import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.mapper.BaseMapperInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;

public interface BaseAuditServiceInterface<E extends AuditEntityInterface, M extends BaseMapperInterface<E>> extends BaseJoiningServiceInterface<E, M>, BaseAuditInterface<E> {


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

    default Integer recordAddAuditEntity(E entity) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("add");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
        });
        return auditService().addAudit(auditDisplayEntity);
    }

    default Integer recordUpdateAuditEntity(E entity) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("update");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
        });
        return auditService().addAudit(auditDisplayEntity);
    }

    default Integer recordDeleteAuditEntity(E entity) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("delete");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
        });
        return auditService().addAudit(auditDisplayEntity);
    }
}
