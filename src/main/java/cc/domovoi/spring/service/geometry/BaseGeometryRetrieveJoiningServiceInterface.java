package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;
import cc.domovoi.spring.geometry.converter.GeometryExporter;
import cc.domovoi.spring.entity.geometry.GeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.mapper.geometry.BaseGeometryRetrieveMapperInterface;
import cc.domovoi.spring.service.BaseRetrieveJoiningServiceInterface;
import cc.domovoi.spring.service.GeometryServiceInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;

import java.util.Collections;
import java.util.List;

/**
 * BaseGeometryRetrieveJoiningServiceInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 * @param <E>     Entity type.
 * @param <M>     Mapper type.
 */
public interface BaseGeometryRetrieveJoiningServiceInterface<INNER extends GeoContextLike, OUTER, E extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>, M extends BaseGeometryRetrieveMapperInterface<E>>
        extends BaseRetrieveJoiningServiceInterface<E, M> {

    /**
     * A service that operate geometry object.
     *
     * @return Geometry service.
     */
    GeometryServiceInterface<INNER> geometryService();

    /**
     * A geometry exporter to export INNER object to OUTER object.
     *
     * @return A geometry exporter.
     */
    GeometryExporter<INNER, OUTER> exporter();

    /**
     * Find entity function, called by the Controller layer.
     *
     * @param id ID of entity
     * @return Entity
     */
    @Override
    default E findEntity(String id) {
        return findWithJoiningEntity(id, depth(), depthTree());
    }

    /**
     * Find entity list function, called by the Controller layer.
     *
     * @param entity Query conditions.
     * @return Entity list.
     */
    @Override
    default List<E> findList(E entity) {
        return findListWithJoiningEntity(entity, depth(), depthTree());
    }

    /**
     * Find entity by ID, and attached external entity. Also, attached geometry data to this entity.
     *
     * @param id    ID of entity.
     * @param depth Depth of association.
     * @return Entity.
     */
    @Override
    default E findWithJoiningEntity(String id, Integer depth, JoiningDepthTreeLike tree) {
        E e = findByMapper(id);
        if (e != null) {
            findGeometryAndSet(e);
            exp(e);
//            joinEntity(e, depth);
//            joiningEntityByDepth(Collections.singletonList(e), depth);
            if (depth == -1) {
                joinEntityListByTree(Collections.singletonList(e), tree);
            }
            else {
                joiningEntityByDepth(Collections.singletonList(e), depth);
            }
        }
        return e;
    }

    @Override
    default List<E> findWithJoiningEntity(List<String> idList, Integer depth) {
        List<E> entityList = findListUsingIdByMapper(idList);
        entityList.forEach(e -> {
            findGeometryAndSet(e);
            exp(e);
        });
        joiningEntityByDepth(entityList, depth);
        return entityList;
    }

    /**
     * Find entity list, and attached external entity. Also, attached geometry data to this entity.
     *
     * @param entity Query conditions
     * @param depth  Depth of association
     * @return Entity list.
     */
    @Override
    default List<E> findListWithJoiningEntity(E entity, Integer depth, JoiningDepthTreeLike tree) {
        List<E> eList = findListByMapper(entity);
        eList.forEach(this::findGeometryAndSet);
        eList.forEach(this::exp);
//        if (depth > 0) {
//            eList.forEach(e -> joinEntity(e, depth));
//        }
//        joiningEntityByDepth(eList, depth);
        if (depth == -1) {
            joinEntityListByTree(eList, tree);
        }
        else {
            joiningEntityByDepth(eList, depth);
        }
        return eList;
    }

    /**
     * Find geometry data, and attached it to the entity.
     *
     * @param entity Entity
     */
    default void findGeometryAndSet(E entity) {
        entity.geometryGetMap().forEach((key, supplier) -> {
            INNER query = geometryService().tempInner();
            query.setContextId(entity.getId());
            query.setContextName(key);
            INNER geometry = geometryService().findGeometry(query);
            entity.geometrySetMap().get(key).accept(geometry);
        });
    }

    /**
     * Convert INNER data to OUTER data.
     *
     * @param entity Entity
     */
    default void exp(E entity) {
        entity.geometryGetMap().forEach((key, supplier) -> {
            INNER inner = supplier.get();
            OUTER outer = exporter().exportGeometry(inner);
            entity.geometricSetMap().get(key).accept(outer);
        });
    }
}
