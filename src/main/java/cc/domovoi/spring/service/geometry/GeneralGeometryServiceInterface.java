package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeneralGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.joining.GeometryLoaderJoiningInterface;
import cc.domovoi.spring.geometry.joining.GeometryServiceJoiningInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.annotation.before.BeforeAdd;
import cc.domovoi.spring.annotation.before.BeforeDelete;
import cc.domovoi.spring.annotation.before.BeforeUpdate;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface GeneralGeometryServiceInterface<INNER extends GeoContextLike<K>, OUTER, K, E extends GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER>> extends GeneralGeometryRetrieveServiceInterface<INNER, OUTER, K, E>, GeneralJoiningServiceInterface<K, E>, GeometryServiceJoiningInterface<INNER>, GeometryLoaderJoiningInterface<INNER, OUTER> {

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
     * @param entity The entity need to be added geometry data.
     * @param tempInnerSupplier tempInnerSupplier
     * @param checkGeometryExistsFunction checkGeometryExistsFunction
     * @param addGeometryFunction addGeometryFunction
     * @return The number of successful insert operations.
     */
    default List<Integer> addGeometryByGeometryService(E entity, Supplier<? extends INNER> tempInnerSupplier, Predicate<? super INNER> checkGeometryExistsFunction, Function<? super INNER, ? extends Integer> addGeometryFunction) {
        List<Integer> addGeometryResultList = new ArrayList<>();
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            INNER geometry = supplier.get();
            if (geometry != null) {
                INNER query = tempInnerSupplier.get();
                query.setContextId(entity.getId());
                query.setContextName(key);
                boolean geometryExists = checkGeometryExistsFunction.test(query);
                // Repeated additions are not allowed
                if (!geometryExists) {
                    geometry.setContextId(entity.getId());
                    geometry.setContextName(key);
                    Integer addGeometryResult = addGeometryFunction.apply(geometry);
                    addGeometryResultList.add(addGeometryResult);
                }
            }
        });
        return addGeometryResultList;
    }

    /**
     * Add geometry data using geometryService.
     *
     * @param entity The entity need to be added geometry data.
     * @return The number of successful insert operations.
     */
    default List<Integer> addGeometryByGeometryService(E entity) {
        return addGeometryByGeometryService(entity, geometryService()::tempInner, geometryService()::checkGeometryExists, geometryService()::addGeometry);
    }

    /**
     * Update geometry data using geometryService.
     * @param entity The entity need to be updated geometry data.
     * @param tempInnerSupplier tempInnerSupplier
     * @param deleteGeometryFunction deleteGeometryFunction
     * @param addGeometryFunction addGeometryFunction
     * @return The number of successful update operations.
     */
    default List<Integer> updateGeometryByGeometryService(E entity, Supplier<? extends INNER> tempInnerSupplier, Function<? super INNER, ? extends Integer> deleteGeometryFunction, Function<? super INNER, ? extends Integer> addGeometryFunction) {
        List<Integer> updateGeometryResultList = new ArrayList<>();
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            INNER geometry = supplier.get();
            if (geometry != null) {
                INNER deleteQuery = tempInnerSupplier.get();
                deleteQuery.setContextId(entity.getId());
                deleteQuery.setContextName(key);
                deleteGeometryFunction.apply(deleteQuery);
                geometry.setContextId(entity.getId());
                geometry.setContextName(key);
                Integer addGeometryResult = addGeometryFunction.apply(geometry);
                updateGeometryResultList.add(addGeometryResult);
            }
        });
        return updateGeometryResultList;
    }

    /**
     * Update geometry data using geometryService.
     *
     * @param entity The entity need to be updated geometry data.
     * @return The number of successful update operations.
     */
    default List<Integer> updateGeometryByGeometryService(E entity) {
        return updateGeometryByGeometryService(entity, geometryService()::tempInner, geometryService()::deleteGeometry, geometryService()::addGeometry);
    }

    default Tuple2<List<Integer>, Boolean> deleteGeometryByGeometryService(E entity, Supplier<? extends INNER> tempInnerSupplier, Function<? super INNER, ? extends Integer> deleteGeometryFunction) {
        List<Integer> deleteGeometryResultList = new ArrayList<>();
        // true -> delete partially; false -> delete overall
        boolean deleteFlag = entity.geometryInnerGetMap().values().stream().anyMatch(g -> g.get() != null);
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            // If the geometry data corresponding to this key exists, delete it.
            if (!deleteFlag || supplier.get() != null) {
                INNER deleteQuery = tempInnerSupplier.get();
                deleteQuery.setContextId(entity.getId());
                deleteQuery.setContextName(key);
                Integer deleteGeometryResult = deleteGeometryFunction.apply(deleteQuery);
                deleteGeometryResultList.add(deleteGeometryResult);
            } else {
                deleteGeometryResultList.add(-1);
            }
        });
        return new Tuple2<>(deleteGeometryResultList, deleteFlag);
    }

    /**
     * Delete geometry data using geometryService.
     *
     * @param entity The entity need to be deleted geometry data.
     * @return (The number list of successful update operations, Whether delete partially or not)
     */
    default Tuple2<List<Integer>, Boolean> deleteGeometryByGeometryService(E entity) {
        return deleteGeometryByGeometryService(entity, geometryService()::tempInner, geometryService()::deleteGeometry);
    }

    /**
     * Convert OUTER data to INNER data.
     *
     * @param entity Entity
     */
    default void imp(E entity) {
        entity.geometryOuterGetMap().forEach((key, supplier) -> {
            OUTER outer = supplier.get();
            if (Objects.nonNull(outer)) {
                INNER inner = loader().loadGeometry(outer);
                entity.geometryInnerSetMap().get(key).accept(inner);
            }
        });
    }
}
