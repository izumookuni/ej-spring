package cc.domovoi.spring.service.mvc;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.mapper.GeneralMapperInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public interface GeneralMvcJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>, M extends GeneralMapperInterface<K, E>> extends GeneralMvcRetrieveJoiningServiceInterface<K, E, M>, GeneralJoiningServiceInterface<K, E> {

    @Override
    default Boolean checkEntityExists(E entity) {
        if (Objects.isNull(entity.getId())) {
            return false;
        }
        E e = findEntityByMapper(entity.getId());
        return Objects.nonNull(e);
    }

    @Override
    default Try<Tuple2<Integer, K>> innerAddEntity(E entity) {
        try {
            Integer addResult = addEntityByMapper(entity);
            return new Success<>(new Tuple2<>(addResult, entity.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }

    @Override
    default Try<Integer> innerUpdateEntity(E entity) {
        return Try.apply(() -> updateEntityByMapper(entity));
    }

    @Override
    default Try<Integer> innerUpdateEntityForced(E entity) {
        return Try.apply(() -> updateEntityForcedByMapper(entity));
    }

    @Override
    default Try<Tuple2<Integer, E>> innerUpdateEntitySetNull(E entity, List<String> setNull) {
        return Try.apply(() -> updateEntitySetNullByMapper(entity, setNull));
    }

    @Override
    default Try<Integer> innerDeleteEntity(E entity) {
        return Try.apply(() -> deleteEntityByMapper(entity));
    }

    @Override
    default Try<Integer> innerDeleteEntityById(List<K> idList) {
        return Try.apply(() -> deleteEntityByIdByMapper(idList));
    }

    default Integer addEntityByMapper(E entity) {
//        if (Objects.isNull(entity.getId())) {
//            entity.setId(idGenerator());
//        }
//        if (Objects.isNull(entity.getAvailable())) {
//            entity.setAvailable(true);
//        }
//        LocalDateTime now = LocalDateTime.now();
//        entity.setCreationTime(now);
//        entity.setUpdateTime(now);
        return mvcMapper().add(entity);
    }

    default Integer updateEntityByMapper(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return mvcMapper().update(entity);
    }

    default Integer updateEntityForcedByMapper(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return mvcMapper().updateForced(entity);
    }

    default Tuple2<Integer, E> updateEntitySetNullByMapper(E entity, List<String> setNull) {
        entity.setUpdateTime(LocalDateTime.now());
        // FIXME: entityAfterUpdate
        return new Tuple2<>(mvcMapper().updateSetNull(entity, setNull), entity);
    }

    default Integer deleteEntityByMapper(E entity) {
        return mvcMapper().delete(entity);
    }

    default Integer deleteEntityByIdByMapper(List<K> idList) {
        return mvcMapper().deleteById(idList);
    }
}
