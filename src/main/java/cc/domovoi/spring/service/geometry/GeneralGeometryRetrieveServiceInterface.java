package cc.domovoi.spring.service.geometry;

import cc.domovoi.spring.entity.geometry.GeneralGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.converter.GeometryExporter;
import cc.domovoi.spring.geometry.model.GeoContextLike;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;
import cc.domovoi.spring.service.annotation.after.AfterFind;
import cc.domovoi.spring.service.annotation.after.AfterFindList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface GeneralGeometryRetrieveServiceInterface<INNER extends GeoContextLike<K>, OUTER, K, E extends GeneralGeometryMultipleJoiningEntityInterface<K, INNER, OUTER>> extends GeneralRetrieveJoiningServiceInterface<K, E> {

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
     *
     * @param entityList Entity list
     */
    default void findGeometryListAndSet(List<E> entityList) {
        if (Objects.isNull(entityList) || entityList.isEmpty()) {
            return;
        }
        List<K> idList = entityList.stream().map(E::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<K, E> entityMap = entityList.stream().collect(Collectors.toMap(E::getId, Function.identity()));
        entityList.get(0).geometryInnerGetMap().keySet().forEach(key -> {
            INNER query = geometryService().tempInner();
            query.setContextIdIn(idList);
            query.setContextName(key);
            List<INNER> geometryList = geometryService().findGeometryList(query);
            geometryList.forEach(geometry -> {
                E entity = entityMap.get(geometry.getContextId());
                if (Objects.nonNull(entity)) {
                    entity.geometryInnerSetMap().get(key).accept(geometry);
                }
            });
        });
    }

    /**
     * Convert INNER data to OUTER data.
     *
     * @param entity Entity
     */
    default void exp(E entity) {
        entity.geometryInnerGetMap().forEach((key, supplier) -> {
            INNER inner = supplier.get();
            OUTER outer = exporter().exportGeometry(inner);
            entity.geometryOuterSetMap().get(key).accept(outer);
        });
    }


}
