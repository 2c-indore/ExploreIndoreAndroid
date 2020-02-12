package org.kathmandulivinglabs.exploreindore.Helper;

import com.mapbox.mapboxsdk.annotations.Marker;

public class EditAmenityEvent {
    public String lat, longitude, name;

    public EditAmenityEvent(String lat, String longitude, String name) {
        this.lat = lat;
        this.longitude = longitude;
        this.name = name;
    }
}
