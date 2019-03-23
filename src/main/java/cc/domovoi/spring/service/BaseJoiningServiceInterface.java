package cc.domovoi.spring.service;

import cc.domovoi.ej.collection.tuple.Tuple2;
import cc.domovoi.ej.collection.util.Try;
import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.mapper.BaseMapperInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * BaseJoiningServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
public interface BaseJoiningServiceInterface<E extends BaseJoiningEntityInterface, M extends BaseMapperInterface<E>> extends BaseRetrieveJoiningServiceInterface<E, M> {

    default String idGenerator() {
        return UUID.randomUUID().toString();
    }

    default void beforeAdd(E entity) {

    }

    default void beforeUpdate(E entity) {

    }

    default void beforeDelete(E entity) {

    }

    default Boolean addCondition(E entity) {
        return true;
    }

    default Boolean updateCondition(E entity) {
        return true;
    }

    default Boolean deleteCondition(E entity) {
        return true;
    }

    default void afterAdd(E entity) {

    }

    default void afterUpdate(E entity) {

    }

    default void afterDelete(E entity) {

    }

    /**
     * Add entity function, called by the Controller layer.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    default Try<Tuple2<Integer, String>> addEntity(E entity) {
        beforeAdd(entity);
        return Try.apply(() -> {
            Boolean entityExist = checkEntityExist(entity);
            if (entityExist) {
                return new Tuple2<>(0, null);
            }
            Integer addResult = addEntityByMapper(entity);
            return new Tuple2<>(addResult, entity.getId());
        });
//        Boolean entityExist = checkEntityExist(entity);
//        return entityExist ? 0 : addEntityByMapper(entity);
    }

    /**
     * Update entity function, called by the Controller layer.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    default Try<Integer> updateEntity(E entity) {
        beforeUpdate(entity);
        return Try.apply(() -> updateEntityByMapper(entity));
//        return updateEntityByMapper(entity);
    }

    /**
     * Delete entity function, called by the Controller layer.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    default Try<Integer> deleteEntity(E entity) {
        beforeDelete(entity);
        if (entity.getId() == null) {
            throw new RuntimeException("id must not be null");
        }
        return Try.apply(() -> deleteEntityByMapper(entity));
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
