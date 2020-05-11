package cc.domovoi.spring.service.extension;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.mapper.extension.CountingMapperInterface;
import cc.domovoi.spring.service.GeneralServiceInterface;

/**
 * BasePagingServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
@Deprecated
public interface CountingServiceInterface<E extends GeneralJoiningEntityInterface<?>, M extends CountingMapperInterface<E>> extends GeneralServiceInterface<M> {

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
        return mvcMapper().findCount(entity);
    }
}
