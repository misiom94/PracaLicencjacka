package com.example.michalmikla.pracalicencjacka;

import java.util.Date;

/**
 * Created by michal.mikla on 06.03.2017.
 */

public class Trip {

    private Integer trip_id;
    private String trip_title;
    private String trip_date;
    private Float trip_distance;
    private String trip_note;

    public Trip()
    {

    }

    public Trip(int trip_id , String trip_title, String trip_date, Float trip_distance, String trip_note) {
        this.trip_id = trip_id;
        this.trip_title = trip_title;
        this.trip_date = trip_date;
        this.trip_distance = trip_distance;
        this.trip_note = trip_note;
    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_title() {
        return trip_title;
    }

    public void setTrip_title(String trip_title) {
        this.trip_title = trip_title;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public Float getTrip_distance() {
        return trip_distance;
    }

    public void setTrip_distance(Float trip_distance) {
        this.trip_distance = trip_distance;
    }

    public String getTrip_note() {
        return trip_note;
    }

    public void setTrip_note(String trip_note) {
        this.trip_note = trip_note;
    }
}
