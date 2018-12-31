package cc.domovoi.geometry.data.converter;

import cc.domovoi.ej.spring.geometry.converter.GeometryExporter;
import cc.domovoi.geometry.data.geometricmodel.GeoPoint;
import cc.domovoi.geometry.data.geometricmodel.GeoType;
import cc.domovoi.geometry.data.geometricmodel.GeometricModel;
import cc.domovoi.geometry.data.lgeometry.LArea;
import cc.domovoi.geometry.data.lgeometry.LGeometryInterface;
import cc.domovoi.geometry.data.lgeometry.LLine;
import cc.domovoi.geometry.data.lgeometry.LPoint;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GeometryExporters {

    public static GeometryExporter<LPoint, List<GeometricModel>> LPointGeometricModelExporter = new LPointGeometricModelExporter();

    public static GeometryExporter<LLine, List<GeometricModel>> LLineGeometricModelExporter = new LLineGeometricModelExporter();

    public static GeometryExporter<LArea, List<GeometricModel>> LLAreaGeometricModelExporter = new LLAreaGeometricModelExporter();

    public static GeometryExporter<LGeometryInterface, List<GeometricModel>> LGeometryGeometricModelExporter = new LGeometryGeometricModelExporter();

    protected static class LPointGeometricModelExporter implements GeometryExporter<LPoint, List<GeometricModel>> {
        @Override
        public List<GeometricModel> exportGeometry(LPoint inner) {
            if (inner == null) {
                return null;
            }
            return Collections.singletonList(new GeometricModel(GeoType.POINT.getName(), Collections.singletonList(new GeoPoint(inner.getX(), inner.getY()))));
        }
    }

    protected static class LLineGeometricModelExporter implements GeometryExporter<LLine, List<GeometricModel>> {
        @Override
        public List<GeometricModel> exportGeometry(LLine inner) {
            if (inner == null) {
                return null;
            }
            List<GeoPoint> points = inner.regularizationPoints().stream().map(point -> new GeoPoint(point.getX(), point.getY())).collect(Collectors.toList());
            return Collections.singletonList(new GeometricModel(GeoType.LINE.getName(), points));
        }
    }

    protected static class LLAreaGeometricModelExporter implements GeometryExporter<LArea, List<GeometricModel>> {
        @Override
        public List<GeometricModel> exportGeometry(LArea inner) {
            if (inner == null) {
                return null;
            }
//            System.out.println(inner.regularizationPoints());
            List<GeoPoint> points = inner.regularizationPoints().stream().map(point -> new GeoPoint(point.getX(), point.getY())).collect(Collectors.toList());
//            System.out.println(points);
            return Collections.singletonList(new GeometricModel(GeoType.POLYGON.getName(), points));
        }
    }

    protected static class LGeometryGeometricModelExporter implements GeometryExporter<LGeometryInterface, List<GeometricModel>> {
        @Override
        public List<GeometricModel> exportGeometry(LGeometryInterface inner) {
            if (inner == null) {
                return null;
            }
            if (inner instanceof LPoint) {
                return Collections.singletonList(new GeometricModel(GeoType.POINT.getName(), Collections.singletonList(new GeoPoint(((LPoint) inner).getX(), ((LPoint) inner).getY()))));
            }
            else if (inner instanceof LLine) {
                List<GeoPoint> points = inner.regularizationPoints().stream().map(point -> new GeoPoint(point.getX(), point.getY())).collect(Collectors.toList());
                return Collections.singletonList(new GeometricModel(GeoType.LINE.getName(), points));
            }
            else if (inner instanceof LArea) {
                List<GeoPoint> points = inner.regularizationPoints().stream().map(point -> new GeoPoint(point.getX(), point.getY())).collect(Collectors.toList());
                return Collections.singletonList(new GeometricModel(GeoType.POLYGON.getName(), points));
            }
            else {
                throw new RuntimeException("unsupported LGeometryInterface");
            }
        }
    }
}
