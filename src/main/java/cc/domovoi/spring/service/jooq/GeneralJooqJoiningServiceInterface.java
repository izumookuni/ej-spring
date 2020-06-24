package cc.domovoi.spring.service.jooq;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.utils.BeanMapUtils;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
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
    default Try<Integer> innerUpdateEntityForced(E entity) {
        return Try.apply(() -> updateEntityForcedByDao(entity));
    }

    @Override
    default Try<Tuple2<Integer, E>> innerUpdateEntitySetNull(E entity, List<String> setNull) {
        return Try.apply(() -> updateEntitySetNullByDao(entity, setNull));
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

    default Integer updatePojoByDao(P pojo) {
        return dsl().update(getTable()).set(unMapper(pojo)).where(initConditionUsingPojoIncludingField(pojo, "id")).execute();
    }

    default Integer updateEntityByDao(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        E e = findEntityUsingIdByDao(entity.getId());
        if (Objects.nonNull(e)) {
            P p = BeanMapUtils.copyPropertyIgnoreNull(e.toPojo(), entity.toPojo());
            return updatePojoByDao(p);
        }
        else {
            return 0;
        }
//        return dsl().update(getTable()).set(unMapper(entity.toPojo())).execute();
//        return unMapper(entity.toPojo()).update();
    }

    default Integer updateEntityForcedByDao(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        P p = entity.toPojo();
        return updatePojoByDao(p);
    }

    default Tuple2<Integer, E> updateEntitySetNullByDao(E entity, List<String> setNull) {
        entity.setUpdateTime(LocalDateTime.now());
        E e = findEntityUsingIdByDao(entity.getId());
        if (Objects.nonNull(e)) {
            P p = BeanMapUtils.copyPropertyIgnoreNull(e.toPojo(), entity.toPojo());
            Reflect r = Reflect.on(p);
            setNull.forEach(s -> r.set(s, null));
            Integer result = updatePojoByDao(p);
            E e1 = Reflect.onClass(entityClass()).create().get();
            BeanUtils.copyProperties(p, e1);
            return new Tuple2<>(result, e1);
        }
        else {
            return new Tuple2<>(0, null);
        }
    }

    default Integer deleteEntityByDao(E entity) {
        return dsl().deleteFrom(getTable()).where(initConditionUsingEntity(entity)).execute();
//        return unMapper(entity.toPojo()).delete();
    }
}
