package cc.domovoi.ej.spring.geometry.geointerface;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GeometricMultipleInterface<OUTER> {

    Map<String, Supplier<? extends OUTER>> geometricGetMap();

    Map<String, Consumer<? super OUTER>> geometricSetMap();

}
