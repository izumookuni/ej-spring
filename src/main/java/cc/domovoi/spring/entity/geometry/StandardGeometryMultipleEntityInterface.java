package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

/**
 * GeometryMultipleEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface StandardGeometryMultipleEntityInterface<INNER, OUTER> extends StandardSingletonEntityInterface, StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER> {

}
