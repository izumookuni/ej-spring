package cc.domovoi.spring.utils;

import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;

public class GeneralUtils {

    private static Logger logger = CommonLogger.logger;

    public static  <T, A extends Annotation> void doFindAnnotationMethod(T t, Class<A> aClass, Integer scope, Object... args) {
        List<Tuple2<A, Method>> methodAnnotationList = methodAnnotationOrdered(t.getClass(), aClass, scope);
//        logger().debug("methodAnnotationList size: " + methodAnnotationList.size());
        methodAnnotationList.forEach(t2 -> {
            logger.debug("{} method: {}", aClass.getSimpleName(), t2.v2().getName());
            try {
                on(t).call(t2.v2().getName(), args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static <T, A extends Annotation> List<Tuple2<A, Method>> methodAnnotationOrdered(Class<T> tClass, Class<A> aClass, int scope) {
        Method[] methods = tClass.getMethods();
        return Stream.of(methods)
                .map(method -> new Tuple2<>(method.getAnnotation(aClass), method))
                .filter(t2 -> Objects.nonNull(t2.v1()) && on(t2.v1()).call("scope").get().equals(scope))
                .sorted(Comparator.comparing(t2 -> (Integer) on(t2.v1()).call("order").get(), Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }
}
