package cc.domovoi.spring.entity.geometry;

/**
 * GeometrySingletonEntityInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 */
public interface GeometrySingletonEntityInterface<INNER, OUTER> extends GeometryMultipleEntityInterface<INNER, OUTER>, GeometrySingletonJoiningEntityInterface<INNER, OUTER> {

}
