package org.kathmandulivinglabs.preparepokhara;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhawak on 3/18/2018.
 */

public class FilterParcel implements Parcelable {
    Map<String,String> filter_parameter = new HashMap<>();

    public Map<String, String> getFilter_parameter() {
        return filter_parameter;
    }

    public void setFilter_parameter(Map<String, String> filter_parameter) {
        this.filter_parameter = filter_parameter;
    }


    public static Creator<FilterParcel> getCREATOR() {
        return CREATOR;
    }

    public FilterParcel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.filter_parameter.size());
        for (Map.Entry<String, String> entry : this.filter_parameter.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected FilterParcel(Parcel in) {
        int filter_parameterSize = in.readInt();
        this.filter_parameter = new HashMap<String, String>(filter_parameterSize);
        for (int i = 0; i < filter_parameterSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.filter_parameter.put(key, value);
        }
    }

    public static final Creator<FilterParcel> CREATOR = new Creator<FilterParcel>() {
        @Override
        public FilterParcel createFromParcel(Parcel source) {
            return new FilterParcel(source);
        }

        @Override
        public FilterParcel[] newArray(int size) {
            return new FilterParcel[size];
        }
    };
}
