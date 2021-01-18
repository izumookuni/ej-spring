package cc.domovoi.spring.mapper;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;

import java.util.List;

public interface GeneralRetrieveMapperInterface<K, E extends GeneralJoiningEntityInterface<K>> {

    /**
     * Find entity.
     *
     * @param id ID of entity.
     * @return Entity.
     */
    E findById(K id);

    /**
     * Find entity list by id list.
     *
     * @param idList Id list of entity list;
     * @return Entity list.
     */
    List<E> findListById(List<K> idList);

    /**
     * Find entity.
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    List<E> findList(E entity);
}
