package cc.domovoi.spring.mapper.jooq;

import cc.domovoi.lambda.EJLambda;
import cc.domovoi.spring.entity.GeneralAnnotationEntityInterface;
import cc.domovoi.spring.entity.annotation.JoiningProperty;
import cc.domovoi.spring.entity.jooq.GeneralJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.extension.RichRetrieveMapperInterface;
import cc.domovoi.spring.utils.JooqUtils;
import org.jooq.*;
import org.jooq.lambda.function.Function3;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.*;
import static org.joor.Reflect.onClass;

public interface GeneralJooqRetrieveJoiningMapperInterface<R extends TableRecord<R>, K, E extends GeneralJooqDSLEntityInterface<K, R>> extends RichRetrieveMapperInterface<K, E> {

    DSLContext dsl();

    Table<R> table();

    Class<E> entityClass();

    @Override
    default E findBaseById(K id) {
        E e = findEntityUsingIdByDSL(id);
        if (Objects.nonNull(e)) {
            doAfterFindEntity(0, "findBaseById", e);
        }
        return e;
    }

    @Override
    default List<E> findBaseListById(List<K> idList) {
        List<E> eList = dsl().select().from(table()).where(table().field("id", keyClass()).in(idList)).fetch().into(entityClass());
        JooqUtils.joiningColumn(eList, Optional.empty(), entityClass(), dsl());
        doAfterFindList(0, "findBaseListById", eList);
        return eList;
    }

    @Override
    default List<E> findBaseList(E entity) {
        if (Objects.nonNull(entity)) {
            doBeforeFindEntity(0, "findBaseList", entity);
        }
        List<E> eList = findEntityListByDSL(entity);
        doAfterFindList(0, "findBaseList", eList);
        return eList;
    }

    default R convertEntityToRecord(E entity) {
        R record = table().newRecord();
        record.from(entity);
        return record;
    }

    default E convertRecordToEntity(R record) {
        return record.into(entityClass());
    }

    default String keyField() {
        return "id";
    }

    @SuppressWarnings("unchecked")
    default Class<K> keyClass() {
        return (Class<K>) table().field(keyField()).getType();
    }

    default Condition initConditionUsingEntity(E entity, Function3<? super Condition, ? super Record, ? super E, ? extends Condition> addition, Predicate<? super Field<?>> predicateIncluding, Predicate<? super org.jooq.Field<?>> predicateExcluding) {
//        return initConditionUsingPojo(entity.toPojo(), (c, r) -> addition.apply(c, r, entity), predicateIncluding, predicateExcluding);
        Record record = convertEntityToRecord(entity);
        Map<Field<?>, Object> conditionMap = new HashMap<>();
        for (org.jooq.Field<?> field : record.fields()) {
            if (!predicateExcluding.test(field) && predicateIncluding.test(field)) {
                Object value = field.getValue(record);
                if (Objects.nonNull(value)) {
                    conditionMap.put(field, value);
                }
            }
        }
        Condition condition0 = condition(conditionMap);
        return addition.apply(condition0, record, entity);
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

    default ResultQuery<Record> findListByKeyDSL(List<Object> keyList, JoiningProperty joiningProperty) {
        return dsl().select().from(table()).where(field(name(joiningProperty.joiningColumn())).in(keyList));
    }

    default List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass, String name) {
        if (keyList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Tuple2<JoiningProperty, java.lang.reflect.Field>> joiningPropertySet = GeneralAnnotationEntityInterface.joiningPropertySet(entityClass);
        JoiningProperty joiningProperty = joiningPropertySet.stream().filter(jP -> Objects.equals(StringUtils.hasText(jP.v1().value()) ? jP.v1().value() : jP.v2().getName(), context)).findFirst().map(Tuple2::v1).orElseThrow(() -> new RuntimeException(String.format("no joining property %s", context)));
        List<E> eList = findListByKeyDSL(keyList, joiningProperty).fetch().into(entityClass()); // .stream().peek(e -> this.doAfterFindEntity(0, e)).collect(Collectors.toList());
        JooqUtils.joiningColumn(eList, Optional.empty(), entityClass(), dsl());
        doAfterFindList(0, name, eList);
        return eList;
    }

    default E findEntityUsingIdByDSL(K id) {
        E entity = onClass(entityClass()).create().get();
        entity.setId(id);
        return findEntityByDSL(entity);
    }

    default <Re extends Record> SelectConditionStep<Re> findRecordListByKeyDSL(E entity, Function<? super DSLContext, ? extends SelectSelectStep<Re>> selectFunction) {
        return selectFunction.apply(dsl()).from(table()).where(initConditionUsingEntity(entity));
    }

    default SelectConditionStep<Record> findEntityDSLSelectConditionStep(E entity) {
        return findRecordListByKeyDSL(entity, DSLContext::select);
    }

    default ResultQuery<Record> findEntityDSL(E entity) {
        return findEntityDSLSelectConditionStep(entity);
    }

    default E findEntityByDSL(E entity) {
        Record record = findEntityDSL(entity).fetchOne();
        if (Objects.nonNull(record)) {
            E result = record.into(entityClass());
            JooqUtils.joiningColumn(Collections.singletonList(result), Optional.of(entity), entityClass(), dsl());
            return result;
        }
        else {
            return null;
        }
    }

    default List<E> findEntityListByDSL(E entity) {
        List<E> result = findEntityDSL(entity).fetch().into(entityClass());
        JooqUtils.joiningColumn(result, Optional.of(entity), entityClass(), dsl());
        return result;
    }

    default E findEntityDSLAndJoiningColumn(Supplier<? extends E> supplier, Optional<E> query) {
        E e = supplier.get();
        JooqUtils.joiningColumn(Collections.singletonList(e), query, entityClass(), dsl());
        return e;
    }

    default List<E> findListDSLAndJoiningColumn(Supplier<? extends List<E>> supplier, Optional<E> query) {
        List<E> eList = supplier.get();
        JooqUtils.joiningColumn(eList, query, entityClass(), dsl());
        return eList;
    }

}
