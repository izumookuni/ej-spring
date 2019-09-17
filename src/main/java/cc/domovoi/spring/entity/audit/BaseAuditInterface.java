package cc.domovoi.spring.entity.audit;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.service.audit.AuditServiceInterface;
import cc.domovoi.spring.utils.RecordAuthorInterface;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface BaseAuditInterface<E extends AuditEntityInterface> {

    AuditServiceInterface auditService();

    Class<E> auditClass();

    String auditAuthorGetter();

    default List<AuditChangeContextGroupModel> findAuditChangeContextGroupModel(Predicate<? super String> contextNameFilter, Predicate<? super String> contextIdFilter, Predicate<? super String> auditFieldFilter) {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditChangeRecord(auditDisplayEntityList, auditClass(), contextNameFilter, contextIdFilter, auditFieldFilter);
    }

    default List<AuditChangeContextGroupModel> findAuditChangeContextGroupModel(Optional<List<String>> contextNameList, Optional<List<String>> contextIdList, Optional<List<String>> auditFieldList) {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditChangeRecord(auditDisplayEntityList, auditClass(), contextNameList, contextIdList, auditFieldList);
    }

    default List<AuditChangeContextGroupModel> findAuditChangeContextGroupModel() {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditChangeRecord(auditDisplayEntityList, auditClass());
    }

    default Try<Integer> initAuditRecord(Function<? super E, ? extends List<E>> findEntityFunction, String auditType) {
        try {
            List<E> entityList = findEntityFunction.apply(auditClass().newInstance());
            Integer addResult =  entityList.stream().mapToInt(entity -> {
                AuditDisplayEntity auditDisplayEntity = entity.asAuditDisplayEntity(auditEntity -> {
                    auditEntity.setAuditBehavior("add");
                    auditEntity.setAuditType(auditType);
                    auditEntity.setAuditLevel("info");
                    if (entity instanceof RecordAuthorInterface) {
                        auditEntity.setAuditAuthor(((RecordAuthorInterface) entity).getAuthorId());
                    }
                    else {
                        auditEntity.setAuditAuthor(auditAuthorGetter());
                    }

                });
                return auditService().addAudit(auditDisplayEntity);
            }).sum();
            return new Success<>(addResult);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }
}
