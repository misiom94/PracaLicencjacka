package com.example.michalmikla.pracalicencjacka;

import java.util.Date;

/**
 * Created by michal.mikla on 06.03.2017.
 */

public class Photo {

    private String photo_path;
    private Integer photo_ID;
    private String photo_date;
    private Integer loc_ID_fk;


    public Photo(){

    }

    public Photo(String photo_path, Integer photo_id,
                 String photo_date, Integer loc_id_fk) {
        this.photo_path = photo_path;
        photo_ID = photo_id;
        loc_ID_fk = loc_id_fk;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getPhoto_date(){return photo_date;}

    public void setPhoto_date(String date){this.photo_date = date;}

    public Integer getPhoto_ID() {
        return photo_ID;
    }

    public void setPhoto_ID(Integer photo_ID) {
        this.photo_ID = photo_ID;
    }

    public Integer getLoc_ID_fk() {
        return loc_ID_fk;
    }

    public void setLoc_ID_fk(Integer loc_ID_fk) {
        this.loc_ID_fk = loc_ID_fk;
    }

}
