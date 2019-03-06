package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.geometry.geointerface.GeometricMultipleInterface;
import cc.domovoi.spring.geometry.geointerface.GeometryMultipleInterface;

/**
 * GeometryMultipleJoiningEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface GeometryMultipleJoiningEntityInterface<INNER, OUTER> extends BaseJoiningEntityInterface, GeometryMultipleInterface<INNER>, GeometricMultipleInterface<OUTER> {
}
