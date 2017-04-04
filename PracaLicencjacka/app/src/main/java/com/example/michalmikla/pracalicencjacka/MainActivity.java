package com.example.michalmikla.pracalicencjacka;
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    Trip trip, takentrip;
    Localization localization;
    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        db = new DatabaseHelper(getApplicationContext());
//        System.out.println("DATABASE NAME: " + db.getDatabaseName());
//
//        trip = new Trip(1, "Test trip", "21.02.2012", (float) 5.00, "First trip");
//        System.out.println("TEST PRINT");
//        System.out.println(trip.getTrip_title()+ "\n" + trip.getTrip_date()+ "\n" +trip.getTrip_distance()+ "\n"+trip.getTrip_note());
//        db.createTrip(trip);
//        takentrip = db.getTripById(1);
//        System.out.println("TAKENTRIP: " + takentrip.getTrip_note());

    }
    public void goToMapActivity(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToCreateTripActivity(View view){
        Intent intent = new Intent(this,CreateTripActivity.class);
        startActivity(intent);
        finish();
    }
}
