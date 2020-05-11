package cc.domovoi.spring.service.jooq;

import cc.domovoi.lambda.EJLambda;
import cc.domovoi.spring.entity.GeneralAnnotationEntityInterface;
import cc.domovoi.spring.entity.audit.AuditUtils;
import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.entity.annotation.JoiningColumn;
import cc.domovoi.spring.entity.annotation.JoiningProperty;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;
import org.jooq.*;
import org.jooq.lambda.function.Function3;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.*;
import static org.joor.Reflect.*;

/**
 * BaseRetrieveServiceInterface.
 *
 * @param <R> Record
 * @param <P> Pojo
 * @param <K> Key
 * @param <E> Entity
 */
public interface GeneralJooqRetrieveJoiningServiceInterface<R extends TableRecord<R>, P, K, E extends GeneralJooqEntityInterface<P, K>> extends DAO<R, P, K>, GeneralRetrieveJoiningServiceInterface<K, E> {

    DSLContext dsl();

    @SuppressWarnings("unchecked")
    default R unMapper(P pojo) {
        R record = getTable().newRecord();
        BeanUtils.copyProperties(pojo, record);
//        record.from(pojo);
        return record;
    }

    default E convertPojoToEntity(P pojo) {
        E entity = onClass(entityClass()).create().get();
        BeanUtils.copyProperties(pojo, entity);
        return entity;
    }

    default P convertEntityToPojo(E entity) {
        return entity.toPojo();
    }

    default String keyField() {
        return "id";
    }

    @SuppressWarnings("unchecked")
    default Class<K> keyClass() {
        return (Class<K>) getTable().field(keyField()).getType();
    }

    default Condition initConditionUsingEntity(E entity, Function3<? super Condition, ? super Record, ? super E, ? extends Condition> addition, Predicate<? super org.jooq.Field<?>> predicateIncluding, Predicate<? super org.jooq.Field<?>> predicateExcluding) {
        return initConditionUsingPojo(entity.toPojo(), (c, r) -> addition.apply(c, r, entity), predicateIncluding, predicateExcluding);
    }

    default Condition initConditionUsingEntity(E entity) {
        return initConditionUsingEntity(entity, (c, r, e) -> c, EJLambda.predicateTrue(), EJLambda.predicateFalse());
    }

    default Condition initConditionUsingEntity(E entity, Function3<? super Condition, ? super Record, ? super E, ? extends Condition> addition) {
        return initConditionUsingEntity(entity, addition, EJLambda.predicateTrue(), EJLambda.predicateFalse());
    }

    default Condition initConditionUsingEntityIncludingField(E entity, Function3<? super Condition, ? super Record, ? super E, ? extends Condition> addition, String... filedName) {
        List<String> filedNameList = Stream.of(filedName).collect(Collectors.toList());
        return initConditionUsingEntity(entity, addition, field -> filedNameList.contains(field.getName()), EJLambda.predicateFalse());
    }

    default Condition initConditionUsingEntityIncludingField(E entity, String... filedName) {
        return  initConditionUsingEntityIncludingField(entity, (c, r, e) -> c, filedName);
    }

    default Condition initConditionUsingPojo(P pojo, BiFunction<? super Condition, ? super Record, ? extends Condition> addition, Predicate<? super org.jooq.Field<?>> predicateIncluding, Predicate<? super org.jooq.Field<?>> predicateExcluding) {
        Record record = unMapper(pojo);
        Map<org.jooq.Field<?>, Object> conditionMap = new HashMap<>();
        for (org.jooq.Field<?> field : record.fields()) {
            if (!predicateExcluding.test(field) && predicateIncluding.test(field)) {
                Object value = field.getValue(record);
                if (Objects.nonNull(value)) {
                    conditionMap.put(field, value);
                }
            }
        }
        Condition condition0 = condition(conditionMap);
        return addition.apply(condition0, record);
    }

    default Condition initConditionUsingPojo(P pojo) {
        return initConditionUsingPojo(pojo, (c, r) -> c, EJLambda.predicateTrue(), EJLambda.predicateFalse());
    }

    default Condition initConditionUsingPojo(P pojo, BiFunction<? super Condition, ? super Record, ? extends Condition> addition) {
        return initConditionUsingPojo(pojo, addition, EJLambda.predicateTrue(), EJLambda.predicateFalse());
    }

    default Condition initConditionUsingPojoIncludingField(P pojo, BiFunction<? super Condition, ? super Record, ? extends Condition> addition, String... filedName) {
        List<String> filedNameList = Stream.of(filedName).collect(Collectors.toList());
        return initConditionUsingPojo(pojo, addition, field -> filedNameList.contains(field.getName()), EJLambda.predicateFalse());
    }

    default Condition initConditionUsingPojoIncludingField(P pojo, String... filedName) {
        return  initConditionUsingPojoIncludingField(pojo, (c, r) -> c, filedName);
    }

    @Override
    default E innerFindEntity(K id) {
        return findEntityUsingIdByDao(id);
    }

    @Override
    default List<E> innerFindList(E entity) {
        return findEntityListByDao(entity);
    }

    @Override
    default List<E> innerFindListById(List<K> idList) {
        List<E> result = dsl().select().from(getTable()).where(getTable().field("id", keyClass()).in(idList)).fetch().into(entityClass());
        joiningColumn(result, Optional.empty());
        return result;
    }

    default ResultQuery<Record> findListByKeyDSL(List<Object> keyList, JoiningProperty joiningProperty) {
        return dsl().select().from(getTable()).where(field(name(joiningProperty.joiningColumn())).in(keyList));
    }

    @Override
    default List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass, String name) {
        if (keyList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Tuple2<JoiningProperty, java.lang.reflect.Field>> joiningPropertySet = GeneralAnnotationEntityInterface.joiningPropertySet(entityClass);
        JoiningProperty joiningProperty = joiningPropertySet.stream().filter(jP -> Objects.equals(StringUtils.hasText(jP.v1().value()) ? jP.v1().value() : jP.v2().getName(), context)).findFirst().map(Tuple2::v1).orElseThrow(() -> new RuntimeException(String.format("no joining property %s", context)));
        List<E> eList = findListByKeyDSL(keyList, joiningProperty).fetch().into(entityClass()); // .stream().peek(e -> this.doAfterFindEntity(0, e)).collect(Collectors.toList());
        joiningColumn(eList, Optional.empty());
        doAfterFindList(0, name, eList);
//        processAfterFindResult(eList);
        return eList;
    }

    default ResultQuery<Record> findPojoDSL(P pojo) {
        return dsl().select().from(getTable()).where(initConditionUsingPojo(pojo));
    }

    default P findPojoByDao(P pojo) {
        Record record = findPojoDSL(pojo).fetchOne();
        if (Objects.nonNull(record)) {
            return record.into(getType());
        }
        else {
            return null;
        }
    }

    default List<P> findPojoListByDao(P pojo) {
        return findPojoDSL(pojo).fetch().into(getType());
    }

    default E findEntityUsingIdByDao(K id) {
        E entity = onClass(entityClass()).create().get();
        entity.setId(id);
        return findEntityByDao(entity);
    }


    default <Re extends Record> SelectConditionStep<Re> findRecordListByKeyDSL(E entity, Function<? super DSLContext, ? extends SelectSelectStep<Re>> selectFunction) {
        return selectFunction.apply(dsl()).from(getTable()).where(initConditionUsingEntity(entity));
    }

    default SelectConditionStep<Record> findEntityDSLSelectConditionStep(E entity) {
        return findRecordListByKeyDSL(entity, DSLContext::select);
//        return dsl().select().from(getTable()).where(initConditionUsingEntity(entity));
    }

    default ResultQuery<Record> findEntityDSL(E entity) {
        return findEntityDSLSelectConditionStep(entity);
    }

    default E findEntityByDao(E entity) {
        Record record = findEntityDSL(entity).fetchOne();
        if (Objects.nonNull(record)) {
            E result = record.into(entityClass());
            joiningColumn(Collections.singletonList(result), Optional.of(entity));
            return result;
        }
        else {
            return null;
        }
    }

    default List<E> findEntityListByDao(E entity) {
        List<E> result = findEntityDSL(entity).fetch().into(entityClass());
        joiningColumn(result, Optional.of(entity));
        return result;
    }

    default E findEntityDSLAndJoiningColumn(Supplier<? extends E> supplier, Optional<E> query) {
        E e = supplier.get();
        joiningColumn(Collections.singletonList(e), query);
        return e;
    }

    default List<E> findListDSLAndJoiningColumn(Supplier<? extends List<E>> supplier, Optional<E> query) {
        List<E> eList = supplier.get();
        joiningColumn(eList, query);
        return eList;
    }

    @SuppressWarnings("unchecked")
    default void joiningColumn(List<E> entityList, Optional<E> query) {
        logger().debug("joiningColumn");
        if (!entityList.isEmpty()) {
//            java.lang.reflect.Field[] fields = entityClass().getDeclaredFields();
            List<java.lang.reflect.Field> fields = AuditUtils.allFieldList(entityClass());
            for (java.lang.reflect.Field field : fields) {
                JoiningColumn joiningColumn = field.getAnnotation(JoiningColumn.class);
                if (joiningColumn != null) {
                    logger().debug("joiningColumn field " + field.getName());
                    List<Object> keyList = entityList.stream().map(entity -> on(entity).get(joiningColumn.key())).filter(Objects::nonNull).collect(Collectors.toList());
                    if (keyList.isEmpty()) {
                        return;
                    }
                    Map targetKeyMap;
                    if (!joiningColumn.custom().equals(JoiningColumnFunctionInterfaceImpl.class)) {
                        targetKeyMap = onClass(joiningColumn.custom()).create().call("apply", dsl(), keyList, query).get();
                    }
                    else {
                        Class<? extends Table> tableClass = joiningColumn.table();
                        Table<Record> table = onClass(tableClass).create().get();
                        Field<?> foreignKeyField = table.field(joiningColumn.foreignKey());
                        Field<?> targetKeyField = table.field(joiningColumn.targetKey());
                        targetKeyMap = dsl().select(foreignKeyField, targetKeyField).from(table).where(foreignKeyField.in(keyList)).fetch().intoGroups(foreignKeyField, targetKeyField);
                    }
//                    Map targetKeyMap = dsl().select(foreignKeyField, targetKeyField).from(table).where(foreignKeyField.in(keyList)).fetch().intoGroups(foreignKeyField, targetKeyField);
                    entityList.forEach(entity -> {
                        Reflect reflect = on(entity);
                        if (targetKeyMap.containsKey(reflect.get(joiningColumn.key()))) {
                            Class<?> fieldClass = field.getType();
                            if (fieldClass.getSimpleName().matches("(List)|(ArrayList)")) {
                                reflect.set(field.getName(), targetKeyMap.get(reflect.get(joiningColumn.key())));
                            }
                            else {
                                reflect.set(field.getName(), ((List<Object>) targetKeyMap.get(reflect.get(joiningColumn.key()))).get(0));
                            }
                        }
                    });
                }
            }
        }
    }
}
