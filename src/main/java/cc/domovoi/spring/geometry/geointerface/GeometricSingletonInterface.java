package cc.domovoi.spring.geometry.geointerface;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * GeometricSingletonInterface.
 *
 * @param <OUTER> OUTER type.
 */
public interface GeometricSingletonInterface<OUTER> extends GeometricMultipleInterface<OUTER> {

    /**
     * Geometric data.
     *
     * @return Geometric data
     */
    OUTER getGeometricModel();

    /**
     * Set Geometric data.
     *
     * @param geometricModel Geometric data.
     */
    void setGeometricModel(OUTER geometricModel);

    @Override
    default Map<String, Supplier<? extends OUTER>> geometricGetMap() {
        return Collections.singletonMap("geometry", this::getGeometricModel);
    }

    @Override
    default Map<String, Consumer<? super OUTER>> geometricSetMap() {
        return Collections.singletonMap("geometry", this::setGeometricModel);
    }
}
