package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.utils.joiningdepthtree.DepthTreeType;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;

public interface GeneralRetrieveJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends OriginalServiceInterface {

    Class<E> entityClass();

    Map<String, GeneralRetrieveJoiningServiceInterface> joiningService();

    E innerFindEntity(K id);

    List<E> innerFindList(E entity);

    List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass);

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

    default <T> void processBeforeFindResult(T value, String scope) {
    }

    default void processAfterFindResult(List<E> entity) {
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
        // before find
        processBeforeFindResult(id, "id");
        // find entity
        E e = innerFindEntity(id);
        // after find
        if (Objects.nonNull(e)) {
            processAfterFindResult(Collections.singletonList(e));
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
            processBeforeFindResult(entity, "entity");
        }
        E e = innerFindEntity(entity);
        // after find
        if (Objects.nonNull(e)) {
            processAfterFindResult(Collections.singletonList(e));
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
            processBeforeFindResult(entity, "entity");
        }

//        List<E> eList = innerFindList(entity);
//        eList.forEach(this::afterFindEntity);
//        joinEntityListByTree(eList, depthTree);
//        return eList.stream().filter(e -> !collectCondition(e).isPresent()).collect(Collectors.toList());

        List<E> eList0 = innerFindList(entity);
        processAfterFindResult(eList0);
        List<E> eList = eList0.stream().peek(this::afterFindEntity).filter(e -> !collectCondition(e).isPresent()).collect(Collectors.toList());

        joinEntityListByTree(eList, depthTree);
        return eList;
    }

//    @SuppressWarnings("unchecked")
    default void joinEntityListByTree(List<E> entityList, JoiningDepthTreeLike tree) {
        // When depthTree is the default (leaf), return directly
        logger().debug("tree: " + tree.treeString());
        if (tree.isLeaf()) {
            logger().debug("tree is Leaf");
            return;
        }

        // Initialize the loop variable
        Map<String, List<GeneralJoiningEntityInterface>> currentEntityMap = Collections.singletonMap("_root", entityList.stream().map(e -> (GeneralJoiningEntityInterface) e).collect(Collectors.toList()));
        Map<String, JoiningDepthTreeLike> currentTreeMap = Collections.singletonMap("_root", tree); // {_root -> {a -> Leaf, b -> Leaf, c -> {d -> Leaf, e -> uTree}}}
        Map<String, Tuple2<GeneralRetrieveJoiningServiceInterface, Map<String, GeneralRetrieveJoiningServiceInterface>>> currentJoiningServiceMap = Collections.singletonMap("_root", new Tuple2<>(this, joiningService()));

        // Cache of current processing results
        Map<String, List<GeneralJoiningEntityInterface>> currentEntityMapBuffer = new HashMap<>();
        Map<String, JoiningDepthTreeLike> currentTreeMapBuffer = new HashMap<>();
        Map<String, Tuple2<GeneralRetrieveJoiningServiceInterface, Map<String, GeneralRetrieveJoiningServiceInterface>>> currentJoiningServiceMapBuffer = new HashMap<>();

        while (!currentEntityMap.isEmpty() && !currentTreeMap.isEmpty()) {
            Set<String> treeKeySet = currentTreeMap.keySet();
            // _root for first time.
            for (String treeKey : treeKeySet) {
                // current variable
                List<GeneralJoiningEntityInterface> currentEntityList = currentEntityMap.get(treeKey);
                JoiningDepthTreeLike currentTree = currentTreeMap.get(treeKey); // {a -> Leaf, b -> Leaf, c -> {d -> Leaf, e -> uTree}}
                GeneralRetrieveJoiningServiceInterface currentService = currentJoiningServiceMap.get(treeKey).v1();
                Map<String, GeneralRetrieveJoiningServiceInterface> currentJoiningService = currentJoiningServiceMap.get(treeKey).v2();

                List<GeneralJoiningEntityInterface> joiningEntityListBuffer = Collections.emptyList();

                if (Objects.isNull(currentEntityList) || currentEntityList.isEmpty()) {
                    continue;
                }

                Set<String> joiningKeySet = currentEntityList.get(0).joiningEntityMap().keySet();
                for (String subKey : joiningKeySet) {
                    logger().debug("subKey: " + subKey);
                    GeneralRetrieveJoiningServiceInterface joiningService = currentJoiningService.get(subKey);
                    List<Object> keyList = currentEntityList.stream().flatMap(e -> {
                        Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap = e.joiningKeyMap();
                        List<Object> innerKeyList = innerJoiningKeyMap.get(subKey).get();
                        return innerKeyList != null ? innerKeyList.stream() : Stream.empty();
                    }).collect(Collectors.toList());

                    List<GeneralJoiningEntityInterface> joiningEntityList = joiningService.findListByKey(keyList, subKey, currentService.entityClass());
                    currentEntityList.forEach(e -> {
                        Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap = e.joiningKeyMap();
                        List<Object> innerKeyList = innerJoiningKeyMap.get(subKey).get();
                        if (innerKeyList != null) {
                            List<GeneralJoiningEntityInterface> innerJoiningEntityList = joiningEntityList.stream().filter(je -> innerKeyList.contains(on(je).get("id"))).collect(Collectors.toList());
                            Map<String, Consumer<? super Object>> joiningEntityMap = e.joiningEntityMap();
                            innerJoiningEntityList.forEach(joiningEntityMap.get(subKey));
                        }
                    });
                    joiningEntityListBuffer = joiningEntityList;

                    Optional<JoiningDepthTreeLike> subTree = currentTree.subTree(subKey);
                    if (subTree.map(JoiningDepthTreeLike::isTree).orElse(false)) {
                        // tree
                        DepthTreeType depthTreeType = subTree.map(JoiningDepthTreeLike::type).orElse(DepthTreeType.DEFAULT);
                        switch (depthTreeType) {
                            case UNLIMITED:
                                if (Objects.nonNull(joiningEntityList) && !joiningEntityList.isEmpty()) {
                                    Set<String> innerJoiningKeySet = joiningEntityList.get(0).joiningEntityMap().keySet();
                                    JoiningDepthTree subTree2 = new JoiningDepthTree();
                                    for (String joiningSubKey : innerJoiningKeySet) {
                                        subTree2.put(joiningSubKey, JoiningDepthTree.unlimitedTree);
                                    }
                                    currentTreeMapBuffer.put(subKey, subTree2);
                                }
                                break;
                            case FIXED:
                                if (Objects.nonNull(joiningEntityList) && !joiningEntityList.isEmpty()) {
                                    Set<String> innerJoiningKeySet = joiningEntityList.get(0).joiningEntityMap().keySet();
                                    JoiningDepthTree subTree2 = new JoiningDepthTree();
                                    for (String joiningSubKey : innerJoiningKeySet) {
                                        subTree2.put(joiningSubKey, subTree.get());
                                    }
                                    currentTreeMapBuffer.put(subKey, subTree2);
                                }
                                break;
                            case NORMAL:
                                currentTreeMapBuffer.put(subKey, subTree.get());
                                break;
                            default:
                                throw new RuntimeException(String.format("can't process depthTreeType: %s", depthTreeType.name()));

                        }

                        currentEntityMapBuffer.put(subKey, joiningEntityListBuffer);
                        currentJoiningServiceMapBuffer.putIfAbsent(subKey, new Tuple2<>(joiningService, joiningService.joiningService()));
                    }
                }
            }

            currentTreeMap = new HashMap<>(currentTreeMapBuffer);
            currentEntityMap = new HashMap<>(currentEntityMapBuffer);
            currentJoiningServiceMap = new HashMap<>(currentJoiningServiceMapBuffer);

            currentTreeMapBuffer.clear();
            currentEntityMapBuffer.clear();
            currentJoiningServiceMapBuffer.clear();
        }
    }
}
