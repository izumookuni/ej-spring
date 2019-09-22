package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.geometry.geointerface.GeometricSingletonInterface;
import cc.domovoi.spring.geometry.geointerface.GeometrySingletonInterface;

public interface StandardGeometrySingletonJoiningEntityInterface<INNER, OUTER> extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>, GeometrySingletonInterface<INNER>, GeometricSingletonInterface<OUTER> {
}
