package org.kathmandulivinglabs.preparepokhara.Realmstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Created by Bhawak on 3/12/2018.
 */

public class Hospital extends RealmObject {
    private String name,name_en, name_ne, emergency_services, note, opening_hours, contact_email, contact_phone, web, wardid;
    private Integer bed_capacity, personnel_count;
    private Long id;
    private String facility_icu, facility_nicu, facility_ventilator, facility_ambulance, facility_xray, facility_operating_theatre, emergency, operator_type;
    private String type;
    private double coordinateslong;
    private double coordinateslat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ne() {
        return name_ne;
    }

    public void setName_ne(String name_ne) {
        this.name_ne = name_ne;
    }

    public String getEmergency_service() {
        return emergency_services;
    }

    public void setEmergency_service(String emergency_services) {
        this.emergency_services = emergency_services;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getPhone() {
        return contact_phone;
    }

    public void setPhone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getMail() {
        return contact_email;
    }

    public void setMail(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
    }

    public Integer getBed_capacity() {
        return bed_capacity;
    }

    public void setBed_capacity(Integer bed_capacity) {
        this.bed_capacity = bed_capacity;
    }

    public Integer getPersonnel_count() {
        return personnel_count;
    }

    public void setPersonnel_count(Integer personnel_count) {
        this.personnel_count = personnel_count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcu() {
        return facility_icu;
    }

    public void setIcu(String facility_icu) {
        this.facility_icu = facility_icu;
    }

    public String getNicu() {
        return facility_nicu;
    }

    public void setNicu(String facility_nicu) {
        this.facility_nicu = facility_nicu;
    }

    public String getVentilator() {
        return facility_ventilator;
    }

    public void setVentilator(String facility_ventilator) {
        this.facility_ventilator = facility_ventilator;
    }

    public String getAmbulance() {
        return facility_ambulance;
    }

    public void setAmbulance(String facility_ambulance) {
        this.facility_ambulance = facility_ambulance;
    }

    public String getXray() {
        return facility_xray;
    }

    public void setXray(String facility_xray) {
        this.facility_xray = facility_xray;
    }

    public String getOt() {
        return facility_operating_theatre;
    }

    public void setOt(String facility_operating_theatre) {
        this.facility_operating_theatre = facility_operating_theatre;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator_type() {
        return operator_type;
    }

    public void setOperator_type(String operator_type) {
        this.operator_type = operator_type;
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

    public void setCoordinateslat(float coordinateslat) {
        this.coordinateslat = coordinateslat;
    }

}
