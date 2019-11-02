package cc.domovoi.spring.geometry.geointerface;

import cc.domovoi.spring.geometry.annotation.GeometryOuter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    Map<String, Supplier<? extends OUTER>> geometryOuterGetMap();

    /**
     * Consumer for each OUTER object. Object of different types are distinguished using Map key.
     *
     * @return Consumer for each OUTER object.
     */
    Map<String, Consumer<? super OUTER>> geometryOuterSetMap();

    default Set<GeometryOuter> geometryOuterSet(Class<? extends GeometryMultipleOuterInterface> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> field.getAnnotation(GeometryOuter.class)).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
