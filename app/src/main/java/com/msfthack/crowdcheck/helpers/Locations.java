package com.msfthack.crowdcheck.helpers;

import java.util.List;

public class Locations {
    private int uid;
    private String title;
    private double lat;
    private double lng;
    private List<POI> poi;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public List<POI> getPoi() {
        return poi;
    }

    public void setPoi(List<POI> poi) {
        this.poi = poi;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", poi=" + poi +
                '}';
    }
}