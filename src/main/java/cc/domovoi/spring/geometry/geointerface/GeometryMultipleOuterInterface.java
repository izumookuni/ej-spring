package cc.domovoi.spring.geometry.geointerface;

import cc.domovoi.spring.geometry.annotation.GeometryInner;
import cc.domovoi.spring.geometry.annotation.GeometryOuter;
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

import static org.joor.Reflect.on;

/**
 * GeometryMultipleOuterInterface.
 *
 * @param <OUTER> OUTER type.
 */
public interface GeometryMultipleOuterInterface<OUTER> {

    /**
     * Supplier for each OUTER object. Object of different types are distinguished using Map key.
     *
     * @return Supplier for each OUTER object.
     */
    default Map<String, Supplier<? extends OUTER>> geometryOuterGetMap() {
        Set<Tuple2<GeometryOuter, Field>> geometryOuterSet = geometryOuterSet(this.getClass());
        Map<String, Supplier<? extends OUTER>> geometryOuterGetMap = new HashMap<>();
        geometryOuterSet.forEach(t2 -> {
            String value = t2.v1().value();
            String fieldName = t2.v2.getName();
            String key = !"".equals(value) ? value : fieldName;
            geometryOuterGetMap.put(key, () -> on(this).get(fieldName));
        });
        return geometryOuterGetMap;
    }

    /**
     * Consumer for each OUTER object. Object of different types are distinguished using Map key.
     *
     * @return Consumer for each OUTER object.
     */
    default Map<String, Consumer<? super OUTER>> geometryOuterSetMap() {
        Set<Tuple2<GeometryOuter, Field>> geometryOuterSet = geometryOuterSet(this.getClass());
        Map<String, Consumer<? super OUTER>> geometryOuterSetMap = new HashMap<>();
        geometryOuterSet.forEach(t2 -> {
            String value = t2.v1().value();
            String fieldName = t2.v2.getName();
            String key = !"".equals(value) ? value : fieldName;
            geometryOuterSetMap.put(key, inner -> on(this).set(fieldName, inner));
        });
        return geometryOuterSetMap;
    }

    default Set<Tuple2<GeometryOuter, Field>> geometryOuterSet(Class<? extends GeometryMultipleOuterInterface> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> new Tuple2<>(field.getAnnotation(GeometryOuter.class), field)).filter(t2 -> Objects.nonNull(t2.v1())).collect(Collectors.toSet());
    }
}
