package cc.domovoi.ej.spring.service;

public interface GeometryServiceInterface<INNER> {

    INNER tempInner();

    INNER findGeometry(INNER geometry);

    Integer addGeometry(INNER geometry);

    Integer deleteGeometry(INNER geometry);


}
