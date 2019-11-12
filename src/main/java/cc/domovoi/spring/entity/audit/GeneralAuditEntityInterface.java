package cc.domovoi.spring.entity.audit;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.tools.jackson.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.jooq.lambda.function.Consumer2;
import org.jooq.lambda.function.Consumer3;
import org.jooq.lambda.function.Function2;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.joor.Reflect.on;

public interface GeneralAuditEntityInterface<K> extends GeneralJoiningEntityInterface<K>, AuditInterface {

    ObjectMapper objectMapper = ObjectMappers.objectMapper;

    String idFormatter(K id);

    default AuditDisplayEntity asAuditDisplayEntity() {
        if (this instanceof AuditDisplayEntity) {
            return (AuditDisplayEntity) this;
        }
        else {
            Reflect reflect = on(this);

            AuditDisplayEntity auditDisplayEntity = new AuditDisplayEntity();
            auditDisplayEntity.setAuditId(UUID.randomUUID().toString());
            auditDisplayEntity.setAuditTime(LocalDateTime.now());
            auditDisplayEntity.setContextId(idFormatter(this.getId()));
            auditDisplayEntity.setContextName(contextName());

            contextPidField().ifPresent(contextPidField -> auditDisplayEntity.setContextPid(reflect.get(contextPidField.v1().getName())));
            scopeIdField().ifPresent(scopeIdField -> auditDisplayEntity.setScopeId(reflect.get(scopeIdField.v1().getName())));

            try {
                auditDisplayEntity.setAuditContent(objectMapper.writeValueAsString(auditRecordMap()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return auditDisplayEntity;
        }
    }

    default AuditDisplayEntity asAuditDisplayEntity(Consumer<? super AuditDisplayEntity> op) {
        AuditDisplayEntity auditDisplayEntity = asAuditDisplayEntity();
        op.accept(auditDisplayEntity);
        return auditDisplayEntity;
    }

    default List<Tuple2<Field, AuditRecord>> auditRecordList(Predicate<? super AuditRecord> filter) {
        return AuditUtils.auditRecordList(this.getClass(), filter);
    }

    default String contextName() {
        return AuditUtils.contextName(this.getClass());
    }

    default List<Tuple2<Field, AuditRecord>> auditRecordList() {
        return AuditUtils.auditRecordList(this.getClass(), ar -> true);
    }

    default Optional<Audit> audit() {
        return AuditUtils.audit(this.getClass());
    }

    default Optional<Tuple2<Field, AuditRecord>> contextPidField() {
        return AuditUtils.contextPidField(this.getClass());
    }

    default Optional<Tuple2<Field, AuditRecord>> scopeIdField() {
        return AuditUtils.scopeIdField(this.getClass());
    }

    default <T> void insertAuditRecordMap(Map<String, T> auditRecordMap, List<Field> auditFieldList, Function2<? super Reflect, ? super String, ? extends T> op) {
        Reflect reflect = on(this);
        processAuditFieldList(auditFieldList,
                (name, apiModelPropertyOptional) -> auditRecordMap.put(name, op.apply(reflect, name)),
                (name, auditRecord, apiModelPropertyOptional) -> auditRecordMap.put(name, op.apply(reflect, name)),
                (name, auditRecord, apiModelPropertyOptional) -> auditRecordMap.put(auditRecord.key(), op.apply(reflect, name)),
                auditRecord -> "".equals(auditRecord.key()));
    }

    default void processAuditFieldList(List<Field> auditFieldList, Consumer2<String, Optional<ApiModelProperty>> noAuditRecordOp, Consumer3<String, AuditRecord, Optional<ApiModelProperty>> fieldNameAuditRecordOp1, Consumer3<String, AuditRecord, Optional<ApiModelProperty>> fieldNameAuditRecordOp2, Predicate<? super AuditRecord> auditRecordPredicate) {
        AuditUtils.processAuditFieldList(auditFieldList, noAuditRecordOp, fieldNameAuditRecordOp1, fieldNameAuditRecordOp2, auditRecordPredicate);
    }

    default List<Field> auditFieldList() {
        return AuditUtils.auditFieldList(this.getClass());
    }

//    default Map<String, Supplier<Object>> auditRecordGetMap() {
//        Map<String, Supplier<Object>> auditRecordMap = new HashMap<>();
//        List<Field> auditFieldList = auditFieldList();
//        insertAuditRecordMap(auditRecordMap, auditFieldList, (r, n) -> () -> r.get(n));
//        return auditRecordMap;
//    }

    default Map<String, Object> auditRecordMap() {
        Map<String, Object> auditRecordMap = new HashMap<>();
        List<Field> auditFieldList = auditFieldList();
        insertAuditRecordMap(auditRecordMap, auditFieldList, Reflect::get);
        return auditRecordMap;
    }
}