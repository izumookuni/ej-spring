package cc.domovoi.spring.service.paging;

import cc.domovoi.spring.entity.jooq.GeneralJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.jooq.GeneralJooqRetrieveJoiningMapperInterface;
import cc.domovoi.spring.service.jooq.dsl.GeneralJooqDSLRetrieveJoiningServiceInterface;
import cc.domovoi.spring.utils.JooqUtils;
import org.jooq.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface GeneralJooqDSLPagingServiceInterface<K, E extends GeneralJooqDSLEntityInterface<K, ?>, M extends GeneralJooqRetrieveJoiningMapperInterface<?, K, E>> extends GeneralPagingServiceInterface<K, E>, GeneralJooqDSLRetrieveJoiningServiceInterface<K, E, M> {

    @Override
    default Integer findCount(E entity) {
        return mvcMapper().findRecordListByKeyDSL(entity, DSLContext::selectCount).fetchOne().value1();
    }

    @Override
    default List<E> innerFindPagingList(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder) {
        SelectConditionStep<Record> selectConditionStep = mvcMapper().findEntityDSLSelectConditionStep(entity);
        SelectLimitStep<Record> selectLimitStep = orderBy(sortBy, sortOrder).<SelectLimitStep<Record>>map(selectConditionStep::orderBy)
                .orElse(selectConditionStep);
        SelectForUpdateStep<Record> selectForUpdateStep = GeneralJooqPagingServiceInterface.offsetLimit(pageNum, pageSize).<SelectForUpdateStep<Record>>map(t2 -> selectLimitStep.offset(t2.v1()).limit(t2.v2()))
                .orElse(selectLimitStep);
        List<E> entityList = selectForUpdateStep.fetch().into(entityClass());
        JooqUtils.joiningColumn(entityList, Optional.of(entity), entityClass(), mvcMapper().dsl());
        return entityList;
    }

    default Optional<List<OrderField<?>>> orderBy(List<String> sortBy, List<String> sortOrder0) {
        if (Objects.nonNull(sortBy)) {
            List<String> sortOrder = Objects.nonNull(sortOrder0) ? sortOrder0 : Collections.emptyList();
            int sortOrderSize = sortOrder.size();
            List<OrderField<?>> orderFieldList = IntStream.range(0, sortBy.size()).mapToObj(idx -> {
                String sb = sortBy.get(idx);
                String so = idx < sortOrderSize ? sortOrder.get(idx) : "asc";
                Field<?> field = mvcMapper().table().field(sb);
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
}
