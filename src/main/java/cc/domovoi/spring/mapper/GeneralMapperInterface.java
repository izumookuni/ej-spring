package cc.domovoi.spring.mapper;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;

import java.util.List;

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
     * Update entity Forced.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    Integer updateBaseForced(E entity);

    /**
     * Update entity, field in setNull list will be set null.
     *
     * @param entity  The entity need to be updated.
     * @param setNull A list that field will be set null.
     * @return The number of successful update operations.
     */
    Integer updateBaseSetNull(E entity, List<String> setNull);

    /**
     * Delete entity.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    Integer deleteBase(E entity);
}
