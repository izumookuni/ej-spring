package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeneralGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.converter.GeometryLoader;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.service.annotation.before.BeforeAdd;
import cc.domovoi.spring.service.annotation.before.BeforeDelete;
import cc.domovoi.spring.service.annotation.before.BeforeUpdate;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;

public interface GeneralGeometryServiceInterface<INNER extends GeoContextLike<K>, OUTER, K, E extends GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER>> extends GeneralGeometryRetrieveServiceInterface<INNER, OUTER, K, E>, GeneralJoiningServiceInterface<K, E> {

    /**
     * A geometry loader to load INNER object from OUTER object.
     *
     * @return A geometry loader.
     */
    GeometryLoader<INNER, OUTER> loader();

    @BeforeAdd(order = -100)
    default void processingAddGemetry(E entity) {
        imp(entity);
        addGeometryByGeometryService(entity);
    }

    @BeforeUpdate(order = -100)
    default void processingUpdateGemetry(E entity) {
        imp(entity);
        updateGeometryByGeometryService(entity);
    }

    @BeforeDelete(order = -100)
    default void processingDeleteGemetry(E entity) {
        imp(entity);
        deleteGeometryByGeometryService(entity);
    }

    /**
     * Add geometry data using geometryService.
     *
     * @param entity The entity need to be added geometry data.
     * @return The number of successful insert operations.
     */
    default List<Integer> addGeometryByGeometryService(E entity) {
        List<Integer> addGeometryResultList = new ArrayList<>();
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
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
     *
     * @param entity The entity need to be updated geometry data.
     * @return The number of successful update operations.
     */
    default List<Integer> updateGeometryByGeometryService(E entity) {
        List<Integer> updateGeometryResultList = new ArrayList<>();
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
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
     *
     * @param entity The entity need to be deleted geometry data.
     * @return (The number list of successful update operations, Whether delete partially or not)
     */
    default Tuple2<List<Integer>, Boolean> deleteGeometryByGeometryService(E entity) {
        List<Integer> deleteGeometryResultList = new ArrayList<>();
        // true -> delete partially; false -> delete overall
        boolean deleteFlag = entity.geometryInnerGetMap().values().stream().anyMatch(g -> g.get() != null);
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            // If the geometry data corresponding to this key exists, delete it.
            if (!deleteFlag || supplier.get() != null) {
                INNER deleteQuery = geometryService().tempInner();
                deleteQuery.setContextId(entity.getId());
                deleteQuery.setContextName(key);
                Integer deleteGeometryResult = geometryService().deleteGeometry(deleteQuery);
                deleteGeometryResultList.add(deleteGeometryResult);
            } else {
                deleteGeometryResultList.add(-1);
            }
        });
        return new Tuple2<>(deleteGeometryResultList, deleteFlag);
    }

    /**
     * Convert OUTER data to INNER data.
     *
     * @param entity Entity
     */
    default void imp(E entity) {
        entity.geometryOuterGetMap().forEach((key, supplier) -> {
            OUTER outer = supplier.get();
            INNER inner = loader().loadGeometry(outer);
            entity.geometryInnerSetMap().get(key).accept(inner);
        });
    }
}
