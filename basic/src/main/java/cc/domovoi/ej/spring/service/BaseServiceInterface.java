package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseMapperInterface;

import java.util.List;

/**
 * BaseServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
public interface BaseServiceInterface<E extends BaseEntityInterface, M extends BaseMapperInterface<E>> extends BaseRetrieveServiceInterface<E, M>, BaseJoiningServiceInterface<E, M> {

    @Override
    default E findEntity(String id) {
        return findByMapper(id);
    }

    @Override
    default List<E> findList(E entity) {
        return findListByMapper(entity);
    }
}
