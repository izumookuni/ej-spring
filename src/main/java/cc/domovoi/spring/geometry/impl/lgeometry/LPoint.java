package cc.domovoi.spring.geometry.impl.lgeometry;

import cc.domovoi.spring.geometry.model.PointLike;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LPoint implements PointLike<LPoint>, LGeometryInterface, Serializable, Cloneable {

    private Double x;

    private Double y;

    /**
     * Order
     */
    private Integer idx;

    /**
     * id
     */
    @JsonIgnore
    private String id;

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

    /**
     * The type of graph that is composed of that point
     * 1: point; 2: line; 3: polygon
     */
    @JsonIgnore
    private Integer geoType;

    /**
     * Associated entity ID List for query.
     */
    @JsonIgnore
    private List<String> contextIdIn;

//    @Override
//    public String toString() {
//        return "LPoint{" +
//                "x=" + x +
//                ", y=" + y +
//                ", idx=" + idx +
//                '}';
//    }

    @Override
    public String toString() {
        return "LPoint{" +
                "x=" + x +
                ", y=" + y +
                ", idx=" + idx +
                ", id='" + id + '\'' +
                ", contextId='" + contextId + '\'' +
                ", contextName='" + contextName + '\'' +
                ", geoId='" + geoId + '\'' +
                ", geoType=" + geoType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LPoint lPoint = (LPoint) o;
        return Objects.equals(x, lPoint.x) &&
                Objects.equals(y, lPoint.y) &&
                Objects.equals(idx, lPoint.idx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, idx);
    }

    public LPoint() {
    }

    public LPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.geoId = UUID.randomUUID().toString();
        initPoints(1);
    }

    public LPoint(Double x, Double y, String contextId) {
        this.x = x;
        this.y = y;
        this.contextId = contextId;
        this.contextName = "geometry";
        initPoints(1);
    }

    public LPoint(Double x, Double y, String contextId, String contextName) {
        this.x = x;
        this.y = y;
        this.contextId = contextId;
        this.contextName = contextName;
        initPoints(1);
    }

    @Override
    public void geo(LPoint geo) {
        this.x = geo.getX();
        this.y = geo.getY();
        this.idx = geo.getIdx();
    }

    @Override
    public LPoint geo() {
        return this;
    }

    @Override
    public List<LPoint> getPoints() {
        return Collections.singletonList(this);
    }

    @Override
    public void setPoints(List<LPoint> points) {
        points.stream().findFirst().ifPresent(point -> {
            this.x = point.x;
            this.y = point.y;
        });
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

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getContextId() {
        return contextId;
    }

    @Override
    public String getContextName() {
        return contextName;
    }

    @Override
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    @Override
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @Override
    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public Integer getGeoType() {
        return geoType;
    }

    public void setGeoType(Integer geoType) {
        this.geoType = geoType;
    }

    @Override
    public List<String> getContextIdIn() {
        return contextIdIn;
    }

    @Override
    public void setContextIdIn(List<String> contextIdIn) {
        this.contextIdIn = contextIdIn;
    }
}
