package cc.domovoi.spring.mapper.geometry;

import cc.domovoi.spring.entity.geometry.GeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.mapper.BaseMapperInterface;

/**
 * BaseGeometryMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface BaseGeometryMapperInterface<E extends GeometryMultipleJoiningEntityInterface> extends BaseMapperInterface<E>, BaseGeometryRetrieveMapperInterface<E> {

}
