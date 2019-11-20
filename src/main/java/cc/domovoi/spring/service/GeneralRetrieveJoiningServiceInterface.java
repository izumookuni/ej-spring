package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.service.annotation.JoiningTable;
import cc.domovoi.spring.service.annotation.after.AfterFind;
import cc.domovoi.spring.service.annotation.after.AfterFindList;
import cc.domovoi.spring.service.annotation.before.BeforeFind;
import cc.domovoi.spring.service.annotation.condition.FindCondition;
import cc.domovoi.spring.utils.joiningdepthtree.DepthTreeType;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningFixedDepthTree;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;
import static org.joor.Reflect.onClass;

public interface GeneralRetrieveJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends OriginalServiceInterface {

    Class<E> entityClass();

    E innerFindEntity(K id);

    List<E> innerFindList(E entity);

    List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass);

    default Map<String, GeneralRetrieveJoiningServiceInterface> joiningService() {
        return innerJoiningService();
    }

    default Map<String, GeneralRetrieveJoiningServiceInterface> innerJoiningService() {
        Map<String, GeneralRetrieveJoiningServiceInterface> joiningService = new HashMap<>();
        java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
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

    default void beforeFindEntity(E entity) {
    }

    default void afterFindEntity(E entity) {
    }

    default void afterFindList(List<E> entityList) {
        entityList.forEach(this::afterFindEntity);
    }

    default <T extends Annotation> void doFindAnnotationMethod(Class<T> aClass, Integer scope, Object... args) {
        logger().debug(String.format("doFindAnnotationMethod(%s %s)", aClass.getSimpleName(), scope));
        List<Tuple2<T, Method>> methodAnnotationList = methodAnnotationOrdered(this.getClass(), aClass, scope);
//        logger().debug("methodAnnotationList size: " + methodAnnotationList.size());
        methodAnnotationList.forEach(t2 -> {
            try {
                logger().debug("method: " + t2.v2().getName());
                on(this).call(t2.v2().getName(), args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    default void doBeforeFindEntity(Integer scope, E entity) {
        logger().debug("doBeforeFindEntity");
        if (0 == scope) {
            beforeFindEntity(entity);
        }
        doFindAnnotationMethod(BeforeFind.class, scope, entity);
    }

    default void doAfterFindEntity(Integer scope, E entity) {
        logger().debug("doAfterFindEntity");
        if (0 == scope) {
            afterFindEntity(entity);
        }
        doFindAnnotationMethod(AfterFind.class, scope, entity);
    }

    default void doAfterFindList(Integer scope, List<E> entityList) {
        if (0 == scope) {
            afterFindList(entityList);
        }
        doFindAnnotationMethod(AfterFindList.class, scope, entityList);
    }

    default Optional<String> findCondition(E entity) {
        return Optional.empty();
    }

    default Optional<String> doFindCondition(E entity) {
        Optional<String> findCondition = findCondition(entity);
        if (findCondition.isPresent()) {
            return findCondition;
        }
        List<Tuple2<FindCondition, Method>> conditionMethodList = methodAnnotationOrdered(this.getClass(), FindCondition.class, 0);
        for (Tuple2<FindCondition, Method> t2 : conditionMethodList) {
            try {
                Optional<String> findCondition1 = on(this).call(t2.v2().getName(), entity).get();
                if (findCondition1.isPresent()) {
                    return findCondition1;
                }
            } catch (Exception e) {
                return Optional.of(e.getMessage());
            }
        }
        return Optional.empty();
    }

    default Optional<String> collectCondition(E entity) {
        return Optional.empty();
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

    default E findEntity(K id, Function<? super K, ? extends E> function, JoiningDepthTreeLike depthTree) {
        // no findCondition
        // before find
//        processBeforeFindResult(id, "id");
        // find entity
        E e = function.apply(id);
        // after find
        if (Objects.nonNull(e)) {
//            processAfterFindResult(Collections.singletonList(e));
            doAfterFindEntity(0, e);
//            joinEntityListByTree(Collections.singletonList(e), depthTree);
//            doAfterFindEntity(1, e);
            Optional<String> collectConditionResult = collectCondition(e);
            if (collectConditionResult.isPresent()) {
                throw new RuntimeException(collectConditionResult.get());
            }
            else if (Objects.nonNull(e.getAvailable()) && !e.getAvailable()) {
                throw new RuntimeException("record is not available");
            }
            else {
                doAfterFindEntity(1, e);
                joinEntityListByTree(Collections.singletonList(e), depthTree);
                doAfterFindEntity(2, e);
                return e;
            }
        }
        else {
            return null;
        }
    }

    default E findEntity(K id, JoiningDepthTreeLike depthTree) {
        return findEntity(id, this::innerFindEntity, depthTree);
    }

    default E findEntity(E entity, Function<? super E, ? extends E> function, JoiningDepthTreeLike depthTree) {
        // findCondition
        Optional<String> findConditionResult = doFindCondition(entity);
        if (findConditionResult.isPresent()) {
            throw new RuntimeException(findConditionResult.get());
        }
        // before find
        if (Objects.nonNull(entity)) {
            doBeforeFindEntity(0, entity);
//            processBeforeFindResult(entity, "entity");
        }
        E e = function.apply(entity);
        // after find
        if (Objects.nonNull(e)) {
//            processAfterFindResult(Collections.singletonList(e));
            doAfterFindEntity(0, e);
//            joinEntityListByTree(Collections.singletonList(e), depthTree);
//            doAfterFindEntity(1, e);
            Optional<String> collectConditionResult = collectCondition(e);
            if (collectConditionResult.isPresent()) {
                throw new RuntimeException(collectConditionResult.get());
            }
            else if (Objects.nonNull(e.getAvailable()) && !e.getAvailable()) {
                throw new RuntimeException("record is not available");
            }
            else {
                doAfterFindEntity(1, e);
                joinEntityListByTree(Collections.singletonList(e), depthTree);
                doAfterFindEntity(2, e);
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

    default E findEntity(E entity, JoiningDepthTreeLike depthTree) {
        return findEntity(entity, this::innerFindEntity, depthTree);
    }

    default List<E> findList(E entity, Function<? super E, ? extends List<E>> function, JoiningDepthTreeLike depthTree) {
        // findCondition
        Optional<String> findConditionResult = doFindCondition(entity);
        if (findConditionResult.isPresent()) {
            throw new RuntimeException(findConditionResult.get());
        }
        // before find
        if (Objects.nonNull(entity)) {
//            beforeFindEntity(entity);
            doBeforeFindEntity(0, entity);
//            processBeforeFindResult(entity, "entity");
        }

//        List<E> eList = innerFindList(entity);
//        eList.forEach(this::afterFindEntity);
//        joinEntityListByTree(eList, depthTree);
//        return eList.stream().filter(e -> !collectCondition(e).isPresent()).collect(Collectors.toList());

        List<E> eList0 = function.apply(entity);
        doAfterFindList(0, eList0);
//        processAfterFindResult(eList0);
        List<E> eList1 = eList0.stream()
                .filter(e -> (Objects.isNull(e.getAvailable()) || e.getAvailable()) && !collectCondition(e).isPresent())
                .collect(Collectors.toList());
        doAfterFindList(1, eList1);
        joinEntityListByTree(eList1, depthTree);
        doAfterFindList(2, eList1);
        return eList1;
    }

    default List<E> findList(Supplier<? extends List<E>> supplier, JoiningDepthTreeLike depthTree) {
        E entity = onClass(entityClass()).create().get();
        return findList(entity, e -> supplier.get(), depthTree);
    }

    default List<E> findList(E entity, JoiningDepthTreeLike depthTree) {
        return findList(entity, this::innerFindList, depthTree);
    }

    @SuppressWarnings("unchecked")
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
                    if (Objects.nonNull(joiningService)) {
                        List<Object> keyList = currentEntityList.stream().flatMap(e -> {
                            Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap = e.joiningKeyMap();
                            List<Object> innerKeyList = innerJoiningKeyMap.get(subKey).get();
                            return innerKeyList != null ? innerKeyList.stream() : Stream.empty();
                        }).collect(Collectors.toList());

                        List<GeneralJoiningEntityInterface> joiningEntityList0 = joiningService.findListByKey(keyList, subKey, currentService.entityClass());
                        List<GeneralJoiningEntityInterface> joiningEntityList = joiningEntityList0.stream().filter(e -> (Objects.isNull(e.getAvailable()) || e.getAvailable()) && !joiningService.collectCondition(e).isPresent()).collect(Collectors.toList());
                        joiningService.doAfterFindList(1, joiningEntityList);
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

    static <T extends Annotation> List<Tuple2<T, Method>> methodAnnotationOrdered(Class<?> sClass, Class<T> aClass, int scope) {
        Method[] methods = sClass.getMethods();
        return Stream.of(methods)
                .map(method -> new Tuple2<>(method.getAnnotation(aClass), method))
                .filter(t2 -> Objects.nonNull(t2.v1()) && on(t2.v1()).call("scope").get().equals(scope))
                .sorted(Comparator.comparing(t2 -> (Integer) on(t2.v1()).call("order").get(), Comparator.naturalOrder()))
                .collect(Collectors.toList());
//        Stream.of(methods).map(method -> {
//            Order order = method.getAnnotation(Order.class);
//            return new Tuple3<>(method.getAnnotation(aClass), Objects.nonNull(order) ? order.value() : Integer.MAX_VALUE, method);
//        }).filter(t3 -> Objects.nonNull(t3.v1())).sorted(Comparator.comparing(Tuple3::v2, Comparator.naturalOrder())).collect(Collectors.toList())
    }
}
