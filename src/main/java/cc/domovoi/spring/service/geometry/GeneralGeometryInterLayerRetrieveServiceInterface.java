package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeneralGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.converter.GeometryExporter;
import cc.domovoi.spring.geometry.model.GeoInterLayerContextLike;

import java.util.List;
import java.util.Objects;

public interface GeneralGeometryInterLayerRetrieveServiceInterface<INNER extends GeoInterLayerContextLike<K, OUTER>, OUTER, K, E extends GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER>> extends GeneralGeometryRetrieveServiceInterface<INNER, OUTER, K, E> {

    // , GeometryRetrieveServiceInterface<INNER>

//    @Override
//    default GeometryRetrieveServiceInterface<INNER> geometryService() {
//        throw new UnsupportedOperationException("can't use geometryService()");
//    }

    @Override
    default GeometryExporter<INNER, OUTER> exporter() {
        throw new UnsupportedOperationException("can't use exporter()");
    }

//    @Override
//    default void findGeometryListAndSet(List<E> entityList) {
//        findGeometryListAndSet(entityList, this::tempInner, this::findGeometryList);
//    }

    @Override
    default void exp(E entity) {
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            INNER inner = supplier.get();
            if (Objects.nonNull(inner)) {
                OUTER outer = inner.getOuter();
                entity.geometryOuterSetMap().get(key).accept(outer);
            }
        });
    }
}
