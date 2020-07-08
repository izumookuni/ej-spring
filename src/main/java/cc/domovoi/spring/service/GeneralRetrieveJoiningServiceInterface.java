package cc.domovoi.spring.service;

import cc.domovoi.spring.annotation.condition.CollectCondition;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.audit.AuditUtils;
import cc.domovoi.spring.service.annotation.JoiningTable;
import cc.domovoi.spring.annotation.after.AfterFind;
import cc.domovoi.spring.annotation.after.AfterFindList;
import cc.domovoi.spring.annotation.before.BeforeFind;
import cc.domovoi.spring.annotation.condition.FindCondition;
import cc.domovoi.spring.utils.GeneralUtils;
import cc.domovoi.spring.utils.joiningdepthtree.DepthTreeType;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningFixedDepthTree;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;
import static org.joor.Reflect.onClass;

public interface GeneralRetrieveJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends OriginalServiceInterface {

    Class<K> keyClass();

    Class<E> entityClass();

    E innerFindEntity(K id);

    List<E> innerFindList(E entity);

    List<E> innerFindListById(List<K> idList);

    List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass, Map<String, Object> params, String name);

    default Map<String, GeneralRetrieveJoiningServiceInterface> joiningService() {
        return innerJoiningService();
    }

    default Map<String, GeneralRetrieveJoiningServiceInterface> innerJoiningService() {
        Map<String, GeneralRetrieveJoiningServiceInterface> joiningService = new HashMap<>();
//        java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
        List<java.lang.reflect.Field> fields = AuditUtils.allFieldList(this.getClass());
        Reflect reflect = on(this);
        for (java.lang.reflect.Field field : fields) {
            JoiningTable joiningTable = field.getAnnotation(JoiningTable.class);
            if (joiningTable != null) {
                int valueLength = joiningTable.value().length;
                String name = field.getName();
                if (valueLength == 1) {
                    String joiningName = "".equals(joiningTable.value()[0]) ? name : joiningTable.value()[0];
                    logger().debug("joiningService.joiningName: " + joiningName);
                    joiningService.put(joiningName, reflect.get(name));
                }
                else if (valueLength > 1) {
                    for (String joiningName : joiningTable.value()) {
                        logger().debug("joiningService.joiningName: " + joiningName);
                        joiningService.put(joiningName, reflect.get(name));
                    }
                }
                else {
                    logger().debug("joiningService.joiningName: " + name);
                    joiningService.put(name, reflect.get(name));
                }
//                String name = field.getName();
//                String joiningName = "".equals(joiningTable.value()) ? name : joiningTable.value();
//                logger().debug("joiningService.joiningName: " + joiningName);
//                joiningService.put(joiningName, reflect.get(name));
            }
        }
        return joiningService;
    }

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

    default void beforeFindEntity(E entity, Map<String, Object> params) {
    }

    default void afterFindEntity(E entity, Map<String, Object> params) {
    }

    default void afterFindList(List<E> entityList, Map<String, Object> params) {
        entityList.forEach(e -> afterFindEntity(e, params));
    }

    default void doBeforeFindEntity(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeFindEntity(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeFind.class, scope, name, params, entity);
    }

    default void doAfterFindEntity(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            afterFindEntity(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterFind.class, scope, name, params, entity);
    }

    default void doAfterFindList(Integer scope, String name, List<E> entityList, Map<String, Object> params) {
        if (0 == scope) {
            afterFindList(entityList, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterFindList.class, scope, name, params, entityList);
    }

    default Optional<String> findCondition(E entity, Map<String, Object> params) {
        return Optional.empty();
    }

    default Optional<String> doFindCondition(String name, E entity, Map<String, Object> params) {
        Optional<String> findCondition = findCondition(entity, params);
        if (findCondition.isPresent()) {
            return findCondition;
        }
        return GeneralUtils.doCondition(this, FindCondition.class, 0, name, params, entity);
    }

    default Optional<String> collectCondition(E entity, Map<String, Object> params) {
        return Optional.empty();
    }

    default Optional<String> doCollectCondition(String name, E entity, Map<String, Object> params) {
        Optional<String> collectCondition = collectCondition(entity, params);
        if (collectCondition.isPresent()) {
            return collectCondition;
        }
        return GeneralUtils.doCondition(this, CollectCondition.class, 0, name, params, entity);
    }

    default JoiningDepthTreeLike depthTree() {
        return JoiningFixedDepthTree.singleLayer();
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

    default List<E> findListById(List<K> idList) {
        return findListById(idList, this::innerFindListById, depthTree(), new HashMap<>(), "findList");
    }

    default List<E> findListById(List<K> idList, JoiningDepthTreeLike depthTree) {
        return findListById(idList, this::innerFindListById, depthTree, new HashMap<>(), "findList");
    }

    default E findEntity(K id, Function<? super K, ? extends E> function, JoiningDepthTreeLike depthTree) {
        return findEntity(id, function, depthTree, new HashMap<>(), "findEntity");
    }

    default List<E> findListById(List<K> idList, Map<String, Object> params) {
        return findListById(idList, this::innerFindListById, depthTree(), params, "findList");
    }

    default List<E> findListById(List<K> idList, JoiningDepthTreeLike depthTree, Map<String, Object> params) {
        return findListById(idList, this::innerFindListById, depthTree, params, "findList");
    }

    default E findEntity(K id, Function<? super K, ? extends E> function, JoiningDepthTreeLike depthTree, Map<String, Object> params) {
        return findEntity(id, function, depthTree, params, "findEntity");
    }

    default E findEntity(K id, Function<? super K, ? extends E> function, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        // no findCondition
        // find entity
        E e = function.apply(id);
        // after find
        if (Objects.nonNull(e)) {
            doAfterFindEntity(0, name, e, params);
            Optional<String> collectConditionResult = collectCondition(e, params);
            if (collectConditionResult.isPresent()) {
                throw new RuntimeException(collectConditionResult.get());
            }
            else if (Objects.nonNull(e.getAvailable()) && !e.getAvailable()) {
                throw new RuntimeException("record is not available");
            }
            else {
                doAfterFindEntity(1, name, e, params);
                joinEntityListByTree(Collections.singletonList(e), depthTree, params, name);
                doAfterFindEntity(2, name, e, params);
                return e;
            }
        }
        else {
            return null;
        }
    }

    default E findEntity(K id, JoiningDepthTreeLike depthTree, String name) {
        return findEntity(id, this::innerFindEntity, depthTree, new HashMap<>(), name);
    }

    default E findEntity(K id, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        return findEntity(id, this::innerFindEntity, depthTree, params, name);
    }

    default E findEntity(K id, JoiningDepthTreeLike depthTree) {
        return findEntity(id, this::innerFindEntity, depthTree);
    }

    default E findEntity(K id, String name) {
        return findEntity(id, depthTree(), name);
    }

    default E findEntity(E entity, Function<? super E, ? extends E> function, JoiningDepthTreeLike depthTree) {
        return findEntity(entity, function, depthTree, new HashMap<>(), "findEntity");
    }

    default E findEntity(E entity, Function<? super E, ? extends E> function, JoiningDepthTreeLike depthTree, Map<String, Object> params) {
        return findEntity(entity, function, depthTree, params, "findEntity");
    }

    default E findEntity(E entity, Function<? super E, ? extends E> function, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        // findCondition
        Optional<String> findConditionResult = doFindCondition(name, entity, params);
        if (findConditionResult.isPresent()) {
            throw new RuntimeException(findConditionResult.get());
        }
        // before find
        if (Objects.nonNull(entity)) {
            doBeforeFindEntity(0, name, entity, params);
        }
        E e = function.apply(entity);
        // after find
        if (Objects.nonNull(e)) {
            doAfterFindEntity(0, name, e, params);
            Optional<String> collectConditionResult = collectCondition(e, params);
            if (collectConditionResult.isPresent()) {
                throw new RuntimeException(collectConditionResult.get());
            }
            else if (Objects.nonNull(e.getAvailable()) && !e.getAvailable()) {
                throw new RuntimeException("record is not available");
            }
            else {
                doAfterFindEntity(1, name, e, params);
                joinEntityListByTree(Collections.singletonList(e), depthTree, params, name);
                doAfterFindEntity(2, name, e, params);
                return e;
            }
        }
        else {
            return null;
        }
    }

    default E findEntity(Supplier<? extends E> supplier, JoiningDepthTreeLike depthTree) {
        E entity = onClass(entityClass()).create().get();
        return findEntity(entity, e -> supplier.get(), depthTree);
    }

    default E findEntity(Supplier<? extends E> supplier, JoiningDepthTreeLike depthTree, String name) {
        E entity = onClass(entityClass()).create().get();
        return findEntity(entity, e -> supplier.get(), depthTree, new HashMap<>(), name);
    }

    default E findEntity(E entity, JoiningDepthTreeLike depthTree, String name) {
        return findEntity(entity, this::innerFindEntity, depthTree, new HashMap<>(), name);
    }

    default E findEntity(Supplier<? extends E> supplier, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        E entity = onClass(entityClass()).create().get();
        return findEntity(entity, e -> supplier.get(), depthTree, params, name);
    }

    default E findEntity(E entity, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        return findEntity(entity, this::innerFindEntity, depthTree, params, name);
    }

    default E findEntity(E entity, JoiningDepthTreeLike depthTree) {
        return findEntity(entity, this::innerFindEntity, depthTree);
    }

    default E findEntity(E entity, String name) {
        return findEntity(entity, depthTree(), name);
    }

    default List<E> findList(E entity, Function<? super E, ? extends List<E>> function, JoiningDepthTreeLike depthTree) {
        return findList(entity, function, depthTree, new HashMap<>(), "findList");
    }

    default List<E> findList(E entity, Function<? super E, ? extends List<E>> function, JoiningDepthTreeLike depthTree, Map<String, Object> params) {
        return findList(entity, function, depthTree, params, "findList");
    }

    default List<E> findList(E entity, Function<? super E, ? extends List<E>> function, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        // findCondition
        Optional<String> findConditionResult = doFindCondition(name, entity, params);
        if (findConditionResult.isPresent()) {
            throw new RuntimeException(findConditionResult.get());
        }
        // before find
        if (Objects.nonNull(entity)) {
            doBeforeFindEntity(0, name, entity, params);
        }

        List<E> eList0 = function.apply(entity);
        doAfterFindList(0, name, eList0, params);
        List<E> eList1 = eList0.stream()
                .filter(e -> (Objects.isNull(e.getAvailable()) || e.getAvailable()) && !collectCondition(e, params).isPresent())
                .collect(Collectors.toList());
        doAfterFindList(1, name, eList1, params);
        joinEntityListByTree(eList1, depthTree, params, name);
        doAfterFindList(2, name, eList1, params);
        return eList1;
    }

    default List<E> findList(Supplier<? extends List<E>> supplier, JoiningDepthTreeLike depthTree) {
        E entity = onClass(entityClass()).create().get();
        return findList(entity, e -> supplier.get(), depthTree);
    }

    default List<E> findList(Supplier<? extends List<E>> supplier, JoiningDepthTreeLike depthTree, String name) {
        E entity = onClass(entityClass()).create().get();
        return findList(entity, e -> supplier.get(), depthTree, new HashMap<>(), name);
    }

    default List<E> findList(E entity, JoiningDepthTreeLike depthTree, String name) {
        return findList(entity, this::innerFindList, depthTree, new HashMap<>(), name);
    }

    default List<E> findList(Supplier<? extends List<E>> supplier, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        E entity = onClass(entityClass()).create().get();
        return findList(entity, e -> supplier.get(), depthTree, params, name);
    }

    default List<E> findList(E entity, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        return findList(entity, this::innerFindList, depthTree, params, name);
    }

    default List<E> findList(E entity, JoiningDepthTreeLike depthTree) {
        return findList(entity, this::innerFindList, depthTree);
    }

    default List<E> findList(E entity, String name) {
        return findList(entity, depthTree(), name);
    }

    default List<E> findListById(List<K> idList, Function<? super List<K>, ? extends List<E>> function, JoiningDepthTreeLike depthTree, Map<String, Object> params, String name) {
        // no findCondition
        // find entityList
        List<E> eList0 = function.apply(idList);
        doAfterFindList(0, name, eList0, params);
        List<E> eList1 = eList0.stream()
                .filter(e -> (Objects.isNull(e.getAvailable()) || e.getAvailable()) && !collectCondition(e, params).isPresent())
                .collect(Collectors.toList());
        doAfterFindList(1, name, eList1, params);
        joinEntityListByTree(eList1, depthTree, params, name);
        doAfterFindList(2, name, eList1, params);
        return eList1;
    }

    @SuppressWarnings("unchecked")
    default void joinEntityListByTree(List<E> entityList, JoiningDepthTreeLike tree, Map<String, Object> params, String name) {
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

                Set<String> joiningKeySet = ((Set<String>) currentEntityList.get(0).joiningEntityMap().keySet()).stream().filter(currentTree::contains).collect(Collectors.toSet());
                for (String subKey : joiningKeySet) {
                    logger().debug("subKey: " + subKey);
                    GeneralRetrieveJoiningServiceInterface joiningService = currentJoiningService.get(subKey);
                    if (Objects.nonNull(joiningService)) {
                        List<Object> keyList = currentEntityList.stream().flatMap(e -> {
                            Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap = e.joiningKeyMap();
                            List<Object> innerKeyList = innerJoiningKeyMap.get(subKey).get();
                            return innerKeyList != null ? innerKeyList.stream() : Stream.empty();
                        }).collect(Collectors.toList());

                        List<GeneralJoiningEntityInterface> joiningEntityList0 = joiningService.findListByKey(keyList, subKey, currentService.entityClass(), params, name);
                        List<GeneralJoiningEntityInterface> joiningEntityList = joiningEntityList0.stream().filter(e -> (Objects.isNull(e.getAvailable()) || e.getAvailable()) && !joiningService.collectCondition(e, params).isPresent()).collect(Collectors.toList());
                        joiningService.doAfterFindList(1, name, joiningEntityList, params);
                        currentEntityList.forEach(e -> {
                            Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap = e.joiningKeyMap();
                            List<Object> innerKeyList = innerJoiningKeyMap.get(subKey).get();
                            if (innerKeyList != null) {
                                List<GeneralJoiningEntityInterface> innerJoiningEntityList = joiningEntityList.stream().filter(je -> innerKeyList.contains(je.getId())).collect(Collectors.toList());
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
                                            subTree2.put(joiningSubKey, JoiningDepthTree.unlimitedTree());
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
                    else {
                        logger().warn("no joiningService for subKey: " + subKey);
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
