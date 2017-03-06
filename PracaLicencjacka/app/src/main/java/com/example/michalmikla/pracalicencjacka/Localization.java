package com.example.michalmikla.pracalicencjacka;

/**
 * Created by michal.mikla on 06.03.2017.
 */

public class Localization {

    private Integer loc_ID;
    private Float loc_latitude;
    private Float loc_longitude;
    private String loc_name;
    private Integer trip_ID_fk;

    public Localization(){

    }

    public Localization(Integer loc_id, Float loc_latitude, Float loc_longitude, String loc_name, Integer trip_id_fk) {
        loc_ID = loc_id;
        this.loc_latitude = loc_latitude;
        this.loc_longitude = loc_longitude;
        this.loc_name = loc_name;
        trip_ID_fk = trip_id_fk;
    }

    public Integer getLoc_ID() {
        return loc_ID;
    }

    public void setLoc_ID(Integer loc_ID) {
        this.loc_ID = loc_ID;
    }

    public Float getLoc_latitude() {
        return loc_latitude;
    }

    public void setLoc_latitude(Float loc_latitude) {
        this.loc_latitude = loc_latitude;
    }

    public Float getLoc_longitude() {
        return loc_longitude;
    }

    public void setLoc_longitude(Float loc_longitude) {
        this.loc_longitude = loc_longitude;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public Integer getTrip_ID_fk() {
        return trip_ID_fk;
    }

    public void setTrip_ID_fk(Integer trip_ID_fk) {
        this.trip_ID_fk = trip_ID_fk;
    }
}
