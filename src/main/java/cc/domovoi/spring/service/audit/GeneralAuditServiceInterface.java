package cc.domovoi.spring.service.audit;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.utils.audit.GeneralAuditInterface;
import org.jooq.lambda.tuple.Tuple2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface GeneralAuditServiceInterface<K, E extends GeneralAuditEntityInterface<K>> extends GeneralJoiningServiceInterface<K, E>, GeneralAuditInterface<E> {

    @AfterAdd(order = -100)
    default void processingAddAudit(E entity, Try<Tuple2<Integer, K>> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        recordAddAuditEntity(entity, request);
    }

    @AfterUpdate(order = -100)
    default void processingUpdateAudit(E entity, Try<Integer> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        recordUpdateAuditEntity(entity, request);
    }

    @AfterDelete(order = -100)
    default void processingDeleteAudit(E entity, Try<Integer> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        recordDeleteAuditEntity(entity, request);
    }

    default Integer recordAddAuditEntity(E entity, Optional<HttpServletRequest> request) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("add");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            request.ifPresent(r -> auditEntity.setAuditIp(AuditUtils.getIpAddr(r)));
        });
        return auditService().addAudit(auditDisplayEntity);
    }

    default Integer recordUpdateAuditEntity(E entity, Optional<HttpServletRequest> request) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("update");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            request.ifPresent(r -> auditEntity.setAuditIp(AuditUtils.getIpAddr(r)));
        });
        return auditService().addAudit(auditDisplayEntity);
    }

    default Integer recordDeleteAuditEntity(E entity, Optional<HttpServletRequest> request) {
        AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
            auditEntity.setAuditBehavior("delete");
            auditEntity.setAuditType("service");
            auditEntity.setAuditLevel("info");
            auditEntity.setAuditAuthor(auditAuthorGetter());
            request.ifPresent(r -> auditEntity.setAuditIp(AuditUtils.getIpAddr(r)));
        });
        return auditService().addAudit(auditDisplayEntity);
    }
}
