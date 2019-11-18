package cc.domovoi.spring.entity;

import cc.domovoi.spring.entity.annotation.JoiningProperty;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;

public interface GeneralAnnotationEntityInterface<K> extends GeneralJoiningEntityInterface<K> {

    default Set<Tuple2<JoiningProperty, Field>> joiningPropertySet() {
        return joiningPropertySet(this.getClass());
    }

    @Override
    default Map<String, Supplier<? extends List<Object>>> joiningKeyMap() {
        return innerJoiningKeyMap();
    }

    @Override
    default Map<String, Consumer<? super Object>> joiningEntityMap() {
        return innerJoiningEntityMap();
    }

    default Map<String, Supplier<? extends List<Object>>> innerJoiningKeyMap() {
        Map<String, Supplier<? extends List<Object>>> joiningKeyMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        Reflect reflect = on(this);
        for (Field field : fields) {
            JoiningProperty joiningProperty = field.getAnnotation(JoiningProperty.class);
            if (joiningProperty != null) {
                String name = field.getName();
                String joiningName = "".equals(joiningProperty.value()) ? name : joiningProperty.value();
                Class<?> fieldClass = field.getType();
                if (fieldClass.getSimpleName().matches("(List)|(ArrayList)")) {
                    joiningKeyMap.put(joiningName, () -> reflect.get(joiningProperty.sourceKey()));

                }
                else {
                    joiningKeyMap.put(joiningName, () -> Collections.singletonList(reflect.get(joiningProperty.sourceKey())));
                }
            }
        }
        return joiningKeyMap;
    }

    @SuppressWarnings("unchecked")
    default Map<String, Consumer<? super Object>> innerJoiningEntityMap() {
        Map<String, Consumer<? super Object>> joiningEntityMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        Reflect reflect = on(this);
        for (Field field : fields) {
            JoiningProperty joiningProperty = field.getAnnotation(JoiningProperty.class);
            if (joiningProperty != null) {
                String name = field.getName();
                String joiningName = "".equals(joiningProperty.value()) ? name : joiningProperty.value();
                Class<?> fieldClass = field.getType();
                if (fieldClass.getSimpleName().matches("(List)|(ArrayList)")) {
                    joiningEntityMap.put(joiningName, (Object e) -> {
                        List<Object> list = reflect.get(name);
                        if (list == null) {
                            reflect.set(name, new ArrayList<>());
                        }
                        ((List<Object>) reflect.get(name)).add(e);
                    });
                }
                else {
                    joiningEntityMap.put(joiningName, (Object e) -> reflect.set(name, e));
                }
            }
        }
        return joiningEntityMap;
    }

    static Set<Tuple2<JoiningProperty, Field>> joiningPropertySet(Class<?> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> new Tuple2<>(field.getAnnotation(JoiningProperty.class), field)).filter(t2 -> Objects.nonNull(t2.v1())).collect(Collectors.toSet());
    }

    static <T extends Annotation> Set<Tuple2<T, Field>> fieldAnnotationSet(Class<?> eClass, Class<T> aClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> new Tuple2<>(field.getAnnotation(aClass), field)).filter(t2 -> Objects.nonNull(t2.v1())).collect(Collectors.toSet());
    }

    static <T extends Annotation> Set<Tuple2<T, Method>>  methodAnnotationSet(Class<?> eClass, Class<T> aClass) {
        Method[] methods = eClass.getDeclaredMethods();
        return Stream.of(methods).map(method -> new Tuple2<>(method.getAnnotation(aClass), method)).filter(t2 -> Objects.nonNull(t2.v1())).collect(Collectors.toSet());
    }


}
