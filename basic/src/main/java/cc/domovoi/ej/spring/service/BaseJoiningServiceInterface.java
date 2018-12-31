package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseMapperInterface;

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

    /**
     * Add entity function, called by the Controller layer.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    default Integer addEntity(E entity) {
        // TODO: 将返回类型改为Try<Tuple2<Integer, String>>
        Boolean entityExist = checkEntityExist(entity);
        return entityExist ? 0 : addEntityByMapper(entity);
    }

    /**
     * Update entity function, called by the Controller layer.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    default Integer updateEntity(E entity) {
        // TODO: 将返回类型改为Try<Integer>
        return updateEntityByMapper(entity);
    }

    /**
     * Delete entity function, called by the Controller layer.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    default Integer deleteEntity(E entity) {
        // TODO: 将返回类型改为Try<Integer>
        return deleteEntityByMapper(entity);
    }

    /**
     * Check whether an Entity with the same ID exists.
     * <p>
     * When the ID is null, a random value will be added to it.
     *
     * @param entity The entity need to be added.
     * @return Whether an Entity with the same ID exists.
     */
    default Boolean checkEntityExist(E entity) {
        // Repeated additions are not allowed. Update operations should be used
        try {
            if (entity.getId() == null || "".equals(entity.getId())) {
                entity.setId(UUID.randomUUID().toString());
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
            entity.setId(UUID.randomUUID().toString());
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
