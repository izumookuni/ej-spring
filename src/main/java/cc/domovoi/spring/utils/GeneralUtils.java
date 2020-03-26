package cc.domovoi.spring.utils;

import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;

public class GeneralUtils {

    private static Logger logger = CommonLogger.logger;

    public static <T, A extends Annotation> void doAnnotationMethod(T t, Class<A> aClass, Integer scope, String name, Object... args) {
        List<Tuple2<A, Method>> methodAnnotationList = methodAnnotationOrdered(t.getClass(), aClass, scope, name);
        int argsCount = args.length;
//        logger().debug("methodAnnotationList size: " + methodAnnotationList.size());
        methodAnnotationList.forEach(t2 -> {
            logger.debug("{} method: {}", aClass.getSimpleName(), t2.v2().getName());
            try {
                int parameterCount = t2.v2().getParameterCount();
                if (argsCount < parameterCount) {
                    throw new RuntimeException(String.format("argsCount(%d) less than method(%s) parameterCount(%d)", argsCount, t2.v2.getName(), parameterCount));
                }
                on(t).call(t2.v2().getName(), argsCount == parameterCount ? args : Stream.of(args).limit(parameterCount).toArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static  <T, A extends Annotation> Optional<String> doCondition(T t, Class<A> aClass, int scope, String name, Object... args) {
        List<Tuple2<A, Method>> conditionMethodList = GeneralUtils.methodAnnotationOrdered(t.getClass(), aClass, scope, name);
        int argsCount = args.length;
        for (Tuple2<A, Method> t2 : conditionMethodList) {
            logger.debug("{} method: {}", aClass.getSimpleName(), t2.v2().getName());
            try {
                int parameterCount = t2.v2().getParameterCount();
                if (argsCount < parameterCount) {
                    throw new RuntimeException(String.format("argsCount(%d) less than method(%s) parameterCount(%d)", argsCount, t2.v2.getName(), parameterCount));
                }
                Optional<String> condition = on(t).call(t2.v2.getName(), argsCount == parameterCount ? args : Stream.of(args).limit(parameterCount).toArray()).get();
                if (condition.isPresent()) {
                    return condition;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.of(e.getMessage());
            }
        }
        return Optional.empty();
    }

    public static <T, A extends Annotation> List<Tuple2<A, Method>> methodAnnotationOrdered(Class<T> tClass, Class<A> aClass, int scope, String name) {
        Method[] methods = tClass.getMethods();
        return Stream.of(methods)
                .map(method -> new Tuple2<>(method.getAnnotation(aClass), method))
                .filter(t2 -> {
                    boolean scopeFlag = Objects.nonNull(t2.v1()) && on(t2.v1()).call("scope").get().equals(scope);
                    if (scopeFlag) {
                        List<String> skip = Arrays.asList(on(t2.v1()).call("skip").get());
                        List<String> include = Arrays.asList(on(t2.v1()).call("include").get());
                        if (skip.isEmpty() && include.isEmpty()) {
                            return true;
                        }
                        else if (include.isEmpty()) {
                            return !skip.contains(name);
                        }
                        else if (skip.isEmpty()) {
                            return include.contains(name);
                        }
                        else {
                            return !skip.contains(name) && include.contains(name);
                        }
                    }
                    else {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(t2 -> (Integer) on(t2.v1()).call("order").get(), Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }


}
