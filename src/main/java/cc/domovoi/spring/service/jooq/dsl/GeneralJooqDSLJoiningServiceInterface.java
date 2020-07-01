package cc.domovoi.spring.service.jooq.dsl;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.jooq.GeneralJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.jooq.GeneralJooqJoiningMapperInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;

public interface GeneralJooqDSLJoiningServiceInterface<K, E extends GeneralJooqDSLEntityInterface<K, ?>, M extends GeneralJooqJoiningMapperInterface<?, K, E>> extends GeneralJooqDSLRetrieveJoiningServiceInterface<K, E, M>, GeneralJoiningServiceInterface<K, E> {

    @Override
    default Boolean checkEntityExists(E entity) {
        return mvcMapper().checkEntityExists(entity);
    }

    @Override
    default Try<Tuple2<Integer, K>> innerAddEntity(E entity) {
        try {
            Integer addResult = mvcMapper().addBase(entity);
            return new Success<>(new Tuple2<>(addResult, entity.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }

    @Override
    default Try<Integer> innerUpdateEntity(E entity) {
        return Try.apply(() -> mvcMapper().updateBase(entity));
    }

    @Override
    default Try<Integer> innerUpdateEntityForced(E entity) {
        return Try.apply(() -> mvcMapper().updateBaseForced(entity));
    }

    @Override
    default Try<Tuple2<Integer, E>> innerUpdateEntitySetNull(E entity, List<String> setNull) {
        return Try.apply(() -> new Tuple2<>(mvcMapper().updateBaseSetNull(entity, setNull), entity));
    }

    @Override
    default Try<Integer> innerDeleteEntity(E entity) {
        return Try.apply(() -> mvcMapper().deleteBase(entity));
    }

    @Override
    default Class<E> entityClass() {
        return mvcMapper().entityClass();
    }
}
