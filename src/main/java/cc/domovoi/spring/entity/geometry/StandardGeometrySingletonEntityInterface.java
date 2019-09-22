package cc.domovoi.spring.entity.geometry;

/**
 * GeometrySingletonEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface StandardGeometrySingletonEntityInterface<INNER, OUTER> extends StandardGeometryMultipleEntityInterface<INNER, OUTER>, StandardGeometrySingletonJoiningEntityInterface<INNER, OUTER> {

}
