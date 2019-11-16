package cc.domovoi.spring.service.jooq;

import cc.domovoi.lambda.EJLambda;
import cc.domovoi.spring.entity.GeneralAnnotationEntityInterface;
import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.entity.jooq.JoiningColumn;
import cc.domovoi.spring.entity.jooq.JoiningProperty;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;
import org.jooq.*;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;
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

    default R unMapper(P pojo) {
        R record = getTable().newRecord();
        record.from(pojo);
        return record;
    }

    default E convertEntityToPojo(P pojo) {
        E entity = onClass(entityClass()).create().get();
        BeanUtils.copyProperties(pojo, entity);
        return entity;
    }

    default P convertPojoToEntity(E entity) {
        return entity.toPojo();
    }

    default Condition initConditionUsingPojo(P pojo, Predicate<? super org.jooq.Field<?>> predicate) {
        Record record = unMapper(pojo);
        Map<org.jooq.Field<?>, Object> conditionMap = new HashMap<>();
        for (org.jooq.Field<?> field : record.fields()) {
            if (predicate.test(field)) {
                Object value = field.getValue(record);
                if (Objects.nonNull(value)) {
                    conditionMap.put(field, value);
                }
            }
        }
        return condition(conditionMap);
    }

    default Condition initConditionUsingPojo(P pojo) {
        return initConditionUsingPojo(pojo, EJLambda.predicateTrue());
    }

    default Condition initConditionUsingPojoIncludingField(P pojo, String... filedName) {
        List<String> filedNameList = Stream.of(filedName).collect(Collectors.toList());
        return initConditionUsingPojo(pojo, field -> filedNameList.contains(field.getName()));
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
    default List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass) {
        Set<Tuple2<JoiningProperty, java.lang.reflect.Field>> joiningPropertySet = GeneralAnnotationEntityInterface.joiningPropertySet(entityClass);
        JoiningProperty joiningProperty = joiningPropertySet.stream().filter(jP -> Objects.equals(StringUtils.hasText(jP.v1().value()) ? jP.v1().value() : jP.v2().getName(), context)).findFirst().map(Tuple2::v1).orElseThrow(() -> new RuntimeException(String.format("no joining property %s", context)));
        List<E> eList = dsl().select(getTable().asterisk()).from(getTable()).where(field(name(joiningProperty.joiningColumn())).in(keyList)).fetch().into(entityClass()).stream().peek(this::afterFindEntity).collect(Collectors.toList());
        joiningColumn(eList);
        processAfterFindResult(eList);
        return eList;
    }

    default P findPojoByDao(P pojo) {
        return dsl().select(getTable().asterisk()).from(getTable()).where(initConditionUsingPojo(pojo)).fetchOne().into(getType());
    }

    default List<P> findPojoListByDao(P pojo) {
        return dsl().select(getTable().asterisk()).from(getTable()).where(initConditionUsingPojo(pojo)).fetch().into(getType());
    }

    default E findEntityUsingIdByDao(K id) {
        E entity = onClass(entityClass()).create().get();
        entity.setId(id);
        return findEntityByDao(entity);
    }

    default E findEntityByDao(E entity) {
        E result =  dsl().select(getTable().asterisk()).from(getTable()).where(initConditionUsingPojo(entity.toPojo())).fetchOne().into(entityClass());
        joiningColumn(Collections.singletonList(result));
        return result;
    }

    default List<E> findEntityListByDao(E entity) {
        List<E> result = dsl().select(getTable().asterisk()).from(getTable()).where(initConditionUsingPojo(entity.toPojo())).fetch().into(entityClass());
        joiningColumn(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    default void joiningColumn(List<E> entityList) {
        if (!entityList.isEmpty()) {
            java.lang.reflect.Field[] fields = entityClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                JoiningColumn joiningColumn = field.getAnnotation(JoiningColumn.class);
                if (joiningColumn != null) {
                    Class<? extends Table> tableClass = joiningColumn.table();
                    Table<Record> table = onClass(tableClass).create().get();
                    List<Object> keyList = entityList.stream().map(entity -> on(entity).get(joiningColumn.key())).collect(Collectors.toList());
                    Field<?> foreignKeyField = table.field(joiningColumn.foreignKey());
                    Field<?> targetKeyField = table.field(joiningColumn.targetKey());
                    Map targetKeyMap = dsl().select(foreignKeyField, targetKeyField).from(table).where(foreignKeyField.in(keyList)).fetch().intoGroups(foreignKeyField, targetKeyField);
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
