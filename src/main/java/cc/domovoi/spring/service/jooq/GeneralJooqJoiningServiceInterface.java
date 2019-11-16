package cc.domovoi.spring.service.jooq;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.utils.BeanMapUtils;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.tuple.Tuple2;

import java.time.LocalDateTime;
import java.util.Objects;

public interface GeneralJooqJoiningServiceInterface<R extends UpdatableRecord<R>, P, K, E extends GeneralJooqEntityInterface<P, K>> extends GeneralJooqRetrieveJoiningServiceInterface<R, P, K, E>, GeneralJoiningServiceInterface<K, E> {

    @Override
    default Boolean checkEntityExists(E entity) {
        if (Objects.isNull(entity.getId())) {
            return false;
        }
        E e = findEntityUsingIdByDao(entity.getId());
        return Objects.nonNull(e);
    }

    @Override
    default Try<Tuple2<Integer, K>> innerAddEntity(E entity) {
        try {
            Integer addResult = addEntityByDao(entity);
            return new Success<>(new Tuple2<>(addResult, entity.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }

    @Override
    default Try<Integer> innerUpdateEntity(E entity) {
        return Try.apply(() -> updateEntityByDao(entity));
    }

    @Override
    default Try<Integer> innerDeleteEntity(E entity) {
        return Try.apply(() -> deleteEntityByDao(entity));
    }

    default Integer addEntityByDao(E entity) {
//        if (Objects.isNull(entity.getId())) {
//            entity.setId(idGenerator());
//        }
//        if (Objects.isNull(entity.getAvailable())) {
//            entity.setAvailable(true);
//        }
//        LocalDateTime now = LocalDateTime.now();
//        entity.setCreationTime(now);
//        entity.setUpdateTime(now);
        return dsl().insertInto(getTable()).set(unMapper(entity.toPojo())).execute();
//        return unMapper(entity.toPojo()).insert();
    }

    default Integer updateEntityByDao(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        E e = findEntityUsingIdByDao(entity.getId());
        if (Objects.nonNull(e)) {
            P p = BeanMapUtils.copyPropertyIgnoreNull(e.toPojo(), entity.toPojo());
            return dsl().update(getTable()).set(unMapper(p)).where(initConditionUsingPojoIncludingField(p, "id")).execute();
        }
        else {
            return 0;
        }
//        return dsl().update(getTable()).set(unMapper(entity.toPojo())).execute();
//        return unMapper(entity.toPojo()).update();
    }

    default Integer deleteEntityByDao(E entity) {
        return dsl().deleteFrom(getTable()).where(initConditionUsingPojo(entity.toPojo())).execute();
//        return unMapper(entity.toPojo()).delete();
    }
}
