package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseRetrieveMapperInterface;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * BaseRetrieveServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
public interface BaseRetrieveServiceInterface<E extends BaseEntityInterface, M extends BaseRetrieveMapperInterface<E>> extends BaseRetrieveJoiningServiceInterface<E, M> {

    @Override
    default Integer depth() {
        return 0;
    }

    @Override
    default Map<String, BaseRetrieveJoiningServiceInterface> joiningService() {
        return Collections.emptyMap();
    }

    @Override
    default E findEntity(String id) {
        return findByMapper(id);
    }

    @Override
    default List<E> findList(E entity) {
        return findListByMapper(entity);
    }
}
