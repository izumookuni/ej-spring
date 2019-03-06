package cc.domovoi.spring.geometry.impl.converter;

import cc.domovoi.spring.geometry.converter.GeometryLoader;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeoPoint;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeoType;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeometricModel;
import cc.domovoi.spring.geometry.impl.lgeometry.LArea;
import cc.domovoi.spring.geometry.impl.lgeometry.LGeometryInterface;
import cc.domovoi.spring.geometry.impl.lgeometry.LLine;
import cc.domovoi.spring.geometry.impl.lgeometry.LPoint;

import java.util.List;
import java.util.stream.Collectors;

public class GeometryLoaders {

    public static GeometryLoader<LPoint, List<GeometricModel>> LPointGeometricModelLoader = new LPointGeometricModelLoader();

    public static GeometryLoader<LLine, List<GeometricModel>> LLineGeometricModelLoader = new LLineGeometricModelLoader();

    public static GeometryLoader<LArea, List<GeometricModel>> LAreaGeometricModelLoader = new LAreaGeometricModelLoader();

    public static GeometryLoader<LGeometryInterface, List<GeometricModel>> LGeometryGeometricModelLoader = new LGeometryGeometricModelLoader();

    protected static class LPointGeometricModelLoader implements GeometryLoader<LPoint, List<GeometricModel>> {
        @Override
        public LPoint loadGeometry(List<GeometricModel> outer) {
            if (outer == null || outer.isEmpty()) {
                return null;
            }
            if (outer.size() == 1 ) {
                GeometricModel geometricModel = outer.get(0);
                if (geometricModel.geoType() == GeoType.POINT) {
                    if (geometricModel.getPointList().size() == 1) {
                        GeoPoint geoPoint = geometricModel.getPointList().get(0);
                        return new LPoint(geoPoint.getX(), geoPoint.getY());
                    }
                    else {
                        throw new RuntimeException("GeoPoint size no equals 1");
                    }
                }
                else {
                    throw new RuntimeException("geoType no equals POINT");
                }
            }
            else {
                throw new RuntimeException("GeometricModel size no equals 1");
            }
        }
    }

    protected static class LLineGeometricModelLoader implements GeometryLoader<LLine, List<GeometricModel>> {
        @Override
        public LLine loadGeometry(List<GeometricModel> outer) {
            if (outer == null || outer.isEmpty()) {
                return null;
            }
            if (outer.size() == 1 ) {
                GeometricModel geometricModel = outer.get(0);
                if (geometricModel.geoType() == GeoType.LINE) {
                    List<GeoPoint> geoPoints = geometricModel.getPointList();
                    List<LPoint> points = genLPointListFromGeoPointList(geoPoints);
                    return new LLine(points);
                }
                else {
                    throw new RuntimeException("geoType no equals LINE");
                }
            }
            else {
                throw new RuntimeException("GeometricModel size no equals 1");
            }
        }
    }

    protected static class LAreaGeometricModelLoader implements GeometryLoader<LArea, List<GeometricModel>> {
        @Override
        public LArea loadGeometry(List<GeometricModel> outer) {
            if (outer == null || outer.isEmpty()) {
                return null;
            }
            if (outer.size() == 1 ) {
                GeometricModel geometricModel = outer.get(0);
                if (geometricModel.geoType() == GeoType.POLYGON) {
                    List<GeoPoint> geoPoints = geometricModel.getPointList();
                    List<LPoint> points = genLPointListFromGeoPointList(geoPoints);
                    return new LArea(points);
                }
                else {
                    throw new RuntimeException("geoType no equals POLYGON");
                }
            }
            else {
                throw new RuntimeException("GeometricModel size no equals 1");
            }
        }
    }

    private static class LGeometryGeometricModelLoader implements GeometryLoader<LGeometryInterface, List<GeometricModel>> {
        @Override
        public LGeometryInterface loadGeometry(List<GeometricModel> outer) {
            if (outer == null || outer.isEmpty()) {
                return null;
            }
            if (outer.size() == 1 ) {
                GeometricModel geometricModel = outer.get(0);
                if (geometricModel.geoType() == GeoType.POINT) {
                    if (geometricModel.getPointList().size() == 1) {
                        GeoPoint geoPoint = geometricModel.getPointList().get(0);
                        return new LPoint(geoPoint.getX(), geoPoint.getY());
                    }
                    else {
                        throw new RuntimeException("GeoPoint size no equals 1");
                    }
                }
                else if (geometricModel.geoType() == GeoType.LINE) {
                    List<GeoPoint> geoPoints = geometricModel.getPointList();
                    List<LPoint> points = genLPointListFromGeoPointList(geoPoints);
                    return new LLine(points);
                }
                else if (geometricModel.geoType() == GeoType.POLYGON) {
                    List<GeoPoint> geoPoints = geometricModel.getPointList();
                    List<LPoint> points = genLPointListFromGeoPointList(geoPoints);
                    return new LArea(points);
                }
                else {
                    throw new RuntimeException("undefined GeoType");
                }
            }
            else {
                throw new RuntimeException("GeometricModel size no equals 1");
            }
        }
    }

    protected static List<LPoint> genLPointListFromGeoPointList(List<GeoPoint> geoPoints) {
        return geoPoints.stream().map(point -> {
            LPoint lPoint = new LPoint();
            lPoint.setX(point.getX());
            lPoint.setY(point.getY());
            return lPoint;
        }).collect(Collectors.toList());
    }
}
