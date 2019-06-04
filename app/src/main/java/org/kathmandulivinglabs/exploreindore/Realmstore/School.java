package org.kathmandulivinglabs.exploreindore.Realmstore;

import io.realm.RealmObject;

/**
 * Created by Bhawak on 3/27/18.
 */

public class School extends RealmObject {
    private String name,name_en, name_ne,opening_hours, phone, mail, web, wardid;
    private Long id;
    private Integer student_count, personnel_count;
    private String type,operator_type;
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

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStudent_count() {
        return student_count;
    }

    public void setStudent_count(Integer student_count) {
        this.student_count = student_count;
    }

    public Integer getPersonnel_count() {
        return personnel_count;
    }

    public void setPersonnel_count(Integer personnel_count) {
        this.personnel_count = personnel_count;
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
}
