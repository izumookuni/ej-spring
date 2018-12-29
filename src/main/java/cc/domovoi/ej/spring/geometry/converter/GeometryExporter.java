package cc.domovoi.ej.spring.geometry.converter;

@FunctionalInterface
public interface GeometryExporter<INNER, OUTER> {

    OUTER exportGeometry(INNER inner);
}
