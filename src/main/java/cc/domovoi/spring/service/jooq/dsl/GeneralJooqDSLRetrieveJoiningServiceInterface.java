package cc.domovoi.spring.service.jooq.dsl;

import cc.domovoi.spring.entity.jooq.GeneralJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.jooq.GeneralJooqRetrieveJoiningMapperInterface;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;
import cc.domovoi.spring.service.mvc.MvcServiceInterface;

import java.util.List;
import java.util.Map;

public interface GeneralJooqDSLRetrieveJoiningServiceInterface<K, E extends GeneralJooqDSLEntityInterface<K, ?>, M extends GeneralJooqRetrieveJoiningMapperInterface<?, K, E>> extends GeneralRetrieveJoiningServiceInterface<K, E>, MvcServiceInterface<M> {

    @Override
    default Class<K> keyClass() {
        return mvcMapper().keyClass();
    }

    @Override
    default E innerFindEntity(K id) {
        return mvcMapper().findById(id);
    }

    @Override
    default List<E> innerFindList(E entity) {
        return mvcMapper().findList(entity);
    }

    @Override
    default List<E> innerFindListById(List<K> idList) {
        return mvcMapper().findListById(idList);
    }

    @Override
    default List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass, Map<String, Object> params, String name) {
        List<E> eList = mvcMapper().findListByKey(keyList, context, entityClass, name);
        doAfterFindList(0, name, eList, params);
        return eList;
    }
}
