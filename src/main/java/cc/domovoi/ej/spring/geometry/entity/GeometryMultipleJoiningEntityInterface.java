package cc.domovoi.ej.spring.geometry.entity;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.geometry.geointerface.GeometricMultipleInterface;
import cc.domovoi.ej.spring.geometry.geointerface.GeometryMultipleInterface;

public interface GeometryMultipleJoiningEntityInterface<INNER, OUTER> extends BaseJoiningEntityInterface, GeometryMultipleInterface<INNER>, GeometricMultipleInterface<OUTER> {
}
