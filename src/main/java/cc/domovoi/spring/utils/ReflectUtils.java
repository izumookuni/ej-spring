package cc.domovoi.spring.utils;

import org.jooq.lambda.tuple.Tuple2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectUtils {

    public static List<Field> allFieldList(Class<?> clazz) {
        Map<String, Field> result = new HashMap<>();
        Class<?> t = clazz;
        do {
            for (Field field : t.getDeclaredFields()) {
                String name = field.getName();

                if (!result.containsKey(name)) {
                    result.put(name, field);
                }
            }

            t = t.getSuperclass();
        }
        while (t != null);

        return new ArrayList<>(result.values());
    }

    public static List<Method> allMethodList(Class<?> clazz) {
        Map<String, List<Method>> result = new HashMap<>();
        Class<?> t = clazz;
        do {
            for (Method method : t.getDeclaredMethods()) {
                String name = method.getName();

                if (!result.containsKey(name)) {
                    List<Method> methodList = new ArrayList<>();
                    methodList.add(method);
                    result.put(name, methodList);
                }
                else {
                    result.get(name).add(method);
                }
            }
            t = t.getSuperclass();
        }
        while (t != null);

        return result.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static List<Parameter> parameterList(Method method) {
        return Stream.of(method.getParameters()).collect(Collectors.toList());
    }

    public static <A extends Annotation> List<Tuple2<Parameter, A>> parameterList(Method method, Class<A> aClass) {
        return Stream.of(method.getParameters())
                .map(parameter -> new Tuple2<>(parameter, parameter.getAnnotation(aClass)))
                .filter(t2 -> Objects.nonNull(t2.v2()))
                .collect(Collectors.toList());
    }

}
