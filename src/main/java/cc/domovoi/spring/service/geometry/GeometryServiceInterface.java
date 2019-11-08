package cc.domovoi.spring.service.geometry;

import java.util.List;

/**
 * GeometryServiceInterface.
 *
 * @param <INNER> Inner geometry type.
 */
public interface GeometryServiceInterface<INNER> {

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
     * Add geometry entity.
     *
     * @param geometry The geometry entity need to be added.
     * @return The number of successful insert operations.
     */
    Integer addGeometry(INNER geometry);

    /**
     * Update geometry entity.
     *
     * @param geometry The geometry entity need to be updated.
     * @return The number of successful update operations.
     */
    default Integer updateGeometry(INNER geometry) {
        deleteGeometry(geometry);
        return addGeometry(geometry);
    }

    /**
     * Delete geometry entity.
     *
     * @param geometry The geometry entity need to be deleted.
     * @return The number of successful delete operations.
     */
    Integer deleteGeometry(INNER geometry);

    /**
     * Find geometry entity list.
     *
     * @param geometry Query conditions.
     * @return Geometry entity list.
     */
    List<INNER> findGeometryList(INNER geometry);

}
