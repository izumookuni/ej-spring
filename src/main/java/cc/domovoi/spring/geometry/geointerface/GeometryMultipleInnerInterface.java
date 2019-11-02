package cc.domovoi.spring.geometry.geointerface;

import cc.domovoi.spring.geometry.annotation.GeometryInner;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    Map<String, Supplier<? extends INNER>> geometryInnerGetMap();

    /**
     * Consumer for each INNER object. Object of different types are distinguished using Map key.
     *
     * @return Consumer for each INNER object.
     */
    Map<String, Consumer<? super INNER>> geometryInnerSetMap();

    default Set<GeometryInner> geometryInnerSet(Class<? extends GeometryMultipleInnerInterface> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> field.getAnnotation(GeometryInner.class)).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
