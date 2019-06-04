package org.kathmandulivinglabs.exploreindore.Realmstore;

import io.realm.RealmObject;

/**
 * Created by Bhawak on 3/27/18.
 */

public class Bank extends RealmObject {
    private String name,name_en, name_ne,opening_hours, phone, mail, web, wardid,atm;
    private Long id;
    private String type,operator_type,nrb_class;
    private double coordinateslong;
    private double coordinateslat;

    public String getAtm() {
        return atm;
    }

    public void setAtm(String atm) {
        this.atm = atm;
    }

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

    public String getNrb_class() {
        return nrb_class;
    }

    public void setNrb_class(String nrb_class) {
        this.nrb_class = nrb_class;
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

