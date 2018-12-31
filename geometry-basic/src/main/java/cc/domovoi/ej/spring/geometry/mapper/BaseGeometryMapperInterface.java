package cc.domovoi.ej.spring.geometry.mapper;

import cc.domovoi.ej.spring.geometry.entity.GeometryMultipleJoiningEntityInterface;
import cc.domovoi.ej.spring.mapper.BaseMapperInterface;

/**
 * BaseGeometryMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface BaseGeometryMapperInterface<E extends GeometryMultipleJoiningEntityInterface> extends BaseMapperInterface<E>, BaseGeometryRetrieveMapperInterface<E> {

}
