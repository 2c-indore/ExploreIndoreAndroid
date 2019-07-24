package org.kathmandulivinglabs.exploreindore.RetrofitPOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Tags {

   private   String key;
   private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
