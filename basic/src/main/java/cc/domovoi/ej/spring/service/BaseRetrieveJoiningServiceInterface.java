package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseRetrieveMapperInterface;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * BaseRetrieveJoiningServiceInterface.
 *
 * @param <E> Entity type.
 * @param <M> Mapper type.
 */
public interface BaseRetrieveJoiningServiceInterface<E extends BaseJoiningEntityInterface, M extends BaseRetrieveMapperInterface<E>> extends GeneralServiceInterface<M> {

    /**
     * Service of each joining entity. Entities of different types are distinguished using Map key.
     *
     * @return Service of each joining entity.
     */
    Map<String, BaseRetrieveJoiningServiceInterface> joiningService();

    /**
     * Depth of association.
     *
     * @return The default is 1.
     */
    default Integer depth() {
        return 1;
    }

    default Map<String, Integer> depthMap() {
        return Collections.emptyMap();
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
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    default List<E> findListByMapper(E entity) {
        return mapper().findBaseList(entity);
    }

    /**
     * Find entity directly using mapper.
     *
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
     * Find entity directly using mapper.
     *
     * @param idList ID list of entity list.
     * @return Entity list.
     */
    default List<E> findListUsingIdByMapper(List<String> idList) {
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return mapper().findBaseListById(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return idList.stream().map(mapper()::findBaseById).collect(Collectors.toList());
        }

    }

    /**
     * Find entity by ID, and attached external entity.
     *
     * @param id    ID of entity.
     * @param depth Depth of association.
     * @return Entity.
     */
    default E findWithJoiningEntity(String id, Integer depth) {
        E e = findByMapper(id);
//        joinEntity(e, depth);
        joinEntityList(Collections.singletonList(e), depth);
        return e;
    }

    default List<E> findWithJoiningEntity(List<String> idList, Integer depth) {
        List<E> entityList = findListUsingIdByMapper(idList);
        joinEntityList(entityList, depth);
        return entityList;
    }

    /**
     * Find entity by ID, and attached external entity.
     *
     * @param entity Query conditions.
     * @param depth  Depth of association.
     * @return Entity list.
     */
    default List<E> findListWithJoiningEntity(E entity, Integer depth) {
        List<E> eList = findListByMapper(entity);
//        if (depth > 0) {
//            eList.forEach(e -> joinEntity(e, depth));
//        }
        joinEntityList(eList, depth);
        return eList;
    }

//    /**
//     * Attached external entity.
//     *
//     * @param entity Current entity.
//     * @param depth  Depth of association.
//     */
//    @Deprecated
//    default void joinEntity(E entity, Integer depth) {
//        if (depth > 0) {
//            entity.joiningKeyMap().forEach((key, idSupplier) -> {
//                BaseRetrieveJoiningServiceInterface joiningService = joiningService().get(key);
//                if (idSupplier.get() != null) {
//                    idSupplier.get().forEach(joiningId -> {
//                        BaseJoiningEntityInterface joiningEntity = joiningService.findWithJoiningEntity(joiningId, depth - 1);
//                        entity.joiningEntityMap().get(key).accept(joiningEntity);
//                    });
//                }
//            });
//        }
//    }

    @SuppressWarnings("unchecked")
    default void joinEntityList(List<E> entityList, Integer depth) {
        if (depth > 0) {
            if (entityList != null && !entityList.isEmpty()) {
                entityList.get(0).joiningKeyMap().keySet().forEach(key -> {
                    BaseRetrieveJoiningServiceInterface joiningService = joiningService().get(key);
                    List<String> idList = entityList.stream().flatMap(e -> {
                        List<String> keyList = e.joiningKeyMap().get(key).get();
                        return keyList != null ? keyList.stream() : Stream.empty();
                    }).collect(Collectors.toList());
                    List<BaseJoiningEntityInterface> joiningEntityList = joiningService.findWithJoiningEntity(idList, depth - 1);
                    entityList.forEach(e -> {
                        List<String> innerKeyList = e.joiningKeyMap().get(key).get();
                        List<BaseJoiningEntityInterface> innerJoiningEntityList = joiningEntityList.stream().filter(je -> innerKeyList.contains(je.getId())).collect(Collectors.toList());
                        innerJoiningEntityList.forEach(e.joiningEntityMap().get(key)::accept);
                    });
                });
            }
        }
    }
}
