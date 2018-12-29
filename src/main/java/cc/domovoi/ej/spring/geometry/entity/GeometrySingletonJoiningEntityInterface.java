package cc.domovoi.ej.spring.geometry.entity;

import cc.domovoi.ej.spring.geometry.geointerface.GeometricSingletonInterface;
import cc.domovoi.ej.spring.geometry.geointerface.GeometrySingletonInterface;

public interface GeometrySingletonJoiningEntityInterface<INNER, OUTER> extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>, GeometrySingletonInterface<INNER>, GeometricSingletonInterface<OUTER> {
}
