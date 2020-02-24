package org.kathmandulivinglabs.exploreindore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryWrapper {
    public String type;
    @SerializedName("coordinates")
    public Object coordinates = null;
}
