package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.model.GeoInterLayerContextLike;

public interface StandardGeometryInterLayerRetrieveServiceInterface<INNER extends GeoInterLayerContextLike<String, OUTER>, OUTER, E extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>> extends GeneralGeometryInterLayerRetrieveServiceInterface<INNER, OUTER, String, E>, StandardGeometryRetrieveServiceInterface<INNER, OUTER, E> {
}
