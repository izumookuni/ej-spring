package cc.domovoi.spring.mapper.geometry;

import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;

/**
 * BaseGeometryMapperInterface.
 *
 * @param <E> Entity type.
 */
@Deprecated
public interface StandardGeometryMapperInterface<E extends StandardGeometryMultipleJoiningEntityInterface> extends StandardMapperInterface<E>, StandardGeometryRetrieveMapperInterface<E> {

}
