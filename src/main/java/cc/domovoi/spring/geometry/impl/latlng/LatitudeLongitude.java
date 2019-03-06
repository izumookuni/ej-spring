package cc.domovoi.spring.geometry.impl.latlng;

/**
 * LatitudeLongitude
 */
public class LatitudeLongitude {

    /**
     * longitude
     */
    private Double longitude;

    /**
     * latitude
     */
    private Double latitude;

    public LatitudeLongitude() {
    }

    public LatitudeLongitude(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
