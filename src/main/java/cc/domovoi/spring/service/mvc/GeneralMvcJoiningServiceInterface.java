package cc.domovoi.spring.service.mvc;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.mapper.GeneralMapperInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

import java.time.LocalDateTime;
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
    default Try<Integer> innerDeleteEntity(E entity) {
        return Try.apply(() -> deleteEntityByMapper(entity));
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
        return mvcMapper().addBase(entity);
    }

    default Integer updateEntityByMapper(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return mvcMapper().updateBase(entity);
    }

    default Integer deleteEntityByMapper(E entity) {
        return mvcMapper().deleteBase(entity);
    }
}
