package cc.domovoi.geometry.data.lgeometry;

import cc.domovoi.geometry.model.PolygonLike;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LArea implements PolygonLike<LArea>, LGeometryInterface, Serializable, Cloneable {

    private List<LPoint> points;

    /**
     * Associated entity ID
     */
    @JsonIgnore
    private String contextId;

    /**
     * Associated graph key
     */
    @JsonIgnore
    private String contextName;

    /**
     * Graph id.
     *
     * Used to distinguish between different graphics.
     *
     * Points with the same geoId belong to the same point/line/polygon.
     */
    @JsonIgnore
    private String geoId;

    @Override
    public String toString() {
        return "LArea{" +
                "points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LArea lArea = (LArea) o;
        return Objects.equals(points, lArea.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    public LArea() {
    }

    public LArea(List<LPoint> points) {
        this.points = points;
        this.geoId = UUID.randomUUID().toString();
        initPoints(3);
    }

    public LArea(List<LPoint> points, String contextId) {
        this.points = points;
        this.geoId = UUID.randomUUID().toString();
        this.contextId = contextId;
        this.contextName = "geometry";
        initPoints(3);
    }

    public LArea(List<LPoint> points, String contextId, String contextName) {
        this.points = points;
        this.geoId = UUID.randomUUID().toString();
        this.contextId = contextId;
        this.contextName = contextName;
        initPoints(3);
    }

    @Override
    public List<LPoint> getPoints() {
        return points;
    }

    @Override
    public void setPoints(List<LPoint> points) {
        this.points = points;
    }

    @Override
    public void geo(LArea geo) {
        this.setPoints(geo.getPoints());
    }

    @Override
    public String getContextId() {
        return contextId;
    }

    @Override
    public void setContextId(String contextId) {
        this.contextId = contextId;
        putContextId(contextId);
    }

    @Override
    public String getContextName() {
        return contextName;
    }

    @Override
    public void setContextName(String contextName) {
        this.contextName = contextName;
        putContextName(contextName);
    }

    @Override
    public LArea geo() {
        return this;
    }

    @Override
    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }
}
