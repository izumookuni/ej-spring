package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;

import java.util.List;

/**
 * BaseServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
@Deprecated
public interface BaseServiceInterface<E extends StandardSingletonEntityInterface, M extends StandardMapperInterface<E>> extends BaseRetrieveServiceInterface<E, M>, BaseJoiningServiceInterface<E, M> {

    @Override
    default E findEntity(String id) {
        return findByMapper(id);
    }

    @Override
    default List<E> findList(E entity) {
        return findListByMapper(entity);
    }
}
