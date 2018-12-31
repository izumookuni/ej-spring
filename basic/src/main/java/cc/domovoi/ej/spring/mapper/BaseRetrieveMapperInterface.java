package cc.domovoi.ej.spring.mapper;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;

import java.util.List;

/**
 * BaseRetrieveMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface BaseRetrieveMapperInterface<E extends BaseJoiningEntityInterface> {

    /**
     * Find entity.
     *
     * @param id ID of entity.
     * @return Entity.
     */
    E findBaseById(String id);

    /**
     * Find entity.
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    List<E> findBaseList(E entity);
}
