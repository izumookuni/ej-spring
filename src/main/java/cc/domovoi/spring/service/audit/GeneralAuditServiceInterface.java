package cc.domovoi.spring.service.audit;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.utils.audit.GeneralAuditInterface;
import cc.domovoi.tools.defaults.NullDefaultUtils;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GeneralAuditServiceInterface<K, E extends GeneralAuditEntityInterface<K>> extends GeneralJoiningServiceInterface<K, E>, GeneralAuditInterface<E> {

    @AfterAdd(order = -100)
    default void processingAddAudit(E entity, Try<Tuple2<Integer, K>> result, Map<String, Object> params) {
        recordAddAuditEntity(entity, Optional.ofNullable((String) params.get("_auditIp")));
    }

    @AfterUpdate(order = -100)
    default void processingUpdateAudit(E entity, Try<Integer> result, Map<String, Object> params) {
        recordUpdateAuditEntity(entity,
                Optional.ofNullable((String) params.get("_auditIp")),
                (Boolean) params.get("forced"),
                Optional.ofNullable((List<String>) params.get("setNull")),
                (E) params.get("entityAfterUpdate"));
    }

    @AfterDelete(order = -100)
    default void processingDeleteAudit(E entity, Try<Integer> result, Map<String, Object> params) {
        recordDeleteAuditEntity(entity, Optional.ofNullable((String) params.get("_auditIp")));
    }

    default Integer recordAddAuditEntity(E entity, Optional<String> auditIp) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("add");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            auditIp.ifPresent(auditEntity::setAuditIp);
        });
        return auditService().addAudit(auditDisplayEntity);
    }

    default Integer recordUpdateAuditEntity(E entity, Optional<String> auditIp, Boolean forced, Optional<List<String>> setNull, E entityAfterUpdate) {
        boolean forcedFlag = NullDefaultUtils.defaultBooleanValue(forced);
        boolean setNullFlag = setNull.isPresent();
        final AuditDisplayEntity auditDisplayEntity;
        if (forcedFlag || !setNullFlag) {
            auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
                if (forcedFlag) {
                    auditEntity.setAuditBehavior("update/forced");
                } else {
                    // normal
                    auditEntity.setAuditBehavior("update");
                }
                auditEntity.setAuditType("service");
                auditEntity.setAuditLevel("info");
                auditEntity.setAuditAuthor(auditAuthorGetter());
                auditIp.ifPresent(auditEntity::setAuditIp);
            });
        } else {
            // setNullFlag
            auditDisplayEntity = entityAfterUpdate.asAuditDisplayEntity(auditEntity -> {
                auditEntity.setAuditBehavior("update/forced");
                auditEntity.setAuditType("service");
                auditEntity.setAuditLevel("info");
                auditEntity.setAuditAuthor(auditAuthorGetter());
                auditIp.ifPresent(auditEntity::setAuditIp);
            });
        }
//        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
//            auditEntity.setAuditBehavior("update");
//            auditEntity.setAuditType("service");
//            auditEntity.setAuditLevel("info");
//            auditEntity.setAuditAuthor(auditAuthorGetter());
//            auditIp.ifPresent(auditEntity::setAuditIp);
//        });
        return auditService().addAudit(auditDisplayEntity);
    }

    default Integer recordDeleteAuditEntity(E entity, Optional<String> auditIp) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("delete");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            auditIp.ifPresent(auditEntity::setAuditIp);
        });
        return auditService().addAudit(auditDisplayEntity);
    }
}
