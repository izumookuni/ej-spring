package cc.domovoi.spring.geometry.model;

public interface GeoInterLayerContextLike<K, OUTER> extends GeoContextLike<K> {

    OUTER getOuter();

    void setOuter(OUTER outer);
}
