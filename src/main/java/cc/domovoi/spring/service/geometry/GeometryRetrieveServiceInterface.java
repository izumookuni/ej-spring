package cc.domovoi.spring.service.geometry;

import java.util.List;

public interface GeometryRetrieveServiceInterface<INNER> {

    /**
     * An empty Inner geometry, used to create a query request
     *
     * @return An empty Inner geometry
     */
    INNER tempInner();

    /**
     * Find geometry entity.
     *
     * @param geometry Query conditions.
     * @return Geometry entity.
     */
    INNER findGeometry(INNER geometry);

    /**
     * Find geometry entity list.
     *
     * @param geometry Query conditions.
     * @return Geometry entity list.
     */
    List<INNER> findGeometryList(INNER geometry);
}
