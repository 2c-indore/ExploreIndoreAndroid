package org.kathmandulivinglabs.exploreindore.Realmstore;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Bhawak on 5/30/18.
 */
public class ExploreSchema extends RealmObject {

    private String tag;

    private String name,namein, contact_email, contact_phone, web;
    private String osm_id;

    private Long id;
    private String type;

    private double coordinateslong;
    private double coordinateslat;

    private RealmList<String> tag_type;
    private RealmList<String> tag_lable;
    private Integer capacity_beds, personnel_count;
    private String ward_id, ward_name;

    public RealmList<String> getTag_type() {
        return tag_type;
    }

    public void setTag_type(RealmList<String> tag_type) {
        this.tag_type = tag_type;
    }

    public RealmList<String> getTag_lable() {
        return tag_lable;
    }

    public void setTag_lable(RealmList<String> tag_lable) {
        this.tag_lable = tag_lable;
    }



    public String getNamein() {
        return namein;
    }

    public void setNamein(String namein) {
        this.namein = namein;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOsm_id() {
        return osm_id;
    }

    public void setOsm_id(String osm_id) {
        this.osm_id = osm_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getCapacity_beds() {
        return capacity_beds;
    }

    public void setCapacity_beds(Integer capacity_beds) {
        this.capacity_beds = capacity_beds;
    }

    public Integer getPersonnel_count() {
        return personnel_count;
    }

    public void setPersonnel_count(Integer personnel_count) {
        this.personnel_count = personnel_count;
    }
    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }
}

