package org.kathmandulivinglabs.exploreindore.Realmstore;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Bhawak on 6/12/18.
 */
public class FilterSchema extends RealmObject {
    private String amenity;
    private String label, type, parameter_name,dbkey;
    private Boolean _boolean;
    private Integer max, min, high, low;
    RealmList<String> option_value;
    RealmList<String> option_lable;
    RealmList<String> option_key;

    public String getDbkey() {
        return dbkey;
    }

    public void setDbkey(String dbkey) {
        this.dbkey = dbkey;
    }

    public RealmList<String> getOption_key() {
        return option_key;
    }

    public void setOption_key(RealmList<String> option_key) {
        this.option_key = option_key;
    }

    public RealmList<String> getOption_value() {
        return option_value;
    }

    public void setOption_value(RealmList<String> option_value) {
        this.option_value = option_value;
    }

    public RealmList<String> getOption_lable() {
        return option_lable;
    }

    public void setOption_lable(RealmList<String> option_lable) {
        this.option_lable = option_lable;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParameter_name() {
        return parameter_name;
    }

    public void setParameter_name(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    public Boolean get_boolean() {
        return _boolean;
    }

    public void set_boolean(Boolean _boolean) {
        this._boolean = _boolean;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }
}
