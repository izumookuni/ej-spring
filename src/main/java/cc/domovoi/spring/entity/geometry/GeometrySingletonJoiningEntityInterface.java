package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.geometry.geointerface.GeometricSingletonInterface;
import cc.domovoi.spring.geometry.geointerface.GeometrySingletonInterface;

public interface GeometrySingletonJoiningEntityInterface<INNER, OUTER> extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>, GeometrySingletonInterface<INNER>, GeometricSingletonInterface<OUTER> {
}
