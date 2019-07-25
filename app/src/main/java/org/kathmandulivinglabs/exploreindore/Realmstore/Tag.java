package org.kathmandulivinglabs.exploreindore.Realmstore;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Bhawak on 6/3/18.
 */
public class Tag extends RealmObject {
    private String amenity;
    private RealmList<String> osmtags;
//    private RealmList<String> tagslabel;
//    private RealmList<String> tagkey;

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public RealmList<String> getOsmtags() {
        return osmtags;
    }

    public void setOsmtags(RealmList<String> osmtags) {
        this.osmtags = osmtags;
    }

//    public RealmList<String> getTagslabel() {
//        return tagslabel;
//    }
//
//    public void setTagslabel(RealmList<String> tagslabel) {
//        this.tagslabel = tagslabel;
//    }
//
//    public RealmList<String> getTagkey() {
//        return tagkey;
//    }
//
//    public void setTagkey(RealmList<String> tagkey) {
//        this.tagkey = tagkey;
//    }
}
