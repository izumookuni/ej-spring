package cc.domovoi.ej.spring.geometry.service;

import cc.domovoi.ej.collection.tuple.Tuple2;
import cc.domovoi.ej.spring.geometry.converter.GeometryLoader;
import cc.domovoi.ej.spring.geometry.entity.GeometryMultipleJoiningEntityInterface;
import cc.domovoi.ej.spring.geometry.data.GeoContextLike;
import cc.domovoi.ej.spring.geometry.mapper.BaseGeometryMapperInterface;
import cc.domovoi.ej.spring.service.BaseJoiningServiceInterface;

import java.util.ArrayList;
import java.util.List;

public interface BaseGeometryJoiningServiceInterface<INNER extends GeoContextLike, OUTER, E extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>, M extends BaseGeometryMapperInterface<E>> extends BaseJoiningServiceInterface<E, M>, BaseGeometryRetrieveJoiningServiceInterface<INNER, OUTER, E, M> {

    GeometryLoader<INNER, OUTER> loader();

    /**
     * Add entity function, called by the Controller layer.
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    @Override
    default Integer addEntity(E entity) {
        Boolean entityExist = checkEntityExist(entity);
        if (entityExist) {
            return 0;
        }
        imp(entity);
        List<Integer> addGeometryResultList = addGeometryByGeometryService(entity);
        if (addGeometryResultList.stream().anyMatch(i -> i == 0)) {
            throw new RuntimeException(String.format("addGeometryResult is %s", addGeometryResultList));
        }
        return addEntityByMapper(entity);
    }

    /**
     * Update entity function, called by the Controller layer.
     *
     * Geometry data will be update if it's exists.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    @Override
    default Integer updateEntity(E entity) {
        imp(entity);
        List<Integer> updateGeometryResultList = updateGeometryByGeometryService(entity);
        if (updateGeometryResultList.stream().anyMatch(i -> i == 0)) {
            throw new RuntimeException(String.format("updateGeometryResult is %s", updateGeometryResultList));
        }
        return updateEntityByMapper(entity);
    }

    /**
     * Delete entity function, called by the Controller layer.
     *
     * If geometry data is not provided, all geometry data and base information will be deleted.
     * If geometry data is not provided，all geometry PROVIDED will be deleted, but base information will NOT be deleted.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    @Override
    default Integer deleteEntity(E entity) {
        imp(entity);
        Tuple2<List<Integer>, Boolean> deleteGeometryResult = deleteGeometryByGeometryService(entity);

        if (deleteGeometryResult._2()) {
            // 提供geometry
            return -1;
        }
        else {
            // 不提供geometry
            return deleteEntityByMapper(entity);
        }
    }

    /**
     * Add geometry data using geometryService.
     *
     * @param entity The entity need to be added geometry data.
     * @return The number of successful insert operations.
     */
    default List<Integer> addGeometryByGeometryService(E entity) {
        List<Integer> addGeometryResultList = new ArrayList<>();
        entity.geometryGetMap().forEach((key, supplier) -> {
            INNER geometry = supplier.get();
            if (geometry != null) {
                INNER query = geometryService().tempInner();
                query.setContextId(entity.getId());
                query.setContextName(key);
                INNER g = geometryService().findGeometry(query);
                // Repeated additions are not allowed
                if (g == null) {
                    geometry.setContextId(entity.getId());
                    geometry.setContextName(key);
                    Integer addGeometryResult = geometryService().addGeometry(geometry);
                    addGeometryResultList.add(addGeometryResult);
                }
            }
        });
        return addGeometryResultList;
    }

    /**
     * Update geometry data using geometryService.
     * @param entity The entity need to be updated geometry data.
     * @return The number of successful update operations.
     */
    default List<Integer> updateGeometryByGeometryService(E entity) {
        List<Integer> updateGeometryResultList = new ArrayList<>();
        entity.geometryGetMap().forEach((key, supplier) -> {
            INNER geometry = supplier.get();
            if (geometry != null) {
                INNER deleteQuery = geometryService().tempInner();
                deleteQuery.setContextId(entity.getId());
                deleteQuery.setContextName(key);
                geometryService().deleteGeometry(deleteQuery);
                geometry.setContextId(entity.getId());
                geometry.setContextName(key);
                Integer addGeometryResult = geometryService().addGeometry(geometry);
                updateGeometryResultList.add(addGeometryResult);
            }
        });
        return updateGeometryResultList;
    }

    /**
     * Delete geometry data using geometryService.
     * @param entity The entity need to be deleted geometry data.
     * @return (The number list of successful update operations, Whether delete partially or not)
     */
    default Tuple2<List<Integer>, Boolean> deleteGeometryByGeometryService(E entity) {
        List<Integer> deleteGeometryResultList = new ArrayList<>();
        // true -> delete partially; false -> delete overall
        boolean deleteFlag = entity.geometryGetMap().values().stream().anyMatch(g -> g.get() != null);
        entity.geometryGetMap().forEach((key, supplier) -> {
            // If the geometry data corresponding to this key exists, delete it.
            if (!deleteFlag || supplier.get() != null) {
                INNER deleteQuery = geometryService().tempInner();
                deleteQuery.setContextId(entity.getId());
                deleteQuery.setContextName(key);
                Integer deleteGeometryResult = geometryService().deleteGeometry(deleteQuery);
                deleteGeometryResultList.add(deleteGeometryResult);
            }
            else {
                deleteGeometryResultList.add(-1);
            }
        });
        return new Tuple2<>(deleteGeometryResultList, deleteFlag);
    }

    /**
     * Convert OUTER data to INNER data.
     * @param entity Entity
     */
    default void imp(E entity) {
        entity.geometricGetMap().forEach((key, supplier) -> {
            OUTER outer = supplier.get();
            INNER inner = loader().loadGeometry(outer);
            entity.geometrySetMap().get(key).accept(inner);
        });
    }
}
