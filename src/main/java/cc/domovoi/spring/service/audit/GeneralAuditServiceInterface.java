package cc.domovoi.spring.service.audit;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.mapper.GeneralMapperInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.utils.audit.GeneralAuditInterface;
import org.jooq.lambda.tuple.Tuple2;

public interface GeneralAuditServiceInterface<K, E extends GeneralAuditEntityInterface<K>> extends GeneralJoiningServiceInterface<K, E>, GeneralAuditInterface<E> {
    @Override
    default void afterAdd(E entity, Try<Tuple2<Integer, K>> result) {
        recordAddAuditEntity(entity);
    }

    @Override
    default void afterUpdate(E entity, Try<Integer> result) {
        recordUpdateAuditEntity(entity);
    }

    @Override
    default void afterDelete(E entity, Try<Integer> result) {
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
