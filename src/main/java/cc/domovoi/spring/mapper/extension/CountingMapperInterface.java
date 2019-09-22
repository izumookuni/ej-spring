package cc.domovoi.spring.mapper.extension;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;

/**
 * BasePagingMapperInterface
 *
 * @param <E> Entity type
 */
public interface CountingMapperInterface<E extends GeneralJoiningEntityInterface<?>> {

    /**
     * Find Amount of entities that have certain attributes.
     *
     * @param entity Query conditions.
     * @return Amount of entities.
     */
    Integer findCount(E entity);
}
