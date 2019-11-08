package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;

public interface StandardGeometryServiceInterface<INNER extends GeoContextLike<String>, OUTER, E extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>> extends StandardGeometryRetrieveServiceInterface<INNER, OUTER, E>, GeneralGeometryServiceInterface<INNER, OUTER, String, E>, StandardJoiningServiceInterface<E> {
}
