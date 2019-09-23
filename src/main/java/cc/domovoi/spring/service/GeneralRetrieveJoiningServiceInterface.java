package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface GeneralRetrieveJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends OriginalServiceInterface {

    Map<String, GeneralRetrieveJoiningServiceInterface> joiningService();

    E innerFindEntity(K id);

    List<E> innerFindList(E entity);

    default E innerFindEntity(E entity) {
        List<E> eList = innerFindList(entity);
        int eListSize = eList.size();
        if (eListSize == 0) {
            return null;
        }
        else if (eListSize == 1) {
            return eList.get(0);
        }
        else {
            throw new RuntimeException("too many entity");
        }
    }

    default void beforeFindEntity(E entity) {
    }

    default void afterFindEntity(E entity) {
    }

    default void beforeFindList(List<E> entity) {
        entity.forEach(this::beforeFindEntity);
    }

    default void afterFindList(List<E> entity) {
        entity.forEach(this::afterFindEntity);
    }

    default Optional<String> findCondition(E entity) {
        return Optional.empty();
    }

    default Optional<String> collectCondition(E entity) {
        return Optional.empty();
    }

    default JoiningDepthTreeLike depthTree() {
        return JoiningDepthTree.leaf;
    }

    default E findEntity(K id) {
        return findEntity(id, depthTree());
    }

    default E findEntity(E entity) {
        return findEntity(entity, depthTree());
    }

    default List<E> findList(E entity) {
        return findList(entity, depthTree());
    }

    default E findEntity(K id, JoiningDepthTreeLike depthTree) {
        // no findCondition
        // no before find
        // find entity
        E e = innerFindEntity(id);
        // after find
        if (Objects.nonNull(e)) {
            afterFindEntity(e);
            joinEntityListByTree(Collections.singletonList(e), depthTree);
        }
        Optional<String> collectConditionResult = collectCondition(e);
        if (collectConditionResult.isPresent()) {
            throw new RuntimeException(collectConditionResult.get());
        }
        else {
            return e;
        }
    }

    default E findEntity(E entity, JoiningDepthTreeLike depthTree) {
        // findCondition
        Optional<String> findConditionResult = findCondition(entity);
        if (findConditionResult.isPresent()) {
            throw new RuntimeException(findConditionResult.get());
        }
        // before find
        if (Objects.nonNull(entity)) {
            beforeFindEntity(entity);
        }
        E e = innerFindEntity(entity);
        // after find
        if (Objects.nonNull(e)) {
            afterFindEntity(e);
            joinEntityListByTree(Collections.singletonList(e), depthTree);
        }
        Optional<String> collectConditionResult = collectCondition(e);
        if (collectConditionResult.isPresent()) {
            throw new RuntimeException(collectConditionResult.get());
        }
        else {
            return e;
        }
    }

    default List<E> findList(E entity, JoiningDepthTreeLike depthTree) {
        // findCondition
        Optional<String> findConditionResult = findCondition(entity);
        if (findConditionResult.isPresent()) {
            throw new RuntimeException(findConditionResult.get());
        }
        // before find
        if (Objects.nonNull(entity)) {
            beforeFindEntity(entity);
        }

//        List<E> eList = innerFindList(entity);
//        eList.forEach(this::afterFindEntity);
//        joinEntityListByTree(eList, depthTree);
//        return eList.stream().filter(e -> !collectCondition(e).isPresent()).collect(Collectors.toList());

        List<E> eList = innerFindList(entity).stream().peek(this::afterFindEntity).filter(e -> !collectCondition(e).isPresent()).collect(Collectors.toList());
        joinEntityListByTree(eList, depthTree);
        return eList;
    }

    List<E> findListByKey(List<Object> keyList, String context);

//    @SuppressWarnings("unchecked")
    default void joinEntityListByTree(List<E> entityList, JoiningDepthTreeLike tree) {
        // When depthTree is the default (leaf), return directly
        if (tree.isLeaf()) {
            return;
        }

        // Initialize the loop variable
        Map<String, List<GeneralJoiningEntityInterface>> currentEntityMap = Collections.singletonMap("_root", entityList.stream().map(e -> (GeneralJoiningEntityInterface) e).collect(Collectors.toList()));
        Map<String, JoiningDepthTreeLike> currentTreeMap = Collections.singletonMap("_root", tree); // {_root -> {a -> Leaf, b -> Leaf, c -> {d -> Leaf, e -> uTree}}}
        Map<String, Map<String, GeneralRetrieveJoiningServiceInterface>> currentJoiningServiceMap = Collections.singletonMap("_root", joiningService());

        // Cache of current processing results
        Map<String, List<GeneralJoiningEntityInterface>> currentEntityMapBuffer = new HashMap<>();
        Map<String, JoiningDepthTreeLike> currentTreeMapBuffer = new HashMap<>();
        Map<String, Map<String, GeneralRetrieveJoiningServiceInterface>> currentJoiningServiceMapBuffer = new HashMap<>();

        while (!currentEntityMap.isEmpty() && !currentTreeMap.isEmpty()) {
            Set<String> treeKeySet = currentTreeMap.keySet();
            // _root for first time.
            for (String treeKey : treeKeySet) {
                // current variable
                List<GeneralJoiningEntityInterface> currentEntityList = currentEntityMap.get(treeKey);
                JoiningDepthTreeLike currentTree = currentTreeMap.get(treeKey); // {a -> Leaf, b -> Leaf, c -> {d -> Leaf, e -> uTree}}
                Map<String, GeneralRetrieveJoiningServiceInterface> currentJoiningService = currentJoiningServiceMap.get(treeKey);

                List<GeneralJoiningEntityInterface> joiningEntityListBuffer = Collections.emptyList();

                if (Objects.isNull(currentEntityList) || currentEntityList.isEmpty()) {
                    continue;
                }

                Set<String> joiningKeySet = currentEntityList.get(0).joiningEntityMap().keySet();
                for (String subKey : joiningKeySet) {
                    GeneralRetrieveJoiningServiceInterface joiningService = currentJoiningService.get(subKey);
                    List<Object> keyList = currentEntityList.stream().flatMap(e -> {
                        Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap = e.joiningKeyMap();
                        List<Object> innerKeyList = innerJoiningKeyMap.get(subKey).get();
                        return innerKeyList != null ? innerKeyList.stream() : Stream.empty();
                    }).collect(Collectors.toList());

                    List<GeneralJoiningEntityInterface> joiningEntityList = (List<GeneralJoiningEntityInterface>) findListByKey(keyList, subKey);

                }


            }
        }
    }
}
