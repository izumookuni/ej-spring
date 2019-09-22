package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.geometry.geointerface.GeometricMultipleInterface;
import cc.domovoi.spring.geometry.geointerface.GeometryMultipleInterface;

/**
 * GeometryMultipleJoiningEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER> extends StandardJoiningEntityInterface, GeneralGeometryMultipleJoiningEntityInterface<String, INNER, OUTER> {
}
