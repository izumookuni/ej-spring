package cc.domovoi.spring.geometry.geointerface;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * GeometryMultipleInterface.
 *
 * @param <INNER> INNER type.
 */
public interface GeometryMultipleInterface<INNER> {

    /**
     * Supplier for each INNER object. Object of different types are distinguished using Map key.
     *
     * @return Supplier for each INNER object.
     */
    Map<String, Supplier<? extends INNER>> geometryGetMap();

    /**
     * Consumer for each INNER object. Object of different types are distinguished using Map key.
     *
     * @return Consumer for each INNER object.
     */
    Map<String, Consumer<? super INNER>> geometrySetMap();
}
