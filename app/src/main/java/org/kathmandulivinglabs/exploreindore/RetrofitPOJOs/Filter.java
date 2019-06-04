package org.kathmandulivinglabs.exploreindore.RetrofitPOJOs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
    public class Filter {

        @SerializedName("label")
        @Expose
        public String label;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("parameter_name")
        @Expose
        public String parameterName;
        @SerializedName("options")
        @Expose
        public List<Option> options = null;
        @SerializedName("range")
        @Expose
        public Range range;
        @SerializedName("boolean")
        @Expose
        public Boolean _boolean;
        private String database_schema_key;

        public String getDatabase_schema_key() {
            return database_schema_key;
        }

        public void setDatabase_schema_key(String database_schema_key) {
            this.database_schema_key = database_schema_key;
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

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }

        public List<Option> getOptions() {
            return options;
        }

        public void setOptions(List<Option> options) {
            this.options = options;
        }

        public Range getRange() {
            return range;
        }

        public void setRange(Range range) {
            this.range = range;
        }

        public Boolean get_boolean() {
            return _boolean;
        }

        public void set_boolean(Boolean _boolean) {
            this._boolean = _boolean;
        }

        public class Option {

            @SerializedName("value")
            @Expose
            public String value;
            @SerializedName("label")
            @Expose
            public String label;
            private String database_schema_key;

            public String getDatabase_schema_key() {
                return database_schema_key;
            }

            public void setDatabase_schema_key(String database_schema_key) {
                this.database_schema_key = database_schema_key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }

        public class Range {

            @SerializedName("step")
            @Expose
            public Integer step;
            @SerializedName("max")
            @Expose
            public Integer max;
            @SerializedName("min")
            @Expose
            public Integer min;
            @SerializedName("high")
            @Expose
            public Integer high;
            @SerializedName("low")
            @Expose
            public Integer low;

            public Integer getStep() {
                return step;
            }

            public void setStep(Integer step) {
                this.step = step;
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

    }

