package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;
import cc.domovoi.spring.mapper.StandardRetrieveMapperInterface;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * BaseRetrieveServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
@Deprecated
public interface BaseRetrieveServiceInterface<E extends StandardSingletonEntityInterface, M extends StandardRetrieveMapperInterface<E>> extends BaseRetrieveJoiningServiceInterface<E, M> {

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
