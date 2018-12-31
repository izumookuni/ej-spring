package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BasePagingEntityInterface;
import cc.domovoi.ej.spring.mapper.BasePagingMapperInterface;

/**
 * BasePagingServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
public interface BasePagingServiceInterface<E extends BasePagingEntityInterface, M extends BasePagingMapperInterface<E>> extends GeneralServiceInterface<M> {

    /**
     * Find Amount of entities that have certain attributes, called by the Controller layer.
     *
     * @param entity Query conditions.
     * @return Amount of entities.
     */
    default Integer findCount(E entity) {
        return findCountByMapper(entity);
    }

    /**
     * Find Amount of entities that have certain attributes directly using mapper.
     *
     * @param entity Query conditions.
     * @return Amount of entities.
     */
    default Integer findCountByMapper(E entity) {
        return mapper().findCount(entity);
    }
}
