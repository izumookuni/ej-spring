package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.model.GeoInterLayerContextLike;

public interface StandardGeometryInterLayerServiceInterface<INNER extends GeoInterLayerContextLike<String, OUTER>, OUTER, E extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>> extends StandardGeometryInterLayerRetrieveServiceInterface<INNER, OUTER, E>, GeneralGeometryInterLayerServiceInterface<INNER, OUTER, String, E>, StandardGeometryServiceInterface<INNER, OUTER, E> {

//    @Override
//    default GeometryServiceInterface<INNER> geometryService() {
//        throw new UnsupportedOperationException("can't use geometryService()");
//    }
}
