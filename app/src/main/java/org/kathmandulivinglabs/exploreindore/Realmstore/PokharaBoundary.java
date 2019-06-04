package org.kathmandulivinglabs.exploreindore.Realmstore;

import io.realm.RealmObject;

/**
 * Created by Bhawak on 7/8/18.
 */
public class PokharaBoundary extends RealmObject {
    private String tag;
    private double coordinateslong;
    private double coordinateslat;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getCoordinateslong() {
        return coordinateslong;
    }

    public void setCoordinateslong(double coordinateslong) {
        this.coordinateslong = coordinateslong;
    }

    public double getCoordinateslat() {
        return coordinateslat;
    }

    public void setCoordinateslat(double coordinateslat) {
        this.coordinateslat = coordinateslat;
    }
}
