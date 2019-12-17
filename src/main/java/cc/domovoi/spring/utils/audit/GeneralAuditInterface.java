package cc.domovoi.spring.utils.audit;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.entity.audit.batch.AuditChangeContextGroupBatchModel;
import cc.domovoi.spring.service.audit.AuditServiceInterface;
import cc.domovoi.spring.utils.RecordAuthorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface GeneralAuditInterface<E extends GeneralAuditEntityInterface<?>> {

    Logger logger = LoggerFactory.getLogger("BaseAuditInterface");

    AuditServiceInterface auditService();

    Class<E> auditClass();

    String auditAuthorGetter();

    default List<AuditChangeContextGroupModel> findAuditChangeContextGroupModel(Predicate<? super String> contextNameFilter, Predicate<? super List<String>> scopeIdListFilter, Predicate<? super String> contextIdFilter, Predicate<? super String> auditFieldFilter) {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditChangeRecord(auditDisplayEntityList, auditClass(), contextNameFilter, scopeIdListFilter, contextIdFilter, auditFieldFilter);
    }

    default List<AuditChangeContextGroupModel> findAuditChangeContextGroupModel(Optional<List<String>> contextNameList, Optional<List<String>> scopeIdList, Optional<List<String>> contextIdList, Optional<List<String>> auditFieldList) {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditChangeRecord(auditDisplayEntityList, auditClass(), contextNameList, scopeIdList, contextIdList, auditFieldList);
    }

    default List<AuditChangeContextGroupModel> findAuditChangeContextGroupModel() {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditChangeRecord(auditDisplayEntityList, auditClass());
    }

    default Try<Integer> initAuditRecord(Function<? super E, ? extends List<E>> findEntityFunction, String auditType) {
        try {
            List<E> entityList = findEntityFunction.apply(auditClass().newInstance());
            logger.debug("entityList size:" + entityList.size());
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
                Integer result = auditService().addAudit(auditDisplayEntity);
                logger.debug(String.format("project(%s) add result: %s", entity.getId(), result));
                return result;
            }).sum();
            return new Success<>(addResult);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }

    default List<AuditChangeContextGroupBatchModel<E>> findAuditChangeContextGroupBatchModel(Predicate<? super String> contextNameFilter, Predicate<? super List<String>> scopeIdListFilter, Predicate<? super String> contextIdFilter, Predicate<? super String> fieldNameFilter) {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditBatchChangeRecord(auditDisplayEntityList, auditClass(), contextNameFilter, scopeIdListFilter, contextIdFilter, fieldNameFilter);
    }

    default List<AuditChangeContextGroupBatchModel<E>> findAuditChangeContextGroupBatchModel(Optional<List<String>> contextNameList, Optional<List<String>> scopeIdList, Optional<List<String>> contextIdList, Optional<List<String>> auditFieldList) {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditBatchChangeRecord(auditDisplayEntityList, auditClass(),
                contextName -> contextNameList.map(list -> list.contains(contextName)).orElse(true),
                scopeId -> scopeIdList.map(list -> list.containsAll(scopeId)).orElse(true),
                contextId -> contextIdList.map(list -> list.contains(contextId)).orElse(true),
                fieldName -> auditFieldList.map(list -> list.contains(fieldName)).orElse(true));
    }

    default List<AuditChangeContextGroupBatchModel<E>> findAuditChangeContextGroupBatchModel() {
        List<AuditDisplayEntity> auditDisplayEntityList = auditService().auditMapper().findListByContextName(AuditUtils.contextName(auditClass()));
        return auditService().findAuditBatchChangeRecord(auditDisplayEntityList, auditClass(),
                contextName -> true,
                scopeId -> true,
                contextId -> true,
                fieldName -> true);
    }

}
