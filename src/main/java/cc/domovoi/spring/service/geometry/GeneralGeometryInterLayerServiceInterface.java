package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeneralGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.converter.GeometryLoader;
import cc.domovoi.spring.geometry.model.GeoInterLayerContextLike;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;

public interface GeneralGeometryInterLayerServiceInterface<INNER extends GeoInterLayerContextLike<K, OUTER>, OUTER, K, E extends GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER>> extends GeneralGeometryInterLayerRetrieveServiceInterface<INNER, OUTER, K, E>, GeneralGeometryServiceInterface<INNER, OUTER, K, E> {

    // , GeometryServiceInterface<INNER>

//    @Override
//    default GeometryServiceInterface<INNER> geometryService(){
//        throw new UnsupportedOperationException("can't use geometryService()");
//    }

    @Override
    default GeometryLoader<INNER, OUTER> loader() {
        throw new UnsupportedOperationException("can't use loader()");
    }

//    @Override
//    default List<Integer> addGeometryByGeometryService(E entity) {
//        return addGeometryByGeometryService(entity, this::tempInner, this::checkGeometryExists, this::addGeometry);
//    }
//
//    @Override
//    default List<Integer> updateGeometryByGeometryService(E entity) {
//        return updateGeometryByGeometryService(entity, this::tempInner, this::deleteGeometry, this::addGeometry);
//    }
//
//    @Override
//    default Tuple2<List<Integer>, Boolean> deleteGeometryByGeometryService(E entity) {
//        return deleteGeometryByGeometryService(entity, this::tempInner, this::deleteGeometry);
//    }

    @Override
    default void imp(E entity) {
        entity.geometryOuterGetMap().forEach((key, supplier) -> {
            OUTER outer = supplier.get();
            INNER inner = geometryService().tempInner();
            inner.setOuter(outer);
            entity.geometryInnerSetMap().get(key).accept(inner);
        });
    }
}
