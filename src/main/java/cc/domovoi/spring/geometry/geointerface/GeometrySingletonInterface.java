package cc.domovoi.spring.geometry.geointerface;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GeometrySingletonInterface<INNER> extends GeometryMultipleInterface<INNER> {

    INNER getGeometry();

    void setGeometry(INNER geometry);

    @Override
    default Map<String, Supplier<? extends INNER>> geometryGetMap() {
        return Collections.singletonMap("geometry", this::getGeometry);
    }

    @Override
    default Map<String, Consumer<? super INNER>> geometrySetMap() {
        return Collections.singletonMap("geometry", this::setGeometry);
    }
}
