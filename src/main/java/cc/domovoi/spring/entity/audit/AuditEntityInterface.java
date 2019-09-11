package cc.domovoi.spring.entity.audit;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.tools.jackson.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.lambda.function.Function2;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.*;

public interface AuditEntityInterface extends BaseJoiningEntityInterface, AuditInterface {

    ObjectMapper objectMapper = ObjectMappers.objectMapper;

    default AuditDisplayEntity asAuditDisplayEntity() {
        if (this instanceof AuditDisplayEntity) {
            return (AuditDisplayEntity) this;
        }
        else {
            AuditDisplayEntity auditDisplayEntity = new AuditDisplayEntity();
            auditDisplayEntity.setAuditId(UUID.randomUUID().toString());
            auditDisplayEntity.setAuditTime(LocalDateTime.now());
            auditDisplayEntity.setContextId(this.getId());
            auditDisplayEntity.setContextName(audit().flatMap(audit -> !"".equals(audit.value()) ? Optional.of(audit.value()) : Optional.empty()).orElseGet(() -> this.getClass().getSimpleName()));
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
        Field[] fields = this.getClass().getDeclaredFields();
        return Stream.of(fields).map(field -> new Tuple2<>(field, field.getAnnotation(AuditRecord.class))).filter(t2 -> Objects.nonNull(t2.v2())).filter(t2 -> filter.test(t2.v2())).collect(Collectors.toList());
    }

    default List<Tuple2<Field, AuditRecord>> auditRecordList() {
        return auditRecordList(ar -> true);
    }

    default Optional<Audit> audit() {
        return Optional.ofNullable(this.getClass().getAnnotation(Audit.class));
    }

    default <T> void insertAuditRecordMap(Map<String, T> auditRecordMap, List<Field> auditFieldList, Function2<? super Reflect, ? super String, ? extends T> op) {
        Reflect reflect = on(this);
        processAuditFieldList(auditFieldList,
                (name) -> auditRecordMap.put(name, op.apply(reflect, name)),
                (name, auditRecord) -> auditRecordMap.put(name, op.apply(reflect, name)),
                (name, auditRecord) -> auditRecordMap.put(auditRecord.key(), op.apply(reflect, name)),
                auditRecord -> "".equals(auditRecord.key()));
    }

    default void processAuditFieldList(List<Field> auditFieldList, Consumer<String> noAuditRecordOp, BiConsumer<String, AuditRecord> fieldNameAuditRecordOp1, BiConsumer<String, AuditRecord> fieldNameAuditRecordOp2, Predicate<? super AuditRecord> auditRecordPredicate) {
        auditFieldList.forEach(field -> {
            AuditRecord auditRecord = field.getAnnotation(AuditRecord.class);
            String name = field.getName();
            if (Objects.isNull(auditRecord)) {
                noAuditRecordOp.accept(name);
            }
            else if (!auditRecord.ignore() && auditRecordPredicate.test(auditRecord)) {
                fieldNameAuditRecordOp1.accept(name, auditRecord);
            }
            else if (!auditRecord.ignore()) {
                fieldNameAuditRecordOp2.accept(name, auditRecord);
            }
        });
    }

    default List<Field> auditFieldList() {
        Optional<Audit> auditOptional = audit();
        Field[] fields = this.getClass().getDeclaredFields();
        return auditOptional.map(audit -> {
            List<String> include = Arrays.asList(audit.include());
            List<String> skip = Arrays.asList(audit.skip());
            List<String> inner = auditRecordList(auditRecord -> !auditRecord.ignore()).stream().map(t2 -> t2.v1().getName()).collect(Collectors.toList());
            return Stream.of(fields).filter(field -> !skip.contains(field.getName()) && (audit.containsAll() || include.contains(field.getName()) || (include.isEmpty() && inner.contains(field.getName())))).collect(Collectors.toList());
        }).orElseGet(() -> auditRecordList(auditRecord -> !auditRecord.ignore()).stream().map(Tuple2::v1).collect(Collectors.toList()));
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
