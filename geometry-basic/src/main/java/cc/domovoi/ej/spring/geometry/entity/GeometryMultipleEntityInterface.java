package cc.domovoi.ej.spring.geometry.entity;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;

/**
 * GeometryMultipleEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface GeometryMultipleEntityInterface<INNER, OUTER> extends BaseEntityInterface, GeometryMultipleJoiningEntityInterface<INNER, OUTER> {

}
