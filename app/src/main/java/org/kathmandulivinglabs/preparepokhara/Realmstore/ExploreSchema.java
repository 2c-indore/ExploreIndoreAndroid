package org.kathmandulivinglabs.preparepokhara.Realmstore;

import io.realm.RealmObject;

/**
 * Created by Bhawak on 5/30/18.
 */
public class ExploreSchema extends RealmObject {
    private String amenity,tag;
    private String name,name_ne, emergency_services, note, opening_hours, contact_email, contact_phone, web, wardid;
    private String osm_id;
    private Integer capacity_beds, personnel_count,stars,rooms,beds;
    private Long id;
    private String facility_icu, facility_nicu, facility_ventilator, facility_ambulance, facility_xray, facility_operating_theatre, emergency, operator_type;
    private String type,nrb_class,atm;
    private double coordinateslong;
    private double coordinateslat;
    private Double frequency;
    private String network;
    private String healthcare_speciality;
    private String drinking_water, toilet;
//    private String class_range;
//    private String level;
    private Integer student_count;

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
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

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
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

    public String getEmergency_services() {
        return emergency_services;
    }

    public void setEmergency_services(String emergency_services) {
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

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacility_icu() {
        return facility_icu;
    }

    public void setFacility_icu(String facility_icu) {
        this.facility_icu = facility_icu;
    }

    public String getFacility_nicu() {
        return facility_nicu;
    }

    public void setFacility_nicu(String facility_nicu) {
        this.facility_nicu = facility_nicu;
    }

    public String getFacility_ventilator() {
        return facility_ventilator;
    }

    public void setFacility_ventilator(String facility_ventilator) {
        this.facility_ventilator = facility_ventilator;
    }

    public String getFacility_ambulance() {
        return facility_ambulance;
    }

    public void setFacility_ambulance(String facility_ambulance) {
        this.facility_ambulance = facility_ambulance;
    }

    public String getFacility_xray() {
        return facility_xray;
    }

    public void setFacility_xray(String facility_xray) {
        this.facility_xray = facility_xray;
    }

    public String getFacility_operating_theatre() {
        return facility_operating_theatre;
    }

    public void setFacility_operating_theatre(String facility_operating_theatre) {
        this.facility_operating_theatre = facility_operating_theatre;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getOperator_type() {
        return operator_type;
    }

    public void setOperator_type(String operator_type) {
        this.operator_type = operator_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNrb_class() {
        return nrb_class;
    }

    public void setNrb_class(String nrb_class) {
        this.nrb_class = nrb_class;
    }

    public String getAtm() {
        return atm;
    }

    public void setAtm(String atm) {
        this.atm = atm;
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

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getHealthcare_speciality() {
        return healthcare_speciality;
    }

    public void setHealthcare_speciality(String healthcare_speciality) {
        this.healthcare_speciality = healthcare_speciality;
    }

    public String getDrinking_water() {
        return drinking_water;
    }

    public void setDrinking_water(String drinking_water) {
        this.drinking_water = drinking_water;
    }

    public String getToilet() {
        return toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

//    public String getClass_range() {
//        return class_range;
//    }
//
//    public void setClass_range(String class_range) {
//        this.class_range = class_range;
//    }
//
//    public String getLevel() {
//        return level;
//    }
//
//    public void setLevel(String level) {
//        this.level = level;
//    }

    public Integer getStudent_count() {
        return student_count;
    }

    public void setStudent_count(Integer student_count) {
        this.student_count = student_count;
    }
}

