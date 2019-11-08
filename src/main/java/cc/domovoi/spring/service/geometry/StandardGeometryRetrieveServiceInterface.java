package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import cc.domovoi.spring.service.StandardRetrieveJoiningServiceInterface;

public interface StandardGeometryRetrieveServiceInterface<INNER extends GeoContextLike<String>, OUTER, E extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>> extends GeneralGeometryRetrieveServiceInterface<INNER, OUTER, String, E>, StandardRetrieveJoiningServiceInterface<E> {
}
