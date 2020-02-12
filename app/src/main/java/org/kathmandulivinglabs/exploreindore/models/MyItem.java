package org.kathmandulivinglabs.exploreindore.models;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.kathmandulivinglabs.exploreindore.Customclass.CustomClusterItem;

public class MyItem implements CustomClusterItem {
    private final LatLng position;
    private String title;
    private String snippet;
    private com.mapbox.mapboxsdk.annotations.Icon icon;

    public MyItem(double lat, double lng) {
        position = new LatLng(lat, lng);
        title = null;
        snippet = null;
    }

    public MyItem(double lat, double lng, String title, String snippet, com.mapbox.mapboxsdk.annotations.Icon icon) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.icon = icon;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public com.mapbox.mapboxsdk.annotations.Icon getIcon() {
        return icon;
    }
}
