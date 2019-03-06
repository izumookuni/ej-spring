package cc.domovoi.spring.geometry.geointerface;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * GeometricMultipleInterface.
 *
 * @param <OUTER> OUTER type.
 */
public interface GeometricMultipleInterface<OUTER> {

    /**
     * Supplier for each OUTER object. Object of different types are distinguished using Map key.
     *
     * @return Supplier for each OUTER object.
     */
    Map<String, Supplier<? extends OUTER>> geometricGetMap();

    /**
     * Consumer for each OUTER object. Object of different types are distinguished using Map key.
     *
     * @return Consumer for each OUTER object.
     */
    Map<String, Consumer<? super OUTER>> geometricSetMap();

}
