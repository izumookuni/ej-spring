package cc.domovoi.spring.service;

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
     * Delete geometry entity.
     *
     * @param geometry The geometry entity need to be deleted.
     * @return The number of successful delete operations.
     */
    Integer deleteGeometry(INNER geometry);


}
