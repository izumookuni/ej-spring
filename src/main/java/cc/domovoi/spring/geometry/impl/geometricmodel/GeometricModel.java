package cc.domovoi.spring.geometry.impl.geometricmodel;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Spatial data model
 */
public class GeometricModel implements Cloneable, Serializable {

    /**
     * Model Type
     */
    private String geoType;

    /**
     * Point list
     */
    private List<GeoPoint> pointList;

    public GeoType geoType() {
        assert pointList != null && !pointList.isEmpty();
        if ("point".equals(geoType)) {
            return GeoType.POINT;
        }
        else if ("polygon".equals(geoType)) {
            return GeoType.POLYGON;
        }
        else if ("polyline".equals(geoType)) {
            assert pointList.size() > 1;
            boolean flag = pointList.get(0).hashCode() == pointList.get(pointList.size() - 1).hashCode();
            if (pointList.size() > 2 && flag) {
//                return GeoType.POLYGON;
                return GeoType.LINE;
            }
            else if (!flag){
                return GeoType.LINE;
            }
            else {
                // size <= 2 && flag
                throw new RuntimeException("GeoType undefined: " + geoType + " with size <= 2 && flag");
            }
        }
        else {
            throw new RuntimeException("GeoType undefined: " + geoType);
        }
    }

    public List<GeoPoint> reversePoint() {
        List<GeoPoint> tempList = new ArrayList<>(pointList);
        Collections.reverse(tempList);
        return tempList;
    }

    public Boolean anticlockwise() {
        if (geoType() == GeoType.POLYGON) {
            // Find a convex point
            Optional<Integer> indexOption = IntStream.range(0, this.pointList.size()).boxed().max(Comparator.comparing(i -> this.pointList.get(i).getX()));
            Optional<Boolean> result = indexOption.map(index -> {
                int size = this.pointList.size();
                GeoPoint point0 = index == 0 ? this.pointList.get(size - 1) : this.pointList.get(index - 1);
                GeoPoint point1 = this.pointList.get(index);
                GeoPoint point2 = index == size - 1 ? this.pointList.get(0) : this.pointList.get(index + 1);
                // Compute the cross product of vectors
                Double line1X = point1.getX() - point0.getX();
                Double line1Y = point1.getY() - point0.getY();
                Double line2X = point2.getX() - point1.getX();
                Double line2Y = point2.getY() - point1.getY();
                return line1X * line2Y - line1Y * line2X >= 0;
            });
            return result.orElseThrow(() -> new RuntimeException("WTF!!!"));
        }
        else {
            throw new RuntimeException("GeoType isn't POLYGON");
        }
    }

    public GeometricModel regularization() {
        if (anticlockwise()) {
            return this;
        }
        else {
            return new GeometricModel(this.geoType, this.reversePoint());
        }
    }

    public List<Double> rawPointValueList() {
        return this.pointList.stream().flatMap(geoPoint -> geoPoint.rawValueList().stream()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "GeometricModel{" +
                "geoType='" + geoType + '\'' +
                ", pointList=" + pointList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeometricModel that = (GeometricModel) o;
        return Objects.equals(geoType, that.geoType) &&
                Objects.equals(pointList, that.pointList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geoType, pointList);
    }

    public GeometricModel() {
    }

    public GeometricModel(String geoType, List<GeoPoint> pointList) {
        this.geoType = geoType;
        this.pointList = pointList;
    }

    public String getGeoType() {
        return geoType;
    }

    public void setGeoType(String geoType) {
        this.geoType = geoType;
    }

    public List<GeoPoint> getPointList() {
        return pointList;
    }

    public void setPointList(List<GeoPoint> pointList) {
        this.pointList = pointList;
    }
}
