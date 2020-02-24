package org.kathmandulivinglabs.exploreindore.Api_helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.Features;

import java.util.List;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Created by Bhawak on 3/11/2018.
 */

public class Wards {
    @SerializedName("boundary")
    @Expose
    private Boundary boundary;
    @SerializedName("boundaryWithWards")
    @Expose
    private BoundaryWithWards boundaryWithWards;

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public BoundaryWithWards getBoundaryWithWards() {
        return boundaryWithWards;
    }

    public void setBoundaryWithWards(BoundaryWithWards boundaryWithWards) {
        this.boundaryWithWards = boundaryWithWards;
    }

    public class Boundary {
        @SerializedName("features")
        @Expose
        private List<Fr> features;

        public class Fr {

            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("properties")
            @Expose
            private Properties properties;
            @SerializedName("geometry")
            @Expose
            private Geometry geometry;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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

            public class Geometry {

                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("coordinates")
                @Expose
                public Object coordinates;
//                private List<List<List<List<Double>>>> m_coordinates = null;
//                @SerializedName("coordinates")
//                @Expose
//                private List<List<List<Double>>> p_coordinates = null;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

//                public List<List<List<List<Double>>>> getMCoordinates() {
//                    return m_coordinates;
//                }
//
//                public void setMCoordinates(List<List<List<List<Double>>>> coordinates) {
//                    this.m_coordinates = coordinates;
//                }
//
//                public List<List<List<Double>>> getPCoordinates() {
//                    return p_coordinates;
//                }
//
//                public void setCoordinates(List<List<List<Double>>> coordinates) {
//                    this.p_coordinates = coordinates;
//                }
            }

            public class Properties {

                @SerializedName("@id")
                @Expose
                private String id;
                @SerializedName("admin_level")
                @Expose
                private String adminLevel;
                @SerializedName("alt_name")
                @Expose
                private String altName;
                @SerializedName("boundary")
                @Expose
                private String boundary;
                @SerializedName("description")
                @Expose
                private String description;
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("name:ne")
                @Expose
                private String nameNe;
                @SerializedName("name:suffix")
                @Expose
                private String nameSuffix;
                @SerializedName("type")
                @Expose
                private String type;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getAdminLevel() {
                    return adminLevel;
                }

                public void setAdminLevel(String adminLevel) {
                    this.adminLevel = adminLevel;
                }

                public String getAltName() {
                    return altName;
                }

                public void setAltName(String altName) {
                    this.altName = altName;
                }

                public String getBoundary() {
                    return boundary;
                }

                public void setBoundary(String boundary) {
                    this.boundary = boundary;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getNameNe() {
                    return nameNe;
                }

                public void setNameNe(String nameNe) {
                    this.nameNe = nameNe;
                }

                public String getNameSuffix() {
                    return nameSuffix;
                }

                public void setNameSuffix(String nameSuffix) {
                    this.nameSuffix = nameSuffix;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

            }
        }

        public List<Fr> getFeatures() {
            return features;
        }

        public void setFeatures(List<Fr> features) {
            this.features = features;
        }
    }

    public class BoundaryWithWards {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("features")
        @Expose
        private List<Feature> features = null;

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

        public class Feature {

            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("geometry")
            @Expose
            private Geometry_ geometry;
            @SerializedName("properties")
            @Expose
            private Properties_ properties;
            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("centroid")
            @Expose
            private Centroid centroid;

            public Centroid getCentroid() {
                return centroid;
            }

            public void setCentroid(Centroid centroid) {
                this.centroid = centroid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Geometry_ getGeometry() {
                return geometry;
            }

            public void setGeometry(Geometry_ geometry) {
                this.geometry = geometry;
            }

            public Properties_ getProperties() {
                return properties;
            }

            public void setProperties(Properties_ properties) {
                this.properties = properties;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public class Geometry_ {

                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("coordinates")
                @Expose
                public Object coordinates = null;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

//                public List<List<List<Double>>> getCoordinates() {
//                    return coordinates;
//                }

                public void setCoordinates(List<List<List<Double>>> coordinates) {
                    this.coordinates = coordinates;
                }

            }

            public class Centroid {
                @SerializedName("coordinates")
                @Expose
                private List<Double> coordinates = null;

                public List<Double> getCoordinates() {
                    return coordinates;
                }

                public void setCoordinates(List<Double> coordinates) {
                    this.coordinates = coordinates;
                }
            }

            public class Properties_ {

                @SerializedName("ward_no")
                @Expose
                private String ward_no;
                @SerializedName("ward_name")
                @Expose
                private String ward_name;

                public String getWard_no() {
                    return ward_no;
                }

                public void setWard_no(String ward_no) {
                    this.ward_no = ward_no;
                }

                public String getWard_name() {
                    return ward_name;
                }

                public void setWard_name(String ward_name) {
                    this.ward_name = ward_name;
                }
                //                @SerializedName("@id")
//                @Expose
//                private String id;
//                @SerializedName("admin_level")
//                @Expose
//                private String adminLevel;
//                @SerializedName("alt_name")
//                @Expose
//                private String altName;
//                @SerializedName("alt_name1")
//                @Expose
//                private String altName1;
//                @SerializedName("boundary")
//                @Expose
//                private String boundary;
//                @SerializedName("name")
//                @Expose
//                private String name;
//                @SerializedName("name:ne")
//                @Expose
//                private String nameNe;
//                @SerializedName("type")
//                @Expose
//                private String type;
//
//                public String getId() {
//                    return id;
//                }
//
//                public void setId(String id) {
//                    this.id = id;
//                }
//
//                public String getAdminLevel() {
//                    return adminLevel;
//                }
//
//                public void setAdminLevel(String adminLevel) {
//                    this.adminLevel = adminLevel;
//                }
//
//                public String getAltName() {
//                    return altName;
//                }
//
//                public void setAltName(String altName) {
//                    this.altName = altName;
//                }
//
//                public String getAltName1() {
//                    return altName1;
//                }
//
//                public void setAltName1(String altName1) {
//                    this.altName1 = altName1;
//                }
//
//                public String getBoundary() {
//                    return boundary;
//                }
//
//                public void setBoundary(String boundary) {
//                    this.boundary = boundary;
//                }
//
//                public String getName() {
//                    return name;
//                }
//
//                public void setName(String name) {
//                    this.name = name;
//                }
//
//                public String getNameNe() {
//                    return nameNe;
//                }
//
//                public void setNameNe(String nameNe) {
//                    this.nameNe = nameNe;
//                }
//
//                public String getType() {
//                    return type;
//                }
//
//                public void setType(String type) {
//                    this.type = type;
//                }
            }

        }

    }

}
