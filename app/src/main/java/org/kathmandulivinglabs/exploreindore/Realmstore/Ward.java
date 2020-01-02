package org.kathmandulivinglabs.exploreindore.Realmstore;

import org.kathmandulivinglabs.exploreindore.Api_helper.Wards;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;
import org.kathmandulivinglabs.exploreindore.models.POI.POIGeometry;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Bhawak on 3/20/2018.
 */

public class Ward extends RealmObject {
    private String name;
    private int number;
    private String name_ne;
    private String osmID;
    private double coordinateslong;
    private double coordinateslat;
    private RealmList<PokharaBoundary> boundry;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public RealmList<PokharaBoundary> getBoundry() {
        return boundry;
    }

    public void setBoundry(RealmList<PokharaBoundary> boundry) {
        this.boundry = boundry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ne() {
        return name_ne;
    }

    public void setName_ne(String name_ne) {
        this.name_ne = name_ne;
    }

    public String getOsmID() {
        return osmID;
    }

    public void setOsmID(String osmID) {
        this.osmID = osmID;
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
