package cc.domovoi.spring.mapper.geometry;

import java.util.List;

/**
 * GeometryMapperInterface.
 *
 * @param <INNER> INNER type.
 */
public interface GeometryMapperInterface<INNER> {

    /**
     * Find geometry entity list.
     *
     * @param geometry Query conditions.
     * @return Geometry entity list.
     */
    List<INNER> findGeometryList(INNER geometry);

    /**
     * Find geometry entity.
     *
     * @param geometry Query conditions.
     * @return Geometry entity.
     */
    INNER findGeometry(INNER geometry);

    /**
     * Add geometry entity list.
     *
     * @param geometryList The geometry entity list need to be added.
     * @return The number of successful insert operations.
     */
    Integer addGeometryList(List<INNER> geometryList);

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
