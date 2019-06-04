package org.kathmandulivinglabs.exploreindore.RetrofitPOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.kathmandulivinglabs.exploreindore.Api_helper.Wards;

import java.util.List;

public class Features {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("geometries")
    @Expose
    private Geometries geometries;

    @SerializedName("filters")
    @Expose
    private List<Filter> filters = null;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Geometries getGeometries() {
        return geometries;
    }

    public void setGeometries(Geometries geometries) {
        this.geometries = geometries;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public class Geometries {

        @SerializedName("pois")
        @Expose
        private Pois pois;

        @SerializedName("boundary")
        @Expose
        private Wards.Boundary boundary;
        @SerializedName("boundaryWithWards")
        @Expose
        private Wards.BoundaryWithWards boundaryWithWards;

        public Wards.Boundary getBoundary() {
            return boundary;
        }

        public void setBoundary(Wards.Boundary boundary) {
            this.boundary = boundary;
        }

        public Wards.BoundaryWithWards getBoundaryWithWards() {
            return boundaryWithWards;
        }

        public void setBoundaryWithWards(Wards.BoundaryWithWards boundaryWithWards) {
            this.boundaryWithWards = boundaryWithWards;
        }

        public Pois getPois() {
            return pois;
        }

        public void setPois(Pois pois) {
            this.pois = pois;
        }



        public class Pois {

            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("features")
            @Expose
            private List<Feature> features = null;
            @SerializedName("createdOn")
            @Expose
            private long createdOn;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Feature> getFeatures() {
                return features;
            }

            public void setFeatures(List<Feature> features) {
                this.features = features;
            }

            public long getCreatedOn() {
                return createdOn;
            }

            public void setCreatedOn(long createdOn) {
                this.createdOn = createdOn;
            }

            public class Feature {

                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("properties")
                @Expose
                private Properties properties;
                @SerializedName("geometry")
                @Expose
                private Geometry geometry;
                @SerializedName("wardId")
                @Expose
                private String wardId;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Properties getProperties() {
                    return properties;
                }

                public void setProperties(Properties properties) {
                    this.properties = properties;
                }

                public Geometry getGeometry() {
                    return geometry;
                }

                public void setGeometry(Geometry geometry) {
                    this.geometry = geometry;
                }

                public String getWardId() {
                    return wardId;
                }

                public void setWardId(String wardId) {
                    this.wardId = wardId;
                }

                public class Properties {

                    @SerializedName("type")
                    @Expose
                    private String type;
                    @SerializedName("id")
                    @Expose
                    private long id;
                    @SerializedName("tags")
                    @Expose
                    private Tags tags;
                    @SerializedName("relations")
                    @Expose
                    private List<Object> relations = null;
                    @SerializedName("meta")
                    @Expose
                    private Meta meta;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public long getId() {
                        return id;
                    }

                    public void setId(long id) {
                        this.id = id;
                    }

                    public Tags getTags() {
                        return tags;
                    }

                    public void setTags(Tags tags) {
                        this.tags = tags;
                    }

                    public List<Object> getRelations() {
                        return relations;
                    }

                    public void setRelations(List<Object> relations) {
                        this.relations = relations;
                    }

                    public Meta getMeta() {
                        return meta;
                    }

                    public void setMeta(Meta meta) {
                        this.meta = meta;
                    }


                    public class Meta {

                        @SerializedName("timestamp")
                        @Expose
                        private String timestamp;
                        @SerializedName("version")
                        @Expose
                        private int version;
                        @SerializedName("changeset")
                        @Expose
                        private int changeset;
                        @SerializedName("user")
                        @Expose
                        private String user;
                        @SerializedName("uid")
                        @Expose
                        private int uid;

                        public String getTimestamp() {
                            return timestamp;
                        }

                        public void setTimestamp(String timestamp) {
                            this.timestamp = timestamp;
                        }

                        public int getVersion() {
                            return version;
                        }

                        public void setVersion(int version) {
                            this.version = version;
                        }

                        public int getChangeset() {
                            return changeset;
                        }

                        public void setChangeset(int changeset) {
                            this.changeset = changeset;
                        }

                        public String getUser() {
                            return user;
                        }

                        public void setUser(String user) {
                            this.user = user;
                        }

                        public int getUid() {
                            return uid;
                        }

                        public void setUid(int uid) {
                            this.uid = uid;
                        }
                    }

                }

                public class Geometry {

                    @SerializedName("type")
                    @Expose
                    private String type;
                    @SerializedName("coordinates")
                    @Expose
                    private List<Float> coordinates = null;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public List<Float> getCoordinates() {
                        return coordinates;
                    }

                    public void setCoordinates(List<Float> coordinates) {
                        this.coordinates = coordinates;
                    }

                }

            }

        }

    }
}
