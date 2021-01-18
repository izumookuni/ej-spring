package cc.domovoi.spring.utils;

import cc.domovoi.spring.entity.annotation.JoiningColumn;
import cc.domovoi.spring.entity.audit.AuditUtils;
import cc.domovoi.spring.service.jooq.JoiningColumnFunctionInterfaceImpl;
import org.jooq.*;
import org.jooq.lambda.function.Function2;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.joor.Reflect.on;
import static org.joor.Reflect.onClass;

public class JooqUtils {

    private static Logger logger = CommonLogger.logger;

    @SuppressWarnings("unchecked")
    public static <E> void joiningColumn(List<E> entityList, Optional<E> query, Class<E> clazz, DSLContext dsl) {
        logger.debug("joiningColumn");
        if (!entityList.isEmpty()) {
//            java.lang.reflect.Field[] fields = entityClass().getDeclaredFields();
            List<java.lang.reflect.Field> fields = ReflectUtils.allFieldList(clazz);
            for (java.lang.reflect.Field field : fields) {
                JoiningColumn joiningColumn = field.getAnnotation(JoiningColumn.class);
                if (joiningColumn != null) {
                    logger.debug("joiningColumn field " + field.getName());
                    List<Object> keyList = entityList.stream().map(entity -> on(entity).get(joiningColumn.key())).filter(Objects::nonNull).collect(Collectors.toList());
                    if (keyList.isEmpty()) {
                        return;
                    }
                    Map targetKeyMap;
                    if (!joiningColumn.custom().equals(JoiningColumnFunctionInterfaceImpl.class)) {
                        targetKeyMap = onClass(joiningColumn.custom()).create().call("apply", dsl, keyList, query).get();
                    }
                    else {
                        Class<? extends Table> tableClass = joiningColumn.table();
                        Table<Record> table = onClass(tableClass).create().get();
                        Field<?> foreignKeyField = table.field(joiningColumn.foreignKey());
                        Field<?> targetKeyField = table.field(joiningColumn.targetKey());
                        targetKeyMap = dsl.select(foreignKeyField, targetKeyField).from(table).where(foreignKeyField.in(keyList)).fetch().intoGroups(foreignKeyField, targetKeyField);
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

    public static Optional<List<OrderField<?>>> orderBy(Table<?> table, List<String> sortBy, List<String> sortOrder0) {
        if (Objects.nonNull(sortBy)) {
            List<String> sortOrder = Objects.nonNull(sortOrder0) ? sortOrder0 : Collections.emptyList();
            int sortOrderSize = sortOrder.size();
            List<OrderField<?>> orderFieldList = IntStream.range(0, sortBy.size()).mapToObj(idx -> {
                String sb = sortBy.get(idx);
                String so = idx < sortOrderSize ? sortOrder.get(idx) : "asc";
                Field<?> field = table.field(sb);
                switch (so) {
                    case "asc":
                        return field.asc();
                    case "desc":
                        return field.desc();
                    case "asc nulls first":
                        return field.asc().nullsFirst();
                    case "asc nulls last":
                        return field.asc().nullsLast();
                    case "desc nulls first":
                        return field.desc().nullsFirst();
                    case "desc nulls last":
                        return field.desc().nullsLast();
                    default:
                        return field.sortDefault();
                }
            }).collect(Collectors.toList());
            return Optional.of(orderFieldList);
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Start line, the minimum value is 1.
     *
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Start line
     */
    public static Optional<Integer> rowFrom(Integer pageNum, Integer pageSize) {
        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
            return Optional.of((pageNum - 1) * pageSize + 1);
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * End line, excluding this line.
     *
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return End line
     */
    public static Optional<Integer> rowUntil(Integer pageNum, Integer pageSize) {
        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
            return Optional.of(pageNum * pageSize + 1);
        }
        else {
            return Optional.empty();
        }
    }

    public static Optional<Tuple2<Integer, Integer>> offsetLimit(Integer pageNum, Integer pageSize) {
        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
            return Optional.of(new Tuple2<>((pageNum - 1) * pageSize, pageSize));
        }
        else {
            return Optional.empty();
        }
    }

    public static <Re extends Record> SelectForUpdateStep<Re> selectOrderByOffsetLimit(Table<?> table, SelectConditionStep<Re> selectConditionStep, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder) {
        SelectLimitStep<Re> selectLimitStep = orderBy(table, sortBy, sortOrder).<SelectLimitStep<Re>>map(selectConditionStep::orderBy)
                .orElse(selectConditionStep);
        return offsetLimit(pageNum, pageSize).<SelectForUpdateStep<Re>>map(t2 -> selectLimitStep.offset(t2.v1()).limit(t2.v2()))
                .orElse(selectLimitStep);
    }

    public static <R, M, Re extends Record> Tuple2<Integer, List<R>> findPagingList(
            Table<?> table,
            M requestModel,
            Function2<? super M, ? super Function<? super DSLContext, ? extends SelectSelectStep<Re>>, ? extends SelectConditionStep<Re>> dslFunction,
            Function<? super DSLContext, ? extends SelectSelectStep<Record1<Integer>>> selectCountFunction,
            Function<? super DSLContext, ? extends SelectSelectStep<Re>> selectFunction,
            Function<? super SelectForUpdateStep<Re>, ? extends List<R>> fetchFunction,
            Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder) {
        Integer total = dslFunction.apply(requestModel, (Function<? super DSLContext, ? extends SelectSelectStep<Re>>) selectCountFunction).fetchOne().into(Integer.class);
        SelectConditionStep<Re> selectConditionStep = dslFunction.apply(requestModel, selectFunction);
        SelectForUpdateStep<Re> selectForUpdateStep = selectOrderByOffsetLimit(table, selectConditionStep, pageNum, pageSize, sortBy, sortOrder);
        List<R> entityList = fetchFunction.apply(selectForUpdateStep);
        return new Tuple2<>(total, entityList);
    }

}
