package cc.domovoi.geometry.data.geometricmodel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Point
 */
public class GeoPoint implements Cloneable, Serializable {

    /**
     * X-coordinates
     */
    private Double x;

    /**
     * Y-coordinates
     */
    private Double y;

    public List<Double> rawValueList() {
        return Arrays.asList(this.x, this.y);
    }

    public GeoPoint() {
    }

    public GeoPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public GeoPoint(List<Double> list) {
        this.x = list.get(0);
        this.y = list.get(1);
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPoint geoPoint = (GeoPoint) o;
        return Objects.equals(x, geoPoint.x) &&
                Objects.equals(y, geoPoint.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
