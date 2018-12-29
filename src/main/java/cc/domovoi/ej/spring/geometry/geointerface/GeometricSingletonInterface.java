package cc.domovoi.ej.spring.geometry.geointerface;

import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GeometricSingletonInterface<OUTER> extends GeometricMultipleInterface<OUTER> {

    @ApiModelProperty(value = "Geometric data")
    OUTER getGeometricModel();

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
