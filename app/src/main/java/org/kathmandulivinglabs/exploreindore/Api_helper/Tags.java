package org.kathmandulivinglabs.exploreindore.Api_helper;

import java.util.List;

/**
 * Created by Bhawak on 6/3/18.
 */
public class Tags {
    private Integer success;
    private List<Tag> tags = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public class Tag {
        private String amenity;
        private List<Tag_> tags = null;

        public String getAmenity() {
            return amenity;
        }

        public void setAmenity(String amenity) {
            this.amenity = amenity;
        }

        public List<Tag_> getTags() {
            return tags;
        }

        public void setTags(List<Tag_> tags) {
            this.tags = tags;
        }
        public class Tag_ {

            private String tag;
            private String label;
            private String database_schema_key;

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getDatabase_schema_key() {
                return database_schema_key;
            }

            public void setDatabase_schema_key(String database_schema_key) {
                this.database_schema_key = database_schema_key;
            }
        }
    }
}
