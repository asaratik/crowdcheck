package com.msfthack.crowdcheck.helpers;

public class POI {
    private int uid;
    private String title;
    private double lat;
    private double lng;
    private int intensity;

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

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "POI{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", intensity=" + intensity +
                '}';
    }
}