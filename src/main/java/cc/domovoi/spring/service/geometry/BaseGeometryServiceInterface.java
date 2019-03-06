package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeometryMultipleEntityInterface;
import cc.domovoi.spring.mapper.geometry.BaseGeometryMapperInterface;
import cc.domovoi.spring.service.BaseServiceInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;

import java.util.List;

/**
 * BaseGeometryServiceInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 * @param <E>     Entity type.
 * @param <M>     Mapper type.
 */
public interface BaseGeometryServiceInterface<INNER extends GeoContextLike, OUTER, E extends GeometryMultipleEntityInterface<INNER, OUTER>, M extends BaseGeometryMapperInterface<E>>
        extends BaseServiceInterface<E, M>, BaseGeometryRetrieveServiceInterface<INNER, OUTER, E, M>, BaseGeometryJoiningServiceInterface<INNER, OUTER, E, M> {

    @Override
    default E findEntity(String id) {
        E e = findByMapper(id);
        if (e != null) {
            findGeometryAndSet(e);
            exp(e);
        }
        return e;
    }

    @Override
    default List<E> findList(E entity) {
        List<E> entityList = findListByMapper(entity);
        entityList.forEach(this::findGeometryAndSet);
        entityList.forEach(this::exp);
        return entityList;
    }
}
