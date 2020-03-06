package cc.domovoi.spring.service.audit;

import cc.domovoi.spring.entity.audit.*;
import cc.domovoi.spring.entity.audit.batch.AuditChangeContextGroupBatchModel;
import cc.domovoi.spring.entity.audit.batch.AuditChangeScopeBatchModel;
import cc.domovoi.spring.mapper.audit.AuditMapperInterface;
import cc.domovoi.spring.utils.ServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface AuditServiceInterface {

    AuditMapperInterface auditMapper();

    ObjectMapper objectMapper();

    default AuditDisplayEntity findAudit(String id) {
        return auditMapper().findEntityById(id);
    }

    default List<AuditDisplayEntity> findAuditList(AuditDisplayEntity entity) {
        return auditMapper().findList(entity);
    }

    default List<AuditDisplayEntity> findAuditListById(List<String> idList) {
        return ServiceUtils.findListGrouped(auditMapper(), idList, Function.identity(), (l, r) -> r, AuditMapperInterface::findListById);
    }

    default Integer addAudit(AuditDisplayEntity entity) {
        entity.init();
        return auditMapper().addEntity(entity);
    }

    default Integer updateAudit(AuditDisplayEntity entity) {
        if (!StringUtils.hasText(entity.getAuditId())) {
            return -1;
        }
        return auditMapper().updateEntity(entity);
    }

    default Integer deleteAudit(AuditDisplayEntity entity) {
        if (!StringUtils.hasText(entity.getAuditId()) && !StringUtils.hasText(entity.getAuditAuthor()) && !StringUtils.hasText(entity.getAuditIp())) {
            return -1;
        }
        return auditMapper().deleteEntity(entity);
    }

    default Integer addAudit(AuditDisplayEntity entity, Consumer<? super AuditDisplayEntity> op) {
        op.accept(entity);
        return addAudit(entity);
    }

    /**
     * findAuditChangeRecord
     * @param auditDisplayEntityList auditDisplayEntityList
     * @param auditClass auditClass
     * @param contextNameFilter contextNameFilter
     * @param scopeIdListFilter scopeIdListFilter
     * @param contextIdFilter contextIdFilter
     * @param fieldNameFilter fieldNameFilter
     * @param <T> GeneralAuditEntityInterface
     * @return AuditChangeContextGroupModel List
     */
    default <T extends GeneralAuditEntityInterface> List<AuditChangeContextGroupModel> findAuditChangeRecord(List<AuditDisplayEntity> auditDisplayEntityList, Class<T> auditClass, Predicate<? super String> contextNameFilter, Predicate<? super List<String>> scopeIdListFilter, Predicate<? super String> contextIdFilter, Predicate<? super String> fieldNameFilter) {
        List<AuditDisplayEntity> auditDisplayEntityListInScope = auditDisplayEntityList.stream().collect(Collectors.groupingBy(AuditDisplayEntity::getContextId))
                .values().stream().filter(list -> list.stream().anyMatch(auditDisplayEntity -> scopeIdListFilter.test(auditDisplayEntity.getScopeIdList())))
                .flatMap(List::stream).collect(Collectors.toList());

        Map<String, List<AuditDisplayEntity>> auditDisplayEntityListMap = auditDisplayEntityListInScope.stream().filter(auditDisplayEntity -> contextNameFilter.test(auditDisplayEntity.getContextName())).collect(Collectors.groupingBy(AuditDisplayEntity::getContextName)); //  && scopeIdListFilter.test(auditDisplayEntity.getScopeIdList())
        return auditDisplayEntityListMap.entrySet().stream().map(entry -> {
            // context group
            String contextName = entry.getKey();
            Map<String, List<AuditDisplayEntity>> auditDisplayEntityListMap1 = entry.getValue().stream().filter(auditDisplayEntity -> contextIdFilter.test(auditDisplayEntity.getContextId())).collect(Collectors.groupingBy(AuditDisplayEntity::getContextId));
            List<AuditChangeContextModel> auditChangeContextModelList = auditDisplayEntityListMap1.entrySet().stream().map(entry1 -> {
                // context
                String contextId = entry1.getKey();
                List<AuditChangeFieldModel> auditChangeFieldModelList = initAuditChangeFieldModelList(entry1.getValue(), auditClass, fieldNameFilter);
                AuditChangeContextModel auditChangeContextModel = new AuditChangeContextModel();
                auditChangeContextModel.setContextId(contextId);
                auditChangeContextModel.setChangeField(auditChangeFieldModelList);
                return auditChangeContextModel;
            }).collect(Collectors.toList());

            AuditChangeContextGroupModel auditChangeContextGroupModel = new AuditChangeContextGroupModel();
            auditChangeContextGroupModel.setContextName(contextName);
            auditChangeContextGroupModel.setChangeContext(auditChangeContextModelList);
            return auditChangeContextGroupModel;
        }).collect(Collectors.toList());
    }

    /**
     * findAuditChangeRecord
     * @param auditDisplayEntityList auditDisplayEntityList
     * @param auditClass audit Class
     * @param contextNameList contextNameList
     * @param scopeIdList scopeIdList
     * @param contextIdList contextIdList
     * @param auditFieldList auditFieldList
     * @param <T> GeneralAuditEntityInterface
     * @return AuditChangeContextGroupModel List
     */
    default <T extends GeneralAuditEntityInterface> List<AuditChangeContextGroupModel> findAuditChangeRecord(List<AuditDisplayEntity> auditDisplayEntityList, Class<T> auditClass, Optional<List<String>> contextNameList, Optional<List<String>> scopeIdList, Optional<List<String>> contextIdList, Optional<List<String>> auditFieldList) {
        return findAuditChangeRecord(auditDisplayEntityList, auditClass,
                contextName -> contextNameList.map(list -> list.contains(contextName)).orElse(true),
                scopeId -> scopeIdList.map(list -> scopeId.containsAll(list)).orElse(true),
                contextId -> contextIdList.map(list -> list.contains(contextId)).orElse(true),
                fieldName -> auditFieldList.map(list -> list.contains(fieldName)).orElse(true));
    }

    default <T extends GeneralAuditEntityInterface> List<AuditChangeContextGroupModel> findAuditChangeRecord(List<AuditDisplayEntity> auditDisplayEntityList, Class<T> auditClass) {
        return findAuditChangeRecord(auditDisplayEntityList, auditClass,
                contextName -> true,
                scopeId -> true,
                contextId -> true,
                fieldName -> true);
    }

    /**
     * initAuditChangeFieldModelList
     * @param auditDisplayEntityList auditDisplayEntityList with same contextName, contextId
     * @param auditClass audit Class
     * @param auditFieldFilter auditFieldFilter
     * @param <T> GeneralAuditEntityInterface
     * @return AuditChangeFieldModel List
     */
    default <T extends GeneralAuditEntityInterface> List<AuditChangeFieldModel> initAuditChangeFieldModelList(List<AuditDisplayEntity> auditDisplayEntityList, Class<T> auditClass, Predicate<? super String> auditFieldFilter) {
        try {
            List<AuditChangeFieldModel> auditChangeFieldModelList = new ArrayList<>();
            AuditUtils.processAuditFieldListV2(AuditUtils.auditFieldListV2(auditClass), (name, apiModelPropertyOptional) -> {
                if (!auditFieldFilter.test(name)) {
                    return;
                }
                AuditChangeFieldModel auditChangeFieldModel = new AuditChangeFieldModel();
                auditChangeFieldModel.setField(apiModelPropertyOptional.map(ApiModelProperty::value).orElse(name));
                auditChangeFieldModel.setChangeRecord(initAuditChangeRecordModelList(auditDisplayEntityList, name));
                auditChangeFieldModelList.add(auditChangeFieldModel);
            }, (name, auditRecord, apiModelPropertyOptional) -> {
                if (!auditFieldFilter.test(name)) {
                    return;
                }
                AuditChangeFieldModel auditChangeFieldModel = new AuditChangeFieldModel();
                auditChangeFieldModel.setField(!"".equals(auditRecord.value()) ? auditRecord.value() : apiModelPropertyOptional.map(ApiModelProperty::value).orElse((!"".equals(auditRecord.key()) ? auditRecord.key() : name)));
                auditChangeFieldModel.setChangeRecord(initAuditChangeRecordModelList(auditDisplayEntityList, name));
                auditChangeFieldModelList.add(auditChangeFieldModel);
            }, (name, auditRecord, apiModelPropertyOptional) -> {
                if (!auditFieldFilter.test(name)) {
                    return;
                }
                AuditChangeFieldModel auditChangeFieldModel = new AuditChangeFieldModel();
                auditChangeFieldModel.setField(!"".equals(auditRecord.value()) ? auditRecord.value() : apiModelPropertyOptional.map(ApiModelProperty::value).orElse(auditRecord.key()));
                auditChangeFieldModel.setChangeRecord(initAuditChangeRecordModelList(auditDisplayEntityList, auditRecord.key()));
                auditChangeFieldModelList.add(auditChangeFieldModel);
            }, auditRecord -> "".equals(auditRecord.key()));
            return auditChangeFieldModelList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * initAuditChangeRecordModelList
     * @param auditDisplayEntityList auditDisplayEntityList with same contextName, contextId
     * @param key content key
     * @return AuditChangeRecordModel List
     */
    @SuppressWarnings("unchecked")
    default List<AuditChangeRecordModel> initAuditChangeRecordModelList(List<AuditDisplayEntity> auditDisplayEntityList, String key) {
        List<Tuple2<Object, AuditDisplayEntity>> keyContentList = auditDisplayEntityList.stream().map(auditDisplayEntity -> {
            try {
                if (Objects.isNull(auditDisplayEntity.getAuditContent())) {
                    return null;
                }
                Map<String, Object> contentMap = (Map<String, Object>) objectMapper().readValue(auditDisplayEntity.getAuditContent(), Map.class);
                return new Tuple2<>(contentMap.get(key), auditDisplayEntity);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<AuditChangeRecordModel> auditChangeRecordModelList = new ArrayList<>();
        keyContentList.stream().reduce(null, (z, m) -> {
            if (Objects.isNull(m.v1()) || m.v1().equals(z)) {
                // z == m.v1
                return z;
            }
            else {
                // z != m.v1
                AuditChangeRecordModel auditChangeRecordModel = new AuditChangeRecordModel(m.v2());
                auditChangeRecordModel.setBefore(z);
                auditChangeRecordModel.setAfter(m.v1());
                auditChangeRecordModelList.add(auditChangeRecordModel);
                return m.v1();
            }
        }, (l, r) -> Objects.nonNull(r) ? r : l);
        return auditChangeRecordModelList;
    }

    default <T extends GeneralAuditEntityInterface> List<AuditChangeContextGroupBatchModel<T>> findAuditBatchChangeRecord(List<AuditDisplayEntity> auditDisplayEntityList, Class<T> auditClass, Predicate<? super String> contextNameFilter, Predicate<? super List<String>> scopeIdListFilter, Predicate<? super String> contextIdFilter, Predicate<? super String> fieldNameFilter) {
        Map<String, List<AuditDisplayEntity>> auditDisplayEntityListMap = auditDisplayEntityList.stream().filter(auditDisplayEntity -> contextNameFilter.test(auditDisplayEntity.getContextName()) && scopeIdListFilter.test(auditDisplayEntity.getScopeIdList())).collect(Collectors.groupingBy(AuditDisplayEntity::getContextName));
        return auditDisplayEntityListMap.entrySet().stream().map(entry -> {
            // context group
            String contextName = entry.getKey();
            List<List<AuditDisplayEntity>> auditDisplayEntityList2 = new ArrayList<>();
            List<AuditDisplayEntity> buffer = new ArrayList<>();
            Optional<AuditDisplayEntity> last = Optional.empty();
            for (AuditDisplayEntity auditDisplayEntity : entry.getValue()) {
                if (contextIdFilter.test(auditDisplayEntity.getContextId())) {
                    if ("add".equals(auditDisplayEntity.getAuditBehavior())) {
                        buffer.add(auditDisplayEntity);
                        last = Optional.of(auditDisplayEntity);
                    }
                    else if ("delete".equals(auditDisplayEntity.getAuditBehavior())) {
                        if (last.map(l -> "add".equals(l.getAuditBehavior())).orElse(false)) {
                            auditDisplayEntityList2.add(new ArrayList<>(buffer));
                            buffer.clear();
                            last = Optional.of(auditDisplayEntity);
                        }
                    }
                }

            }
            List<AuditChangeScopeBatchModel<T>> auditChangeScopeBatchModelList = initAuditChangeScopeBatchModelList(auditDisplayEntityList2, auditClass, fieldNameFilter);
            AuditChangeContextGroupBatchModel<T> auditChangeContextGroupBatchModel = new AuditChangeContextGroupBatchModel<>();
            auditChangeContextGroupBatchModel.setContextName(contextName);
            auditChangeContextGroupBatchModel.setChangeContext(auditChangeScopeBatchModelList);
            return auditChangeContextGroupBatchModel;
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    default <T extends GeneralAuditEntityInterface> List<AuditChangeScopeBatchModel<T>> initAuditChangeScopeBatchModelList(List<List<AuditDisplayEntity>> auditDisplayEntityList2, Class<T> auditClass, Predicate<? super String> auditFieldFilter) {
        try {
            List<List<T>> auditEntityList2 = auditDisplayEntityList2.stream().map(auditEntityList -> auditEntityList.stream().map(auditEntity -> {
                try {
                    Map<String, Object> entityMap = Objects.nonNull(auditEntity.getAuditContent()) ? (Map<String, Object>) objectMapper().readValue(auditEntity.getAuditContent(), Map.class) : Collections.emptyMap();
                    Reflect reflect = Reflect.onClass(auditClass).create();
                    AuditUtils.processAuditFieldListV2(AuditUtils.auditFieldListV2(auditClass), (name, apiModelPropertyOptional) -> {
                        if (!auditFieldFilter.test(name)) {
                            return;
                        }
                        reflect.set(name, entityMap.get(name));
                    }, (name, auditRecord, apiModelPropertyOptional) -> {
                        if (!auditFieldFilter.test(name)) {
                            return;
                        }
                        reflect.set(name, entityMap.get(name));
                    }, (name, auditRecord, apiModelPropertyOptional) -> {
                        if (!auditFieldFilter.test(name)) {
                            return;
                        }
                        reflect.set(name, entityMap.get(auditRecord.key()));
                    }, auditRecord -> "".equals(auditRecord.key()));
                    return reflect.<T>get();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList())).collect(Collectors.toList());
            return Seq.scanLeft(auditEntityList2.stream(), new Tuple2<>(Collections.<T>emptyList(), Collections.<T>emptyList()), (z, eL) -> new Tuple2<>(z.v2(), eL))
                    .drop(1).map(t2 -> new AuditChangeScopeBatchModel<>(t2.v1(), t2.v2())).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
