package cc.domovoi.spring.entity.geometry;

import cc.domovoi.spring.entity.BaseEntityInterface;

/**
 * GeometryMultipleEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface GeometryMultipleEntityInterface<INNER, OUTER> extends BaseEntityInterface, GeometryMultipleJoiningEntityInterface<INNER, OUTER> {

}
