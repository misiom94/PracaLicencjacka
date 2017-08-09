package com.example.michalmikla.pracalicencjacka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.mikla on 06.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

    //Local tag
    private static final String LOG = "DatabaseHelper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "tripsMenager.db";

    //Table names
    private static final String TABLE_TRIP = "trips";
    private static final String TABLE_LOCALIZATION = "localizations";
    private static final String TABLE_PHOTO = "photos";

    //Trip columns
    private static final String TRIP_ID = "trip_id";
    private static final String TRIP_TITLE = "trip_title";
    private static final String TRIP_DATE = "trip_date";
    private static final String TRIP_LENGTH = "trip_length";
    private static final String TRIP_NOTE = "trip_note";

    //Location columns
    private static final String LOC_ID = "localization_id";
    private static final String LOC_LATITUDE = "loc_latitude";
    private static final String LOC_LONGITUDE = "loc_longitude";
    private static final String LOC_NAME = "loc_name";
    private static final String TRIP_ID_FK = "trip_id_fk";

    //Photo columns
    private static final String PHOTO_ID = "photo_id";
    private static final String PHOTO_PATH = "photo_path";
    private static final String LOC_ID_FK = "loc_id_fk";

    //Creating table statements
    //Trip table create statement
    private static final String CREATE_TABLE_TRIP = "CREATE TABLE "
            + TABLE_TRIP + "(" + TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRIP_TITLE + " TEXT," + TRIP_DATE + " TEXT,"
            + TRIP_LENGTH + " FLOAT,"
            + TRIP_NOTE + " TEXT" + ")";

    //Localization table create statement
    private static final String CREATE_TABLE_LOCALIZATION = "CREATE TABLE "
            + TABLE_LOCALIZATION + "(" + LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LOC_LATITUDE + " TEXT," + LOC_LONGITUDE + " TEXT,"
            + LOC_NAME + " TEXT,"
            + TRIP_ID_FK + " TEXT,"
            + " FOREIGN KEY(" + TRIP_ID_FK + ") REFERENCES "
            + TABLE_TRIP + "("+TRIP_ID+")" + ");";

    //Photo table create statement
    private static final String CREATE_TABLE_PHOTO = "CREATE TABLE "
            + TABLE_PHOTO + "(" + PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PHOTO_PATH + " TEXT,"
            + LOC_ID_FK + " TEXT,"
            + " FOREIGN KEY (" +LOC_ID_FK+ ") REFERENCES "
            + TABLE_LOCALIZATION + "("+LOC_ID+")" + ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_PHOTO + "," + TABLE_LOCALIZATION + "," + TABLE_TRIP;

    private String result;

    public DatabaseHelper(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_LOCALIZATION);
        db.execSQL(CREATE_TABLE_PHOTO);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old tables to create new updated
        db.execSQL(SQL_DELETE_ENTRIES);
        //creating new updated tables
        onCreate(db);

    }

    public static DatabaseHelper getInstance(Context context){
        if(mInstance==null){
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    //CRUD(Create, Read, Update, Delete) Operations

    public long createTrip(Trip trip){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRIP_TITLE, trip.getTrip_title());
        values.put(TRIP_DATE, trip.getTrip_date());
        values.put(TRIP_LENGTH, trip.getTrip_distance());
        values.put(TRIP_NOTE, trip.getTrip_note());

        return db.insert(TABLE_TRIP, null, values);
    }

    public long createTripNoClass(DatabaseHelper mDatabaseHelper, String trip_title, String trip_date, float trip_distance, String trip_note)
    {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRIP_TITLE,trip_title);
        values.put(TRIP_DATE,trip_date);
        values.put(TRIP_LENGTH,trip_distance);
        values.put(TRIP_NOTE,trip_note);

        return db.insert(TABLE_TRIP,null,values);

    }
    public Trip selectTrip(){
        SQLiteDatabase db = this.getReadableDatabase();
        String querySelect = "SELECT * FROM " + TABLE_TRIP ;
        Trip trip = new Trip();
        Cursor c = db.rawQuery(querySelect,null);
        c.moveToLast();
        try{
            trip.setTrip_id(c.getInt(c.getColumnIndex(TRIP_ID)));
            trip.setTrip_title(c.getString(c.getColumnIndex(TRIP_TITLE)));
            trip.setTrip_date(c.getString(c.getColumnIndex(TRIP_DATE)));
            trip.setTrip_distance(c.getFloat(c.getColumnIndex(TRIP_LENGTH)));
            trip.setTrip_note(c.getString(c.getColumnIndex(TRIP_NOTE)));

        }catch(NullPointerException e){
            e.printStackTrace();
//            Toast.makeText(this,"No data in table!", Toast.LENGTH_SHORT).show();
            Log.e("DATABSE","NO DATA IN TABLE!");
        }
        Log.e(LOG,"TRIP DATA: "+"\n"+trip.getTrip_id()+"\n"+trip.getTrip_title()+"\n"+trip.getTrip_date());
        return trip;

    }

    public Trip getTripById(int trip_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String querySelect = "SELECT * FROM " + TABLE_TRIP + " WHERE "
                + TRIP_ID + " = " + trip_id;
        Log.e(LOG, querySelect);
        Trip trip = new Trip();

        Cursor c = db.rawQuery(querySelect,null);
        try{
            if(c!=null) {
                c.moveToFirst();
            }
            trip.setTrip_id(c.getInt(c.getColumnIndex(TRIP_ID)));
            trip.setTrip_title(c.getString(c.getColumnIndex(TRIP_NOTE)));
            trip.setTrip_date(c.getString(c.getColumnIndex(TRIP_DATE)));
            trip.setTrip_distance(c.getFloat(c.getColumnIndex(TRIP_LENGTH)));
            trip.setTrip_note(c.getString(c.getColumnIndex(TRIP_NOTE)));

        }catch(NullPointerException e){
            e.printStackTrace();
//            Toast.makeText(this,"No data in table!", Toast.LENGTH_SHORT).show();
            Log.e("DATABSE","NO DATA IN TABLE!");
        }
        Log.e(LOG,"TRIP DATA: "+"\n"+trip.getTrip_id()+"\n"+trip.getTrip_title()+"\n"+trip.getTrip_date());
        return trip;
    }

    public ArrayList<Trip> getAllTrips(){
        ArrayList<Trip> trips = new ArrayList<>();
        String querySelect = "SELECT * FROM " + TABLE_TRIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelect,null);
        //looping all rows and adding to list
        if(c.moveToFirst()){
            do{
                Trip trip = new Trip();
                trip.setTrip_id(c.getInt(c.getColumnIndex(TRIP_ID)));
                trip.setTrip_title(c.getString(c.getColumnIndex(TRIP_NOTE)));
                trip.setTrip_date(c.getString(c.getColumnIndex(TRIP_DATE)));
                trip.setTrip_distance(c.getFloat(c.getColumnIndex(TRIP_LENGTH)));
                trip.setTrip_note(c.getString(c.getColumnIndex(TRIP_NOTE)));

                trips.add(trip);
            }while(c.moveToNext());
        }Log.i(LOG, querySelect);
        return trips;

    }

    public int updateTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRIP_TITLE,trip.getTrip_title());
        values.put(TRIP_DATE, String.valueOf(trip.getTrip_date()));
        values.put(TRIP_LENGTH, trip.getTrip_distance());
        values.put(TRIP_NOTE, trip.getTrip_note());

        return db.update(TABLE_TRIP,values,TRIP_ID + " = ?", new String[]{
                String.valueOf(trip.getTrip_id())});
    }

    public void deleteTrip(int trip_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIP,TRIP_ID + " = ?",
                new String[] {String.valueOf(trip_id)});
    }

    public long createLocalization(Localization localization)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOC_ID, localization.getLoc_ID());
        values.put(LOC_LATITUDE, localization.getLoc_latitude());
        values.put(LOC_LONGITUDE, localization.getLoc_longitude());
        values.put(LOC_NAME, localization.getLoc_name());
        values.put(TRIP_ID_FK, localization.getTrip_ID_fk());

        return db.insert(TABLE_LOCALIZATION, null, values);
    }
    public long createLocalizationNoClass(double latitude, double longitude, String name, int tripId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOC_LATITUDE,latitude);
        values.put(LOC_LONGITUDE,longitude);
        values.put(LOC_NAME, name);
        values.put(TRIP_ID_FK, tripId);
        return db.insert(TABLE_LOCALIZATION, null, values);
    }

    public Localization getLastLocalization()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String querySelect = "SELECT * FROM " + TABLE_LOCALIZATION;
        Log.e(LOG,querySelect);
        Cursor c = db.rawQuery(querySelect,null);
        if(c!=null){
            c.moveToLast();
        }
        Localization localization = new Localization();
        localization.setLoc_ID(c.getInt(c.getColumnIndex(LOC_ID)));
        localization.setLoc_latitude(c.getFloat(c.getColumnIndex(LOC_LATITUDE)));
        localization.setLoc_longitude(c.getFloat(c.getColumnIndex(LOC_LONGITUDE)));
        localization.setLoc_name(c.getString(c.getColumnIndex(LOC_NAME)));
        localization.setTrip_ID_fk(c.getInt(c.getColumnIndex(TRIP_ID_FK)));

        return localization;


    }

    public List<Localization> getLocalizationsById(int tripId) {
        List<Localization> localizations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String querySelect = "SELECT * FROM " + TABLE_LOCALIZATION + " WHERE "
                + TRIP_ID_FK + " = " + tripId;
        Log.e(LOG, querySelect);
        Cursor c = db.rawQuery(querySelect, null);
        if (c != null) {
            c.moveToFirst();
            do {
                Localization localization = new Localization();
                localization.setLoc_ID(c.getInt(c.getColumnIndex(LOC_ID)));
                localization.setLoc_latitude(c.getFloat(c.getColumnIndex(LOC_LATITUDE)));
                localization.setLoc_longitude(c.getFloat(c.getColumnIndex(LOC_LONGITUDE)));
                localization.setLoc_name(c.getString(c.getColumnIndex(LOC_NAME)));
                localization.setTrip_ID_fk(c.getInt(c.getColumnIndex(TRIP_ID_FK)));
                localizations.add(localization);
            } while (c.moveToNext());
        }
        return localizations;
    }

    public List<Localization> getAllLocalizations(){
        List<Localization> localizations = new ArrayList<Localization>();
        String querySelect = "SELECT * FROM " + TABLE_LOCALIZATION;
        Log.e(LOG, querySelect);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelect,null);
        if(c.moveToFirst()){
            do{
                Localization localization = new Localization();
                localization.setLoc_ID(c.getInt(c.getColumnIndex(LOC_ID)));
                localization.setLoc_latitude(c.getFloat(c.getColumnIndex(LOC_LATITUDE)));
                localization.setLoc_longitude(c.getFloat(c.getColumnIndex(LOC_LONGITUDE)));
                localization.setLoc_name(c.getString(c.getColumnIndex(LOC_NAME)));
                localization.setTrip_ID_fk(c.getInt(c.getColumnIndex(TRIP_ID_FK)));

                localizations.add(localization);
            }while(c.moveToNext());
        }
        return localizations;
    }

    public int updateLocalization(Localization localization){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOC_ID, localization.getLoc_ID());
        values.put(LOC_LATITUDE, localization.getLoc_latitude());
        values.put(LOC_LONGITUDE, localization.getLoc_longitude());
        values.put(LOC_NAME, localization.getLoc_name());
        values.put(TRIP_ID_FK, localization.getTrip_ID_fk());
        //update row
        return db.update(TABLE_LOCALIZATION, values, LOC_ID + " = ?",
                new String[] {String.valueOf(localization.getLoc_ID())});

    }

    public void deleteLocalization(int loc_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCALIZATION,LOC_ID + " = ?",
                new String[] {String.valueOf(loc_id)});
    }

    public long createPhoto(String photoPath, int locIdFk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(PHOTO_ID, photo.getPhoto_ID());
        values.put(PHOTO_PATH, photoPath);
        values.put(LOC_ID_FK, locIdFk);
        return db.insert(TABLE_PHOTO, null, values);

    }

    public Photo getLastPhoto()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String querySelect = "SELECT * FROM " + TABLE_PHOTO ;
        Log.e(LOG, querySelect);
        Cursor c = db.rawQuery(querySelect, null);
        if(c != null){
            c.moveToLast();
        }
        Photo photo = new Photo();
        photo.setPhoto_ID(c.getInt(c.getColumnIndex(PHOTO_ID)));
        photo.setPhoto_path(c.getString(c.getColumnIndex(PHOTO_PATH)));
        photo.setLoc_ID_fk(c.getColumnIndex(LOC_ID_FK));
        return photo;
    }

    public Photo getPhoto(int photo_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String querySelect = "SELECT * FROM " + TABLE_PHOTO + " WHERE "
                + PHOTO_ID + " = ?" + photo_id;
        Log.e(LOG, querySelect);

        Cursor c = db.rawQuery(querySelect, null);
        if(c != null){
            c.moveToFirst();
        }
        Photo photo = new Photo();
        photo.setPhoto_ID(c.getInt(c.getColumnIndex(PHOTO_ID)));
        photo.setPhoto_path(c.getString(c.getColumnIndex(PHOTO_PATH)));
        photo.setLoc_ID_fk(c.getColumnIndex(LOC_ID_FK));

        return photo;
    }
    public List<Photo> getPhotosById(int locID){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Photo> photos = new ArrayList<>();
        String querySelect = "SELECT * FROM " + TABLE_PHOTO + " WHERE " +
                LOC_ID_FK + " = " + locID;
        Log.e(LOG, querySelect);
        Cursor c = db.rawQuery(querySelect,null);
        if(c.moveToFirst()){
            do{
                Photo photo = new Photo();
                photo.setPhoto_ID(c.getInt(c.getColumnIndex(PHOTO_ID)));
                photo.setPhoto_path(c.getString(c.getColumnIndex(PHOTO_PATH)));
                photo.setLoc_ID_fk(c.getInt(c.getColumnIndex(LOC_ID_FK)));
                photos.add(photo);
            }while(c.moveToNext());
        }
        return photos;
    }

    public List<Photo> getAllPhotos(){
        List<Photo> photos = new ArrayList<Photo>();
        String querySelect = "SELECT * FROM " + TABLE_PHOTO;
        Log.e(LOG, querySelect);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelect,null);
        if(c.moveToFirst()){
            Photo photo = new Photo();
            photo.setPhoto_ID(c.getInt(c.getColumnIndex(PHOTO_ID)));
            photo.setPhoto_path(c.getString(c.getColumnIndex(PHOTO_PATH)));
            photo.setLoc_ID_fk(c.getInt(c.getColumnIndex(LOC_ID_FK)));
            photos.add(photo);
        }while(c.moveToNext());
        return photos;

    }

    public int updatePhoto(Photo photo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PHOTO_ID, photo.getPhoto_ID());
        values.put(PHOTO_PATH, photo.getPhoto_path());
        values.put(LOC_ID_FK, photo.getLoc_ID_fk());

        return db.update(TABLE_PHOTO, values, PHOTO_ID + " = ?",
                new String[]{String.valueOf(photo.getPhoto_ID())});
    }

    public void deletePhoto(int photo_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO, PHOTO_ID + " = ?",
                new String[]{String.valueOf(photo_id)});
    }









}
