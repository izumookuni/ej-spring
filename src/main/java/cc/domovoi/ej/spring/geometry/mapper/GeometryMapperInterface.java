package cc.domovoi.ej.spring.geometry.mapper;

import java.util.List;

public interface GeometryMapperInterface<INNER> {

    List<INNER> findGeometryList(INNER geometry);

    INNER findGeometry(INNER geometry);

    Integer addGeometryList(List<INNER> geometryList);

    Integer addGeometry(INNER geometry);

    Integer deleteGeometry(INNER geometry);

}
