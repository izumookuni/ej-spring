package cc.domovoi.geometry.model;

/**
 * GeoLike.
 *
 * @param <T> Geo type.
 */
public interface GeoLike<T> {

    /**
     * Geo object.
     *
     * @return Geo object.
     */
    T geo();

    /**
     * Set geo object.
     *
     * @param geo Geo object.
     */
    void geo(T geo);


}
