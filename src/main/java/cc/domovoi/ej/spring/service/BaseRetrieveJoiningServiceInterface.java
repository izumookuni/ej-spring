package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseRetrieveMapperInterface;

import java.util.List;
import java.util.Map;

public interface BaseRetrieveJoiningServiceInterface<E extends BaseJoiningEntityInterface, M extends BaseRetrieveMapperInterface<E>> extends GeneralServiceInterface<M> {

    Map<String, BaseRetrieveJoiningServiceInterface> joiningService();

    /**
     * Depth of association.
     *
     * @return The default is 1.
     */
    default Integer depth() {
        return 1;
    }

    /**
     * Find entity function, called by the Controller layer.
     *
     * @param id ID of entity.
     * @return Entity.
     */
    default E findEntity(String id) {
        return findWithJoiningEntity(id, depth());
    }

    /**
     * Find entity list function, called by the Controller layer.
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    default List<E> findList(E entity) {
        return findListWithJoiningEntity(entity, depth());
    }

    /**
     * Find entity directly using mapper.
     * @param entity Query conditions.
     * @return Entity list.
     */
    default List<E> findListByMapper(E entity) {
        return mapper().findBaseList(entity);
    }

    /**
     * Find entity directly using mapper.
     * @param id ID of entity.
     * @return Entity.
     */
    default E findByMapper(String id) {
        if (id == null) {
            return null;
        }
        return mapper().findBaseById(id);
    }

    /**
     * Find entity by ID, and attached external entity.
     *
     * @param id ID of entity.
     * @param depth Depth of association.
     * @return Entity.
     */
    default E findWithJoiningEntity(String id, Integer depth) {
        E e = findByMapper(id);
        joinEntity(e, depth);
        return e;
    }

    /**
     * Find entity by ID, and attached external entity.
     *
     * @param entity Query conditions.
     * @param depth Depth of association.
     * @return Entity list.
     */
    default List<E> findListWithJoiningEntity(E entity, Integer depth) {
        List<E> eList = findListByMapper(entity);
        if (depth > 0) {
            eList.forEach(e -> joinEntity(e, depth));
        }
        return eList;
    }

    /**
     * Attached external entity.
     * @param entity Current entity
     * @param depth Depth of association.
     */
    default void joinEntity(E entity, Integer depth) {
        if (depth > 0) {
            entity.joiningKeyMap().forEach((key, idSupplier) -> {
                BaseRetrieveJoiningServiceInterface joiningService = joiningService().get(key);
                if (idSupplier.get() != null) {
                    idSupplier.get().forEach(joiningId -> {
                        BaseJoiningEntityInterface joiningEntity = joiningService.findWithJoiningEntity(joiningId, depth - 1);
                        entity.joiningEntityMap().get(key).accept(joiningEntity);
                    });
                }
            });
        }
    }
}
