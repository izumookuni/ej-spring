package cc.domovoi.spring.geometry.impl.converter;

import cc.domovoi.spring.geometry.converter.GeometryExporter;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeoPoint;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeoType;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeometricModel;
import cc.domovoi.spring.geometry.impl.latlng.LatitudeLongitude;

import java.util.Collections;
import java.util.List;

public class LatitudeLongitudeExporters {

    public static GeometryExporter<LatitudeLongitude, List<GeometricModel>> LatitudeLongitudePointExporter = new LatitudeLongitudePointExporter();

    private static class LatitudeLongitudePointExporter implements GeometryExporter<LatitudeLongitude, List<GeometricModel>> {
        @Override
        public List<GeometricModel> exportGeometry(LatitudeLongitude inner) {
            if (inner == null) {
                return null;
            }
            return Collections.singletonList(new GeometricModel(GeoType.POINT.getName(), Collections.singletonList(new GeoPoint(inner.getLongitude(), inner.getLatitude()))));
        }
    }
}
