package org.kathmandulivinglabs.exploreindore.models.POI;

import java.util.List;

import io.realm.RealmObject;

public class POIGeometry {
    public String type;
    public List<Double> coordinates;

    public POIGeometry(String type, List<Double> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

}
