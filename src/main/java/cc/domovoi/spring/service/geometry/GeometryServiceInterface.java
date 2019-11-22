package cc.domovoi.spring.service.geometry;

import java.util.Objects;

/**
 * GeometryServiceInterface.
 *
 * @param <INNER> Inner geometry type.
 */
public interface GeometryServiceInterface<INNER> extends GeometryRetrieveServiceInterface<INNER> {

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
     * Check whether geometry exists
     * @param geometry The geometry entity need to be checked.
     * @return Whether Geometry Exists
     */
    default Boolean checkGeometryExists(INNER geometry) {
        return Objects.nonNull(findGeometry(geometry));
    }
}
