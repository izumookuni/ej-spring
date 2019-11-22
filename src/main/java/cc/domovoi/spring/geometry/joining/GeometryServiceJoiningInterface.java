package cc.domovoi.spring.geometry.joining;

import cc.domovoi.spring.service.geometry.GeometryServiceInterface;

public interface GeometryServiceJoiningInterface<INNER> extends GeometryRetrieveServiceJoiningInterface<INNER> {

    @Override
    GeometryServiceInterface<INNER> geometryService();
}
