package cc.domovoi.spring.mapper;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;

public interface GeneralMapperInterface<K, E extends GeneralJoiningEntityInterface<K>> extends GeneralRetrieveMapperInterface<K, E> {

    /**
     * Add entity.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    Integer addBase(E entity);

    /**
     * Update entity.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    Integer updateBase(E entity);

    /**
     * Delete entity.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    Integer deleteBase(E entity);
}
