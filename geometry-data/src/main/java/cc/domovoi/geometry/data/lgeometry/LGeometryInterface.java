package cc.domovoi.geometry.data.lgeometry;

import cc.domovoi.geometry.model.GeoContextLike;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface LGeometryInterface extends GeoContextLike {

    List<LPoint> getPoints();

    void setPoints(List<LPoint> points);

    String getGeoId();

    String getContextId();

    String getContextName();

    default List<LPoint> regularizationPoints() {
        return getPoints().stream().sorted(Comparator.comparingInt(LPoint::getIdx)).collect(Collectors.toList());
    }

    default void initPoints(Integer geoType) {
        if (getPoints() != null) {
            if (getPoints().stream().allMatch(point -> point.getIdx() != null)) {
                getPoints().sort(Comparator.comparingInt(LPoint::getIdx));
            }
            IntStream.range(0, getPoints().size()).forEach(i -> {
                LPoint point = getPoints().get(i);
                point.setIdx(i);
                point.setId(UUID.randomUUID().toString());
                point.setGeoType(geoType);
                point.setGeoId(getGeoId());
                point.setContextId(getContextId());
                point.setContextName(getContextName());
            });
        }
    }

    default void putContext(String contextId, String contextName) {
        if (getPoints() != null) {
            getPoints().forEach(point -> {
                point.setContextId(contextId);
                point.setContextName(contextName);
            });
        }
    }

    default void putContextId(String contextId) {
        if (getPoints() != null) {
            getPoints().forEach(point -> {
                point.setContextId(contextId);
            });
        }
    }

    default void putContextName(String contextName) {
        if (getPoints() != null) {
            getPoints().forEach(point -> {
                point.setContextName(contextName);
            });
        }
    }

    default Optional<LPoint> corePoint() {
        if (getPoints() != null && !getPoints().isEmpty()) {
            OptionalDouble avgX = getPoints().stream().mapToDouble(LPoint::getX).average();
            OptionalDouble avgY = getPoints().stream().mapToDouble(LPoint::getY).average();
            if (avgX.isPresent() && avgY.isPresent()) {
                return Optional.of(new LPoint(avgX.getAsDouble(), avgY.getAsDouble()));
            }
            else {
                return Optional.empty();
            }
        }
        else {
            return Optional.empty();
        }
    }
}
