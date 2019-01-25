package cc.domovoi.ej.spring.mapper;

import cc.domovoi.ej.spring.entity.GeneralJoiningEntityInterface;

import java.util.List;
import java.util.stream.Collectors;

public interface GeneralRetrieveMapperInterface<K, E extends GeneralJoiningEntityInterface<K, ?>> {

    /**
     * Find entity.
     *
     * @param id ID of entity.
     * @return Entity.
     */
    E findBaseById(K id);

    /**
     * Find entity list by id list.
     *
     * @param idList Id list of entity list;
     * @return Entity list.
     */
    List<E> findBaseListById(List<K> idList);

    /**
     * Find entity.
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    List<E> findBaseList(E entity);
}
