package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.geometry.geointerface.GeometryMultipleOuterInterface;
import cc.domovoi.spring.geometry.geointerface.GeometryMultipleInnerInterface;

/**
 * GeneralGeometryMultipleJoiningEntityInterface.
 *
 * @param <K> Key type.
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER> extends GeneralJoiningEntityInterface<K>, GeometryMultipleInnerInterface<INNER>, GeometryMultipleOuterInterface<OUTER> {
}
