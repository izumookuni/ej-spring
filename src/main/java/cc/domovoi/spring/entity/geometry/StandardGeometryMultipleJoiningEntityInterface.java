package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

/**
 * GeometryMultipleJoiningEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER> extends StandardJoiningEntityInterface, GeneralGeometryMultipleJoiningEntityInterface<String, INNER, OUTER> {
}
