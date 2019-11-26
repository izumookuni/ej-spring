package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeneralGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.joining.GeometryExporterJoiningInterface;
import cc.domovoi.spring.geometry.joining.GeometryRetrieveServiceJoiningInterface;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;
import cc.domovoi.spring.annotation.after.AfterFind;
import cc.domovoi.spring.annotation.after.AfterFindList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface GeneralGeometryRetrieveServiceInterface<INNER extends GeoContextLike<K>, OUTER, K, E extends GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER>> extends GeneralRetrieveJoiningServiceInterface<K, E>, GeometryRetrieveServiceJoiningInterface<INNER>, GeometryExporterJoiningInterface<INNER, OUTER> {

    @AfterFindList(order = -100)
    default void processingJoiningGeometryList(List<E> entity) {
        findGeometryListAndSet(entity);
        entity.forEach(this::exp);
    }

    @AfterFind(order = -100)
    default void processingJoiningGeometry(E entity) {
        processingJoiningGeometryList(Collections.singletonList(entity));
    }

    /**
     * Find geometry data, and attached it to the entity.
     * @param entityList Entity list
     * @param tempInnerSupplier tempInnerSupplier
     * @param function function
     */
    default void findGeometryListAndSet(List<E> entityList, Supplier<? extends INNER> tempInnerSupplier, Function<? super INNER, ? extends List<INNER>> function) {
        if (Objects.isNull(entityList) || entityList.isEmpty()) {
            return;
        }
        List<K> idList = entityList.stream().map(E::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<K, E> entityMap = entityList.stream().collect(Collectors.toMap(E::getId, Function.identity()));
        entityList.get(0).geometryInnerGetMap().keySet().forEach(key -> {
            INNER query = tempInnerSupplier.get();
            query.setContextIdIn(idList);
            query.setContextName(key);
            List<INNER> geometryList = function.apply(query);
            geometryList.forEach(geometry -> {
                E entity = entityMap.get(geometry.getContextId());
                if (Objects.nonNull(entity)) {
                    entity.geometryInnerSetMap().get(key).accept(geometry);
                }
            });
        });
    }

    /**
     * Find geometry data, and attached it to the entity.
     *
     * @param entityList Entity list
     */
    default void findGeometryListAndSet(List<E> entityList) {
        findGeometryListAndSet(entityList, geometryService()::tempInner, geometryService()::findGeometryList);
    }

    /**
     * Convert INNER data to OUTER data.
     *
     * @param entity Entity
     */
    default void exp(E entity) {
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            INNER inner = supplier.get();
            if (Objects.nonNull(inner)) {
                OUTER outer = exporter().exportGeometry(inner);
                entity.geometryOuterSetMap().get(key).accept(outer);
            }
        });
    }


}
