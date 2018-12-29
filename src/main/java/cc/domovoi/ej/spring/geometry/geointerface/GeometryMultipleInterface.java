package cc.domovoi.ej.spring.geometry.geointerface;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GeometryMultipleInterface<INNER> {

    Map<String, Supplier<? extends INNER>> geometryGetMap();

    Map<String, Consumer<? super INNER>> geometrySetMap();
}
