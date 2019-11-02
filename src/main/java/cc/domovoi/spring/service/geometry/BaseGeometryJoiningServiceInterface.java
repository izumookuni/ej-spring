package cc.domovoi.spring.service.geometry;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.geometry.converter.GeometryLoader;
import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseGeometryJoiningServiceInterface.
 *
 * @param <INNER> INNER type.
 * @param <OUTER> OUTER type.
 * @param <E>     Entity type.
 * @param <M>     Mapper type.
 */
@Deprecated
public interface BaseGeometryJoiningServiceInterface<INNER extends GeoContextLike, OUTER, E extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>, M extends StandardMapperInterface<E>>
        extends BaseJoiningServiceInterface<E, M>, BaseGeometryRetrieveJoiningServiceInterface<INNER, OUTER, E, M> {

    /**
     * A geometry loader to load INNER object from OUTER object.
     *
     * @return A geometry loader.
     */
    GeometryLoader<INNER, OUTER> loader();

    /**
     * Add entity function, called by the Controller layer.
     *
     * @param entity The entity need to be added.
     * @return The number of successful insert operations.
     */
    @Override
    default Try<Tuple2<Integer, String>> addEntity(E entity) {
        if (!addCondition(entity)) {
            return new Failure<>(new RuntimeException("do not meet the addCondition"));
        }
        beforeAdd(entity);
        Try<Tuple2<Integer, String>> addResult = Try.apply(() -> {
            Boolean entityExist = checkEntityExist(entity);
            if (entityExist) {
                return new Tuple2<>(0, null);
            }
            imp(entity);
            List<Integer> addGeometryResultList = addGeometryByGeometryService(entity);
            if (addGeometryResultList.stream().anyMatch(i -> i == 0)) {
                throw new RuntimeException(String.format("addGeometryResult is %s", addGeometryResultList));
            }
            Integer result = addEntityByMapper(entity);
            return new Tuple2<>(result, entity.getId());
        });
        afterAdd(entity);
        return addResult;
//        Boolean entityExist = checkEntityExists(entity);
//        if (entityExist) {
//            return 0;
//        }
//        imp(entity);
//        List<Integer> addGeometryResultList = addGeometryByGeometryService(entity);
//        if (addGeometryResultList.stream().anyMatch(i -> i == 0)) {
//            throw new RuntimeException(String.format("addGeometryResult is %s", addGeometryResultList));
//        }
//        return addEntityByMapper(entity);
    }

    /**
     * Update entity function, called by the Controller layer.
     * <p>
     * Geometry data will be update if it's exists.
     *
     * @param entity The entity need to be updated.
     * @return The number of successful update operations.
     */
    @Override
    default Try<Integer> updateEntity(E entity) {
        if (!updateCondition(entity)) {
            return new Failure<>(new RuntimeException("do not meet the updateCondition"));
        }
        beforeUpdate(entity);
        Try<Integer> updateResult = Try.apply(() -> {
            imp(entity);
            List<Integer> updateGeometryResultList = updateGeometryByGeometryService(entity);
            if (updateGeometryResultList.stream().anyMatch(i -> i == 0)) {
                throw new RuntimeException(String.format("updateGeometryResult is %s", updateGeometryResultList));
            }
            return updateEntityByMapper(entity);
        });
        afterUpdate(entity);
        return updateResult;
//        imp(entity);
//        List<Integer> updateGeometryResultList = updateGeometryByGeometryService(entity);
//        if (updateGeometryResultList.stream().anyMatch(i -> i == 0)) {
//            throw new RuntimeException(String.format("updateGeometryResult is %s", updateGeometryResultList));
//        }
//        return updateEntityByMapper(entity);
    }

    /**
     * Delete entity function, called by the Controller layer.
     * <p>
     * If geometry data is not provided, all geometry data and base information will be deleted.
     * If geometry data is not provided，all geometry PROVIDED will be deleted, but base information will NOT be deleted.
     *
     * @param entity The entity need to be deleted.
     * @return The number of successful delete operations.
     */
    @Override
    default Try<Integer> deleteEntity(E entity) {
        if (!deleteCondition(entity)) {
            return new Failure<>(new RuntimeException("do not meet the deleteCondition"));
        }
        beforeDelete(entity);
        Try<Integer> deleteResult = Try.apply(() -> {
//            if (entity.getId() == null) {
//                throw new RuntimeException("id must not be null");
//            }
            imp(entity);
            Tuple2<List<Integer>, Boolean> deleteGeometryResult = deleteGeometryByGeometryService(entity);

            if (deleteGeometryResult.v2()) {
                // Provide geometry
                return -1;
            } else {
                // Does not provide geometry
                return deleteEntityByMapper(entity);
            }
        });
        afterDelete(entity);
        return deleteResult;
//        if (entity.getId() == null) {
//            throw new RuntimeException("id must not be null");
//        }
//        imp(entity);
//        Tuple2<List<Integer>, Boolean> deleteGeometryResult = deleteGeometryByGeometryService(entity);
//
//        if (deleteGeometryResult._2()) {
//            // Provide geometry
//            return -1;
//        } else {
//            // Does not provide geometry
//            return deleteEntityByMapper(entity);
//        }
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
