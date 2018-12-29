package cc.domovoi.ej.spring.geometry.converter;

@FunctionalInterface
public interface GeometryLoader<INNER, OUTER> {

    INNER loadGeometry(OUTER outer);

}
