package org.kathmandulivinglabs.preparepokhara.RetrofitPOJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhawak on 6/26/18.
 */
public class Attractions {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("attractions")
    @Expose
    private List<Attraction> attractions = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public class Attraction {

        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("pois")
        @Expose
        private Pois pois;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("properties")
                @Expose
                private Properties properties;
                @SerializedName("geometry")
                @Expose
                private Geometry geometry;
                @SerializedName("detail")
                @Expose
                private Detail detail;

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

                public Detail getDetail() {
                    return detail;
                }

                public void setDetail(Detail detail) {
                    this.detail = detail;
                }
                public class Geometry {

                    @SerializedName("type")
                    @Expose
                    private String type;
                    @SerializedName("coordinates")
                    @Expose
                    private List<Double> coordinates = null;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public List<Double> getCoordinates() {
                        return coordinates;
                    }

                    public void setCoordinates(List<Double> coordinates) {
                        this.coordinates = coordinates;
                    }

                }
                public class Detail {

                    @SerializedName("content")
                    @Expose
                    private Object content;
                    @SerializedName("photo")
                    @Expose
                    private Object photo;

                    public Object getContent() {
                        return content;
                    }

                    public void setContent(Object content) {
                        this.content = content;
                    }

                    public Object getPhoto() {
                        return photo;
                    }

                    public void setPhoto(Object photo) {
                        this.photo = photo;
                    }

                }
                public class Properties {

                    @SerializedName("type")
                    @Expose
                    private String type;
                    @SerializedName("id")
                    @Expose
                    private String id;
                    @SerializedName("tags")
                    @Expose
                    private Tags tags;

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

                    public Tags getTags() {
                        return tags;
                    }

                    public void setTags(Tags tags) {
                        this.tags = tags;
                    }
                    public class Tags {

                        @SerializedName("amenity")
                        @Expose
                        private String amenity;
                        @SerializedName("layer")
                        @Expose
                        private String layer;
                        @SerializedName("name")
                        @Expose
                        private String name;
                        @SerializedName("name:ne")
                        @Expose
                        private String nameNe;
                        @SerializedName("place")
                        @Expose
                        private String place;
                        @SerializedName("religion")
                        @Expose
                        private String religion;
                        @SerializedName("tourism")
                        @Expose
                        private String tourism;
                        @SerializedName("addr:city")
                        @Expose
                        private String addrCity;
                        @SerializedName("addr:street")
                        @Expose
                        private String addrStreet;
                        @SerializedName("denomination")
                        @Expose
                        private String denomination;
                        @SerializedName("drinking_water")
                        @Expose
                        private String drinkingWater;
                        @SerializedName("opening_hours")
                        @Expose
                        private String openingHours;
                        @SerializedName("source")
                        @Expose
                        private String source;
                        @SerializedName("toilets")
                        @Expose
                        private String toilets;
                        @SerializedName("alt_name")
                        @Expose
                        private String altName;
                        @SerializedName("building")
                        @Expose
                        private String building;
                        @SerializedName("historic")
                        @Expose
                        private String historic;
                        @SerializedName("access")
                        @Expose
                        private String access;
                        @SerializedName("barrier")
                        @Expose
                        private String barrier;
                        @SerializedName("fee")
                        @Expose
                        private String fee;
                        @SerializedName("boundary")
                        @Expose
                        private String boundary;
                        @SerializedName("natural")
                        @Expose
                        private String natural;
                        @SerializedName("wheelchair")
                        @Expose
                        private String wheelchair;
                        @SerializedName("man_made")
                        @Expose
                        private String manMade;
                        @SerializedName("direction")
                        @Expose
                        private String direction;
                        @SerializedName("name:en")
                        @Expose
                        private String nameEn;
                        @SerializedName("boat")
                        @Expose
                        private String boat;
                        @SerializedName("type")
                        @Expose
                        private String type;
                        @SerializedName("water")
                        @Expose
                        private String water;
                        @SerializedName("wikidata")
                        @Expose
                        private String wikidata;
                        @SerializedName("wikipedia")
                        @Expose
                        private String wikipedia;
                        @SerializedName("alt_name:en")
                        @Expose
                        private String altNameEn;
                        @SerializedName("name:ru")
                        @Expose
                        private String nameRu;

                        public String getAmenity() {
                            return amenity;
                        }

                        public void setAmenity(String amenity) {
                            this.amenity = amenity;
                        }

                        public String getLayer() {
                            return layer;
                        }

                        public void setLayer(String layer) {
                            this.layer = layer;
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

                        public String getPlace() {
                            return place;
                        }

                        public void setPlace(String place) {
                            this.place = place;
                        }

                        public String getReligion() {
                            return religion;
                        }

                        public void setReligion(String religion) {
                            this.religion = religion;
                        }

                        public String getTourism() {
                            return tourism;
                        }

                        public void setTourism(String tourism) {
                            this.tourism = tourism;
                        }

                        public String getAddrCity() {
                            return addrCity;
                        }

                        public void setAddrCity(String addrCity) {
                            this.addrCity = addrCity;
                        }

                        public String getAddrStreet() {
                            return addrStreet;
                        }

                        public void setAddrStreet(String addrStreet) {
                            this.addrStreet = addrStreet;
                        }

                        public String getDenomination() {
                            return denomination;
                        }

                        public void setDenomination(String denomination) {
                            this.denomination = denomination;
                        }

                        public String getDrinkingWater() {
                            return drinkingWater;
                        }

                        public void setDrinkingWater(String drinkingWater) {
                            this.drinkingWater = drinkingWater;
                        }

                        public String getOpeningHours() {
                            return openingHours;
                        }

                        public void setOpeningHours(String openingHours) {
                            this.openingHours = openingHours;
                        }

                        public String getSource() {
                            return source;
                        }

                        public void setSource(String source) {
                            this.source = source;
                        }

                        public String getToilets() {
                            return toilets;
                        }

                        public void setToilets(String toilets) {
                            this.toilets = toilets;
                        }

                        public String getAltName() {
                            return altName;
                        }

                        public void setAltName(String altName) {
                            this.altName = altName;
                        }

                        public String getBuilding() {
                            return building;
                        }

                        public void setBuilding(String building) {
                            this.building = building;
                        }

                        public String getHistoric() {
                            return historic;
                        }

                        public void setHistoric(String historic) {
                            this.historic = historic;
                        }

                        public String getAccess() {
                            return access;
                        }

                        public void setAccess(String access) {
                            this.access = access;
                        }

                        public String getBarrier() {
                            return barrier;
                        }

                        public void setBarrier(String barrier) {
                            this.barrier = barrier;
                        }

                        public String getFee() {
                            return fee;
                        }

                        public void setFee(String fee) {
                            this.fee = fee;
                        }

                        public String getBoundary() {
                            return boundary;
                        }

                        public void setBoundary(String boundary) {
                            this.boundary = boundary;
                        }

                        public String getNatural() {
                            return natural;
                        }

                        public void setNatural(String natural) {
                            this.natural = natural;
                        }

                        public String getWheelchair() {
                            return wheelchair;
                        }

                        public void setWheelchair(String wheelchair) {
                            this.wheelchair = wheelchair;
                        }

                        public String getManMade() {
                            return manMade;
                        }

                        public void setManMade(String manMade) {
                            this.manMade = manMade;
                        }

                        public String getDirection() {
                            return direction;
                        }

                        public void setDirection(String direction) {
                            this.direction = direction;
                        }

                        public String getNameEn() {
                            return nameEn;
                        }

                        public void setNameEn(String nameEn) {
                            this.nameEn = nameEn;
                        }

                        public String getBoat() {
                            return boat;
                        }

                        public void setBoat(String boat) {
                            this.boat = boat;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }

                        public String getWater() {
                            return water;
                        }

                        public void setWater(String water) {
                            this.water = water;
                        }

                        public String getWikidata() {
                            return wikidata;
                        }

                        public void setWikidata(String wikidata) {
                            this.wikidata = wikidata;
                        }

                        public String getWikipedia() {
                            return wikipedia;
                        }

                        public void setWikipedia(String wikipedia) {
                            this.wikipedia = wikipedia;
                        }

                        public String getAltNameEn() {
                            return altNameEn;
                        }

                        public void setAltNameEn(String altNameEn) {
                            this.altNameEn = altNameEn;
                        }

                        public String getNameRu() {
                            return nameRu;
                        }

                        public void setNameRu(String nameRu) {
                            this.nameRu = nameRu;
                        }

                    }

                }
            }

        }
    }
}
