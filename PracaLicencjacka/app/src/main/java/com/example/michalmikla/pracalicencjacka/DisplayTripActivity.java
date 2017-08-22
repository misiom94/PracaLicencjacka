package com.example.michalmikla.pracalicencjacka;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DisplayTripActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private static final String LOG = "DISPLAY TRIP ACTIVITY";
    GoogleMap mMap;
    private LatLng markerLocalization;
    TextView textTitle, textDate, textNote;
    int tripID;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_trip);
        db = DatabaseHelper.getInstance(this);
        textTitle = (TextView) findViewById(R.id.textViewTitle);
        textDate = (TextView) findViewById(R.id.textViewDate);
        textNote = (TextView) findViewById(R.id.textViewNote);
        Intent tableIntent = getIntent();
        recieveDataFromMap(tableIntent);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        mapFragment.getMapAsync(this);

    }

    private void setupMap() {

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void deleteTrip(View view)
    {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this trip?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteTrip(tripID);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void recieveDataFromMap(Intent intent)
    {
        tripID = intent.getIntExtra("tripId",0);
        Log.i(LOG,"----Recieved data----" + "\nTrip id: " + tripID);
    }

    public void showTripData()
    {
        Trip trip = db.getTripById(tripID);
        textTitle.setText(trip.getTrip_title());
        textNote.setText(trip.getTrip_note());
        textDate.setText(trip.getTrip_date());
        try {
            showLocalizations(tripID);
        }catch (CursorIndexOutOfBoundsException e){
            Toast.makeText(this,"NO LOCATIONS AVAIABLE", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToMainMenu(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToShowPhoto(View view){
        Intent intent = new Intent(this,DisplayPhotoActivity.class);
        intent.putExtra("locId",tripID);
        intent.putExtra("showAll",true);
        startActivity(intent);

    }

    public void showLocalizations(int tripId)
    {
        List<Localization> localizations;
        localizations = db.getLocalizationsById(tripId);
        for(Localization l:localizations){
            Log.i(LOG,"----CURRENT LOCALIZATION DATA----\n" + "ID: " + l.getLoc_ID() + "\nName: " + l.getLoc_name() +
            "\nLatitude: "+l.getLoc_latitude()+"\nLongitude: "+l.getLoc_longitude()+"\nTripIdFk: "+l.getTrip_ID_fk());
            markerLocalization = new LatLng(l.getLoc_latitude(), l.getLoc_longitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocalization,13));
            Marker marker = mMap.addMarker(new MarkerOptions().position(markerLocalization).title(l.getLoc_name()));
            marker.setTag(l.getLoc_ID());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        showTripData();
        setupMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        final int tmpLocId = (int) marker.getTag();
        Log.i(LOG,"----TMP LOC ID----" + String.valueOf(tmpLocId));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i(LOG,"----CLICKED ON MARKET TITLE WITH LOC ID: " + String.valueOf(tmpLocId));
                sendDataToDisplayPhoto(tmpLocId);

            }
        });
        return false;
    }
    public void sendDataToDisplayPhoto(int locId) {
        Intent intent = new Intent(this,DisplayPhotoActivity.class);
        intent.putExtra("locId",locId);
        intent.putExtra("showAll",false);
        startActivity(intent);
    }
}
