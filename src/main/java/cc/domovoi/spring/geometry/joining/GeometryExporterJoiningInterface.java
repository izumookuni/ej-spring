package cc.domovoi.spring.geometry.joining;

import cc.domovoi.spring.geometry.converter.GeometryExporter;

public interface GeometryExporterJoiningInterface<INNER, OUTER> {

    /**
     * A geometry exporter to export INNER object to OUTER object.
     *
     * @return A geometry exporter.
     */
    GeometryExporter<INNER, OUTER> exporter();
}
