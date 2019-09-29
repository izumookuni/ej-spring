package cc.domovoi.spring.service;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * BaseJoiningServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
@Deprecated
public interface BaseJoiningServiceInterface<E extends StandardJoiningEntityInterface, M extends StandardMapperInterface<E>> extends BaseRetrieveJoiningServiceInterface<E, M> {

    default String idGenerator() {
        return UUID.randomUUID().toString();
    }

    default void beforeAdd(E entity) { }

    default void beforeUpdate(E entity) { }

    default void beforeDelete(E entity) { }

    default Boolean addCondition(E entity) {
        return true;
    }

    default Boolean updateCondition(E entity) {
        return true;
    }

    default Boolean deleteCondition(E entity) {
        return Objects.nonNull(entity) && StringUtils.hasText(entity.getId());
    }

    default void afterAdd(E entity) { }

    default void afterUpdate(E entity) { }

    default void afterDelete(E entity) { }

    /**
     * Add entity function, called by the Controller layer.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    default Try<Tuple2<Integer, String>> addEntity(E entity) {
        if (!addCondition(entity)) {
            return new Failure<>(new RuntimeException("do not meet the addCondition"));
        }
        beforeAdd(entity);
        Try<Tuple2<Integer, String>> addResult = Try.apply(() -> {
            Boolean entityExist = checkEntityExist(entity);
            if (entityExist) {
                return new Tuple2<>(0, null);
            }
            Integer result = addEntityByMapper(entity);
            return new Tuple2<>(result, entity.getId());
        });
        afterAdd(entity);
        return addResult;
//        Boolean entityExist = checkEntityExists(entity);
//        return entityExist ? 0 : addEntityByMapper(entity);
    }

    /**
     * Update entity function, called by the Controller layer.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    default Try<Integer> updateEntity(E entity) {
        if (!updateCondition(entity)) {
            return new Failure<>(new RuntimeException("do not meet the updateCondition"));
        }
        beforeUpdate(entity);
        Try<Integer> updateResult = Try.apply(() -> updateEntityByMapper(entity));
        afterUpdate(entity);
        return updateResult;
//        return updateEntityByMapper(entity);
    }

    /**
     * Delete entity function, called by the Controller layer.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    default Try<Integer> deleteEntity(E entity) {
        if (!deleteCondition(entity)) {
            return new Failure<>(new RuntimeException("do not meet the deleteCondition"));
        }
        beforeDelete(entity);
//        if (entity.getId() == null) {
//            throw new RuntimeException("id must not be null");
//        }
        Try<Integer> deleteResult = Try.apply(() -> deleteEntityByMapper(entity));
        afterDelete(entity);
        return deleteResult;
//        return deleteEntityByMapper(entity);
    }

    /**
     * Check whether an Entity with the same ID exists.
     * <p>
     * When the ID is null, a random value will be added to it.
     *
     * @param entity The entity need to be added.
     * @return Whether an Entity with the same ID exists.
     */
    @SuppressWarnings("unchecked")
    default Boolean checkEntityExist(E entity) {
        // Repeated additions are not allowed. Update operations should be used
        try {
            if (entity.getId() == null || "".equals(entity.getId())) {
                entity.setId(idGenerator());
                return false;
            }
            E query = (E) entity.getClass().newInstance();
            query.setId(entity.getId());
            List<E> eList = findListByMapper(query);
            return !eList.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Add entity directly using mapper.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    default Integer addEntityByMapper(E entity) {
        if (entity.getId() == null || "".equals(entity.getId())) {
            entity.setId(idGenerator());
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreationTime(now);
        entity.setUpdateTime(now);
        return mapper().addBase(entity);
    }

    /**
     * Update entity directly using mapper.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    default Integer updateEntityByMapper(E entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return mapper().updateBase(entity);
    }

    /**
     * Delete entity directly using mapper.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    default Integer deleteEntityByMapper(E entity) {
        return mapper().deleteBase(entity);
    }
}
