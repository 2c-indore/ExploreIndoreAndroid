package org.kathmandulivinglabs.exploreindore.Api_helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bhawak on 3/11/2018.
 */

public class Features {
    private int success;
    private Geojson geojson;
    private String message;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public Geojson getGeojson() {
        return geojson;
    }

    public void setGeojson(Geojson geojson) {
        this.geojson = geojson;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Geojson{
        private List<Feature> features = null;

        public List<Feature> getFeatures() {
            return features;
        }

        public void setFeatures(List<Feature> features) {
            this.features = features;
        }

        public class Feature{
            private Properties properties;
            private Geometry geometry;
            private String wardId;

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

            public class Properties{
                private String type;
                private Long id;
                private Tags tags;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public Tags getTags() {
                    return tags;
                }

                public void setTags(Tags tags) {
                    this.tags = tags;
                }
                public class Tags{

                    @SerializedName("amenity")
                    @Expose
                    private String amenity;
                    @SerializedName("building")
                    @Expose
                    private String building;
                    @SerializedName("capacity:beds")
                    @Expose
                    private String capacityBeds;
                    @SerializedName("email")
                    @Expose
                    private String email;
                    @SerializedName("emergency")
                    @Expose
                    private String emergency;
                    @SerializedName("emergency_service")
                    @Expose
                    private String emergencyService;
                    @SerializedName("facility:ambulance")
                    @Expose
                    private String facilityAmbulance;
                    @SerializedName("facility:icu")
                    @Expose
                    private String facilityIcu;
                    @SerializedName("facility:nicu")
                    @Expose
                    private String facilityNicu;
                    @SerializedName("facility:operating_theater")
                    @Expose
                    private String facilityOperatingTheater;
                    @SerializedName("facility:operation_theatre")
                    @Expose
                    private String facilityOperationTheatre;
                    @SerializedName("facility:ventilator")
                    @Expose
                    private String facilityVentilator;
                    @SerializedName("facility:x-ray")
                    @Expose
                    private String facilityXRay;
                    @SerializedName("name")
                    @Expose
                    private String name;
                    @SerializedName("name:ne")
                    @Expose
                    private String nameNe;
                    @SerializedName("note")
                    @Expose
                    private String note;
                    @SerializedName("operator:type")
                    @Expose
                    private String operatorType;
                    @SerializedName("personnel:count")
                    @Expose
                    private String personnelCount;
                    @SerializedName("phone")
                    @Expose
                    private String phone;
                    @SerializedName("source")
                    @Expose
                    private String source;
                    @SerializedName("website")
                    @Expose
                    private String website;
                    @SerializedName("alt_name")
                    @Expose
                    private String altName;
                    @SerializedName("emergency:services")
                    @Expose
                    private String emergencyServices;
                    @SerializedName("facility:operating_theatre")
                    @Expose
                    private String facilityOperatingTheatre;
                    @SerializedName("addr:city")
                    @Expose
                    private String addrCity;
                    @SerializedName("addr:street")
                    @Expose
                    private String addrStreet;
                    @SerializedName("start_date")
                    @Expose
                    private String startDate;
                    @SerializedName("opening_hours")
                    @Expose
                    private String openingHours;
                    @SerializedName("addr:postcode")
                    @Expose
                    private String addrPostcode;
                    @SerializedName("wheelchair")
                    @Expose
                    private String wheelchair;
                    @SerializedName("fax")
                    @Expose
                    private String fax;
                    @SerializedName("contact:phone")
                    @Expose
                    private String contactPhone;
                    @SerializedName("building:levels")
                    @Expose
                    private String buildingLevels;
                    @SerializedName("personel:count")
                    @Expose
                    private String personelCount;
                    @SerializedName("capacity:bed")
                    @Expose
                    private String capacityBed;
                    @SerializedName("facilityn:nicu")
                    @Expose
                    private String facilitynNicu;
                    @SerializedName("contact:fax")
                    @Expose
                    private String contactFax;
                    @SerializedName("contact:email")
                    @Expose
                    private String contactEmail;
                    @SerializedName("addr:place")
                    @Expose
                    private String addrPlace;
                    @SerializedName("operator")
                    @Expose
                    private String operator;
                    @SerializedName("name:en")
                    @Expose
                    private String nameEn;
                    @SerializedName("addr:housenumber")
                    @Expose
                    private String addrHousenumber;
                    @SerializedName("contact:")
                    @Expose
                    private String contact;
                    @SerializedName("estd")
                    @Expose
                    private String estd;
                    @SerializedName("ambulance")
                    @Expose
                    private String ambulance;

                    @SerializedName("student:count")
                    @Expose
                    private String studentCount;
                    private String atm;
                    @SerializedName("nrb_class")
                    @Expose
                    private String nrbClass;

                    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

                    public String getAmenity() {
                        return amenity;
                    }

                    public void setAmenity(String amenity) {
                        this.amenity = amenity;
                    }

                    public String getBuilding() {
                        return building;
                    }

                    public void setBuilding(String building) {
                        this.building = building;
                    }

                    public String getCapacityBeds() {
                        return capacityBeds;
                    }

                    public void setCapacityBeds(String capacityBeds) {
                        this.capacityBeds = capacityBeds;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
                    }

                    public String getEmergency() {
                        return emergency;
                    }

                    public void setEmergency(String emergency) {
                        this.emergency = emergency;
                    }

                    public String getEmergencyService() {
                        return emergencyService;
                    }

                    public void setEmergencyService(String emergencyService) {
                        this.emergencyService = emergencyService;
                    }

                    public String getFacilityAmbulance() {
                        return facilityAmbulance;
                    }

                    public void setFacilityAmbulance(String facilityAmbulance) {
                        this.facilityAmbulance = facilityAmbulance;
                    }

                    public String getFacilityIcu() {
                        return facilityIcu;
                    }

                    public void setFacilityIcu(String facilityIcu) {
                        this.facilityIcu = facilityIcu;
                    }

                    public String getFacilityNicu() {
                        return facilityNicu;
                    }

                    public void setFacilityNicu(String facilityNicu) {
                        this.facilityNicu = facilityNicu;
                    }

                    public String getFacilityOperatingTheater() {
                        return facilityOperatingTheater;
                    }

                    public void setFacilityOperatingTheater(String facilityOperatingTheater) {
                        this.facilityOperatingTheater = facilityOperatingTheater;
                    }

                    public String getFacilityOperationTheatre() {
                        return facilityOperationTheatre;
                    }

                    public void setFacilityOperationTheatre(String facilityOperationTheatre) {
                        this.facilityOperationTheatre = facilityOperationTheatre;
                    }

                    public String getFacilityVentilator() {
                        return facilityVentilator;
                    }

                    public void setFacilityVentilator(String facilityVentilator) {
                        this.facilityVentilator = facilityVentilator;
                    }

                    public String getFacilityXRay() {
                        return facilityXRay;
                    }

                    public void setFacilityXRay(String facilityXRay) {
                        this.facilityXRay = facilityXRay;
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

                    public String getNote() {
                        return note;
                    }

                    public void setNote(String note) {
                        this.note = note;
                    }

                    public String getOperatorType() {
                        return operatorType;
                    }

                    public void setOperatorType(String operatorType) {
                        this.operatorType = operatorType;
                    }

                    public String getPersonnelCount() {
                        return personnelCount;
                    }

                    public void setPersonnelCount(String personnelCount) {
                        this.personnelCount = personnelCount;
                    }

                    public String getPhone() {
                        return phone;
                    }

                    public void setPhone(String phone) {
                        this.phone = phone;
                    }

                    public String getSource() {
                        return source;
                    }

                    public void setSource(String source) {
                        this.source = source;
                    }

                    public String getWebsite() {
                        return website;
                    }

                    public void setWebsite(String website) {
                        this.website = website;
                    }

                    public String getAltName() {
                        return altName;
                    }

                    public void setAltName(String altName) {
                        this.altName = altName;
                    }

                    public String getEmergencyServices() {
                        return emergencyServices;
                    }

                    public void setEmergencyServices(String emergencyServices) {
                        this.emergencyServices = emergencyServices;
                    }

                    public String getFacilityOperatingTheatre() {
                        return facilityOperatingTheatre;
                    }

                    public void setFacilityOperatingTheatre(String facilityOperatingTheatre) {
                        this.facilityOperatingTheatre = facilityOperatingTheatre;
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

                    public String getStartDate() {
                        return startDate;
                    }

                    public void setStartDate(String startDate) {
                        this.startDate = startDate;
                    }

                    public String getOpeningHours() {
                        return openingHours;
                    }

                    public void setOpeningHours(String openingHours) {
                        this.openingHours = openingHours;
                    }

                    public String getAddrPostcode() {
                        return addrPostcode;
                    }

                    public void setAddrPostcode(String addrPostcode) {
                        this.addrPostcode = addrPostcode;
                    }

                    public String getWheelchair() {
                        return wheelchair;
                    }

                    public void setWheelchair(String wheelchair) {
                        this.wheelchair = wheelchair;
                    }

                    public String getFax() {
                        return fax;
                    }

                    public void setFax(String fax) {
                        this.fax = fax;
                    }

                    public String getContactPhone() {
                        return contactPhone;
                    }

                    public void setContactPhone(String contactPhone) {
                        this.contactPhone = contactPhone;
                    }

                    public String getBuildingLevels() {
                        return buildingLevels;
                    }

                    public void setBuildingLevels(String buildingLevels) {
                        this.buildingLevels = buildingLevels;
                    }

                    public String getPersonelCount() {
                        return personelCount;
                    }

                    public void setPersonelCount(String personelCount) {
                        this.personelCount = personelCount;
                    }

                    public String getCapacityBed() {
                        return capacityBed;
                    }

                    public void setCapacityBed(String capacityBed) {
                        this.capacityBed = capacityBed;
                    }

                    public String getFacilitynNicu() {
                        return facilitynNicu;
                    }

                    public void setFacilitynNicu(String facilitynNicu) {
                        this.facilitynNicu = facilitynNicu;
                    }

                    public String getContactFax() {
                        return contactFax;
                    }

                    public void setContactFax(String contactFax) {
                        this.contactFax = contactFax;
                    }

                    public String getContactEmail() {
                        return contactEmail;
                    }

                    public void setContactEmail(String contactEmail) {
                        this.contactEmail = contactEmail;
                    }

                    public String getAddrPlace() {
                        return addrPlace;
                    }

                    public void setAddrPlace(String addrPlace) {
                        this.addrPlace = addrPlace;
                    }

                    public String getOperator() {
                        return operator;
                    }

                    public void setOperator(String operator) {
                        this.operator = operator;
                    }

                    public String getNameEn() {
                        return nameEn;
                    }

                    public void setNameEn(String nameEn) {
                        this.nameEn = nameEn;
                    }

                    public String getAddrHousenumber() {
                        return addrHousenumber;
                    }

                    public void setAddrHousenumber(String addrHousenumber) {
                        this.addrHousenumber = addrHousenumber;
                    }

                    public String getContact() {
                        return contact;
                    }

                    public void setContact(String contact) {
                        this.contact = contact;
                    }
                    public String getEstd() {
                        return estd;
                    }

                    public void setEstd(String estd) {
                        this.estd = estd;
                    }

                    public String getAmbulance() {
                        return ambulance;
                    }

                    public void setAmbulance(String ambulance) {
                        this.ambulance = ambulance;
                    }

                    public String getStudentCount() {
                        return studentCount;
                    }

                    public void setStudentCount(String studentCount) {
                        this.studentCount = studentCount;
                    }

                    public String getAtm() {
                        return atm;
                    }

                    public void setAtm(String atm) {
                        this.atm = atm;
                    }

                    public String getNrbClass() {
                        return nrbClass;
                    }

                    public void setNrbClass(String nrbClass) {
                        this.nrbClass = nrbClass;
                    }

                    public Map<String, Object> getAdditionalProperties() {
                        return additionalProperties;
                    }

                    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
                        this.additionalProperties = additionalProperties;
                    }
                }
            }
            public class Geometry{
                private String type;
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

        }
    }
}
