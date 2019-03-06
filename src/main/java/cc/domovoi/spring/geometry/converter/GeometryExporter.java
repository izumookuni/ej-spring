package cc.domovoi.spring.geometry.converter;

import java.util.function.Function;

/**
 * A geometry exporter to export INNER object to OUTER object.
 *
 * @param <INNER> INNER type
 * @param <OUTER> OUTER type
 */
@FunctionalInterface
public interface GeometryExporter<INNER, OUTER> extends Function<INNER, OUTER> {

    /**
     * Export INNER type to OUTER type.
     *
     * @param inner INNER object.
     * @return OUTER object.
     */
    OUTER exportGeometry(INNER inner);

    /**
     * Applies this function to the given argument.
     *
     * @param inner the function argument
     * @return the function result
     */
    @Override
    default OUTER apply(INNER inner) {
        return exportGeometry(inner);
    }
}
