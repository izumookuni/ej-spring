package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.collection.tuple.Tuple2;
import cc.domovoi.ej.collection.util.Try;
import cc.domovoi.ej.spring.data.JoiningDepthLeaf;
import cc.domovoi.ej.spring.data.JoiningDepthTree;
import cc.domovoi.ej.spring.data.JoiningDepthTreeLike;
import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseRetrieveMapperInterface;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
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

    default JoiningDepthTreeLike depthTree() {
        return JoiningDepthTree.leaf;
    }

    /**
     * Find entity function, called by the Controller layer.
     *
     * @param id ID of entity.
     * @return Entity.
     */
    default E findEntity(String id) {
        return findWithJoiningEntity(id, depth(), depthTree());
    }

    /**
     * Find entity list function, called by the Controller layer.
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    default List<E> findList(E entity) {
        return findListWithJoiningEntity(entity, depth(), depthTree());
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
            int listSize = idList.size();
            if (listSize <= 500) {
                return mapper().findBaseListById(idList);
            }
            else {
                List<E> entityList = new ArrayList<>();
                for (int i = 0; i < listSize / 500; i++) {
                    List<String> innerIdList = idList.subList(i * 500, (i + 1) * 500);
                    List<E> innerEntityList = mapper().findBaseListById(innerIdList);
                    entityList.addAll(innerEntityList);
                }
                return entityList;
            }
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
     * @param tree JoiningDepthTree.
     * @return Entity.
     */
    default E findWithJoiningEntity(String id, Integer depth, JoiningDepthTreeLike tree) {
        E e = findByMapper(id);
        if (depth == -1) {
            joinEntityListByTree(Collections.singletonList(e), tree);
        }
        else {
            joiningEntityByDepth(Collections.singletonList(e), depth);
        }
        return e;
    }

    /**
     * Find entity by ID, and attached external entity.
     *
     * @param entity Query conditions.
     * @param depth  Depth of association.
     * @param tree JoiningDepthTree.
     * @return Entity list.
     */
    default List<E> findListWithJoiningEntity(E entity, Integer depth, JoiningDepthTreeLike tree) {
        List<E> eList = findListByMapper(entity);
        if (depth == -1) {
            joinEntityListByTree(eList, tree);
        }
        else {
            joiningEntityByDepth(eList, depth);
        }
        return eList;
    }

    @SuppressWarnings("unchecked")
    default void joiningEntityByDepth(List<E> entityList, Integer depth) {
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

    default List<E> findWithJoiningEntity(List<String> idList, Integer depth) {
        List<E> entityList = findListUsingIdByMapper(idList);
        joiningEntityByDepth(entityList, depth);
        return entityList;
    }

    @SuppressWarnings("unchecked")
    default void joinEntityListByTree(List<E> entityList, JoiningDepthTreeLike tree) {
        if (tree.isLeaf()) {
            return;
        }
        List<Tuple2<String, List<BaseJoiningEntityInterface>>> currentEntityMap = Collections.singletonList(new Tuple2<>("_root", entityList.stream().map(e -> (BaseJoiningEntityInterface) e).collect(Collectors.toList())));
        List<Tuple2<String, JoiningDepthTreeLike>> currentTreeMap = Collections.singletonList(new Tuple2<>("_root", tree)); // {_root -> {a -> Leaf, b -> Leaf, c -> {d -> Leaf, e -> Leaf}}}

        Map<String, Map<String, BaseRetrieveJoiningServiceInterface>> currentJoiningServiceMap = Collections.singletonMap("_root", joiningService());


        List<Tuple2<String, List<BaseJoiningEntityInterface>>> currentEntityMapBuffer = new ArrayList<>();
        List<Tuple2<String, JoiningDepthTreeLike>> currentTreeMapBuffer = new ArrayList<>();
        Map<String, Map<String, BaseRetrieveJoiningServiceInterface>> currentJoiningServiceMapBuffer = new HashMap<>();


        while (currentTreeMap.stream().map(Tuple2::_2).anyMatch(JoiningDepthTreeLike::isTree)) {
            // _root for first time.
            List<String> keyList = currentTreeMap.stream().map(Tuple2::_1).collect(Collectors.toList());
            for (int keyIdx = 0; keyIdx < keyList.size(); keyIdx++) {

                String key = keyList.get(keyIdx);
                String key2 = currentEntityMap.get(keyIdx)._1();
                assert key.equals(key2);
                List<BaseJoiningEntityInterface> currentEntityList = currentEntityMap.get(keyIdx)._2();
                JoiningDepthTreeLike currentTree = currentTreeMap.get(keyIdx)._2(); // tree
                Map<String, BaseRetrieveJoiningServiceInterface> currentJoiningService = currentJoiningServiceMap.get(key);

                List<BaseJoiningEntityInterface> joiningEntityListBuffer = Collections.emptyList();

                if (currentTree.isTree()) {

                    for (String subKey : currentTree.keySet()) {
                        // a, b, c for first time
                        Optional<JoiningDepthTreeLike> subTree = currentTree.subTree(subKey);
                        BaseRetrieveJoiningServiceInterface joiningService = currentJoiningService.get(subKey);
                        if (currentEntityList != null && !currentEntityList.isEmpty()) {

                            List<String> idList = currentEntityList.stream().flatMap(e -> {
                                List<String> innerKeyList = e.joiningKeyMap().get(subKey).get();
                                return innerKeyList != null ? innerKeyList.stream() : Stream.empty();
                            }).collect(Collectors.toList());

                            List<BaseJoiningEntityInterface> joiningEntityList = joiningService.findListUsingIdByMapper(idList);
                            currentEntityList.forEach(e -> {

                                List<String> innerKeyList = e.joiningKeyMap().get(subKey).get();
                                if (innerKeyList != null) {

                                    List<BaseJoiningEntityInterface> innerJoiningEntityList = joiningEntityList.stream().filter(je -> innerKeyList.contains(je.getId())).collect(Collectors.toList());
                                    innerJoiningEntityList.forEach(e.joiningEntityMap().get(subKey)::accept);
                                }
                            });
                            joiningEntityListBuffer = joiningEntityList;
                        }


                        if (subTree.map(JoiningDepthTreeLike::isTree).orElseGet(() -> false)) {

                            currentTreeMapBuffer.add(new Tuple2<>(subKey, subTree.get()));

                            currentEntityMapBuffer.add(new Tuple2<>(subKey, joiningEntityListBuffer));

                            currentJoiningServiceMapBuffer.putIfAbsent(subKey, joiningService.joiningService());
                        }

                    }
                }
            }


            currentTreeMap = new ArrayList<>(currentTreeMapBuffer);
            currentEntityMap = new ArrayList<>(currentEntityMapBuffer);
            currentJoiningServiceMap = new HashMap<>(currentJoiningServiceMapBuffer);
//            currentTreeMap = currentTreeMapBuffer;
//            currentEntityMap = currentEntityMapBuffer;
//            currentJoiningServiceMap = currentJoiningServiceMapBuffer;

            currentTreeMapBuffer.clear();
            currentEntityMapBuffer.clear();
            currentJoiningServiceMapBuffer.clear();
        }
    }
}
