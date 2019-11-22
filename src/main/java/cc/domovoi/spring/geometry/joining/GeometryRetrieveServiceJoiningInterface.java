package cc.domovoi.spring.geometry.joining;

import cc.domovoi.spring.service.geometry.GeometryRetrieveServiceInterface;

public interface GeometryRetrieveServiceJoiningInterface<INNER> {

    /**
     * A service that operate geometry object.
     *
     * @return Geometry service.
     */
    GeometryRetrieveServiceInterface<INNER> geometryService();
}
