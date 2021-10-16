package com.example.covid_19_info.model;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {
    public int _id;
    public String iso2;
    public String iso3;
    public float lat;

    @SerializedName("long")
    public float lng;
    public String flag;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
