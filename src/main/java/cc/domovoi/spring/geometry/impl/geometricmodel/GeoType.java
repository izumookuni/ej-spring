package cc.domovoi.spring.geometry.impl.geometricmodel;

public enum GeoType {
    POINT(0, "point"),
    LINE(1, "polyline"),
    POLYGON(2, "polygon")
    ;

    private final int id;

    private final String name;

    GeoType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
