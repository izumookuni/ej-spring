package cc.domovoi.spring.geometry.geointerface;

import cc.domovoi.spring.geometry.annotation.GeometryInner;
import org.jooq.lambda.tuple.Tuple2;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.*;

/**
 * GeometryMultipleInnerInterface.
 *
 * @param <INNER> INNER type.
 */
public interface GeometryMultipleInnerInterface<INNER> {

    /**
     * Supplier for each INNER object. Object of different types are distinguished using Map key.
     *
     * @return Supplier for each INNER object.
     */
    default Map<String, Supplier<? extends INNER>> geometryInnerGetMap() {
        Set<Tuple2<GeometryInner, Field>> geometryInnerSet = geometryInnerSet(this.getClass());
        Map<String, Supplier<? extends INNER>> geometryInnerGetMap = new HashMap<>();
        geometryInnerSet.forEach(t2 -> {
            String value = t2.v1().value();
            String fieldName = t2.v2.getName();
            String key = !"".equals(value) ? value : fieldName;
            geometryInnerGetMap.put(key, () -> on(this).get(fieldName));
        });
        return geometryInnerGetMap;
    }

    /**
     * Consumer for each INNER object. Object of different types are distinguished using Map key.
     *
     * @return Consumer for each INNER object.
     */
    default Map<String, Consumer<? super INNER>> geometryInnerSetMap() {
        Set<Tuple2<GeometryInner, Field>> geometryInnerSet = geometryInnerSet(this.getClass());
        Map<String, Consumer<? super INNER>> geometryInnerSetMap = new HashMap<>();
        geometryInnerSet.forEach(t2 -> {
            String value = t2.v1().value();
            String fieldName = t2.v2.getName();
            String key = !"".equals(value) ? value : fieldName;
            geometryInnerSetMap.put(key, inner -> on(this).set(fieldName, inner));
        });
        return geometryInnerSetMap;
    }

    default Set<Tuple2<GeometryInner, Field>> geometryInnerSet(Class<? extends GeometryMultipleInnerInterface> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> new Tuple2<>(field.getAnnotation(GeometryInner.class), field)).filter(t2 -> Objects.nonNull(t2.v1())).collect(Collectors.toSet());
    }
}
