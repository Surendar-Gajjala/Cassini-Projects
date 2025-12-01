package com.cassinisys.platform.util;

/**
 * Created by reddy on 9/30/15.
 */
public class GeoLocation {
    public Double latitude;
    public Double longitude;

    public GeoLocation() {

    }

    public GeoLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
