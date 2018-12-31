package cc.domovoi.ej.spring.geometry.converter;

import java.util.function.Function;

/**
 * A geometry loader to load INNER object from OUTER object.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
@FunctionalInterface
public interface GeometryLoader<INNER, OUTER> extends Function<OUTER, INNER> {

    /**
     * Load INNER object from OUTER object.
     *
     * @param outer OUTER object.
     * @return INNER object.
     */
    INNER loadGeometry(OUTER outer);

    /**
     * Applies this function to the given argument.
     *
     * @param outer the function argument
     * @return the function result
     */
    @Override
    default INNER apply(OUTER outer) {
        return loadGeometry(outer);
    }
}
