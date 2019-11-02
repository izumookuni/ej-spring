package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.geometry.geointerface.GeometrySingletonOuterInterface;
import cc.domovoi.spring.geometry.geointerface.GeometrySingletonInnerInterface;

public interface StandardGeometrySingletonJoiningEntityInterface<INNER, OUTER> extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>, GeometrySingletonInnerInterface<INNER>, GeometrySingletonOuterInterface<OUTER> {
}
