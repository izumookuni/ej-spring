package cc.domovoi.spring.service.paging;

import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.service.jooq.GeneralJooqRetrieveJoiningServiceInterface;
import org.jooq.*;
import org.jooq.lambda.tuple.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface GeneralJooqPagingServiceInterface<R extends TableRecord<R>, P, K, E extends GeneralJooqEntityInterface<P, K>> extends GeneralPagingServiceInterface<K, E>, GeneralJooqRetrieveJoiningServiceInterface<R, P, K, E> {

    @Override
    default Integer findCount(E entity) {
        return findRecordListByKeyDSL(entity, DSLContext::selectCount).fetchOne().value1();
    }

    @Override
    default List<E> innerFindPagingList(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder) {
        SelectConditionStep<Record> selectConditionStep = findEntityDSLSelectConditionStep(entity);
        SelectLimitStep<Record> selectLimitStep = orderBy(sortBy, sortOrder).<SelectLimitStep<Record>>map(selectConditionStep::orderBy)
                .orElse(selectConditionStep);
        SelectForUpdateStep<Record> selectForUpdateStep = offsetLimit(pageNum, pageSize).<SelectForUpdateStep<Record>>map(t2 -> selectLimitStep.offset(t2.v1()).limit(t2.v2()))
                .orElse(selectLimitStep);
        List<E> entityList = selectForUpdateStep.fetch().into(entityClass());
        joiningColumn(entityList, Optional.of(entity));
        return entityList;
    }

    default Optional<List<OrderField<?>>> orderBy(List<String> sortBy, List<String> sortOrder0) {
        if (Objects.nonNull(sortBy)) {
            List<String> sortOrder = Objects.nonNull(sortOrder0) ? sortOrder0 : Collections.emptyList();
            int sortOrderSize = sortOrder.size();
            List<OrderField<?>> orderFieldList = IntStream.range(0, sortBy.size()).mapToObj(idx -> {
                String sb = sortBy.get(idx);
                String so = idx < sortOrderSize ? sortOrder.get(idx) : "asc";
                Field<?> field = getTable().field(sb);
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
    static Optional<Integer> rowFrom(Integer pageNum, Integer pageSize) {
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
    static Optional<Integer> rowUntil(Integer pageNum, Integer pageSize) {
        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
            return Optional.of(pageNum * pageSize + 1);
        }
        else {
            return Optional.empty();
        }
    }

    static Optional<Tuple2<Integer, Integer>> offsetLimit(Integer pageNum, Integer pageSize) {
        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
            return Optional.of(new Tuple2<>((pageNum - 1) * pageSize, pageSize));
        }
        else {
            return Optional.empty();
        }
    }
}
