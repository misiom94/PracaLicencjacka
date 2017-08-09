package com.example.michalmikla.pracalicencjacka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener{

    private final String LOG = "MAPS_ACTIVITY ";
    private GoogleMap mMap;
    private TextView lat, lng;;
    private Location mLocation;
    private LatLng yourLocalization;
    private String tName,tDate,tNote;
    private int tId;
    public static GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private double latitude, longitude;
    private List<LatLng> locationHistory = new ArrayList<LatLng>();
    Polyline line;
    private static final int currentZoom = 15;
    Location mLastLocation;
    DatabaseHelper db;
    SupportMapFragment mapFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //declaration views to show localization
        lat = (TextView) findViewById(R.id.textViewLatitude);
        lng = (TextView) findViewById(R.id.textViewLongitude);
        setContentView(R.layout.activity_maps);
        setupMap();
        if(savedInstanceState!=null)
        {
            longitude = savedInstanceState.getDouble("longitude");
            latitude = savedInstanceState.getDouble("latitude");
            actualizeLocationView();
            Log.i(LOG,"----RECIEVED SAVED INSTANCE DATA----\n"+longitude+"\n"+latitude);
        }
        setupGoogleApiClient();
        Intent tripIntent = getIntent();
        recieveData(tripIntent);

        try{
            db = new DatabaseHelper(this);
        }catch(Exception e){
            Log.e(LOG, "DATABSE NOT CREATED !");
        }

    }
    private void setupMap(){
        if(mMap == null){
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void setupGoogleApiClient()
    {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
        }
    }

    public void actualizeLocationView()
    {
        lat.setText(String.valueOf("LAT: "+latitude));
        lng.setText(String.valueOf("LON: "+longitude));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
        refreshMaps(mMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        locationHistory.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        refreshMaps(mMap);
    }

    public void recieveData(Intent intent)
    {
        tId   = intent.getIntExtra("tripID",0);
        tName = intent.getStringExtra("tripName");
        tDate = intent.getStringExtra("tripDate");
        tNote = intent.getStringExtra("tripNote");
        Log.i(LOG, " -----Recieved data----\n Trip id: "+tId+"\nTrip name: "+tName+"\n"+"Trip date: "+tDate);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {

        saveInstanceState.putInt("tripId",tId);
        saveInstanceState.putString("tripName",tName);
        saveInstanceState.putString("tripDate",tDate);
        saveInstanceState.putString("tripNote",tNote);
        saveInstanceState.putDouble("latitude",latitude);
        saveInstanceState.putDouble("longitude",longitude);
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tId = savedInstanceState.getInt("tripId");
        tName = savedInstanceState.getString("tripName");
        tDate = savedInstanceState.getString("tripDate");
        tNote = savedInstanceState.getString("tripNote");
        latitude = savedInstanceState.getDouble("latitude");
        longitude = savedInstanceState.getDouble("longitude");

    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        refreshMaps(mMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission problem !", Toast.LENGTH_SHORT).show();
            return;
        }
        startLocationUpdates();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation!=null){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

        }
        try{
            locationHistory.add(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void refreshMaps(GoogleMap map)
    {
        try {
            yourLocalization = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            map.clear();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocalization,currentZoom));
            map.addMarker(new MarkerOptions().position(yourLocalization).title("You are here"));
            latitude = yourLocalization.latitude;
            longitude = yourLocalization.longitude;
            Log.i(LOG,"CURRENT LAT: "+latitude+"\n CURRENT LON: "+longitude);
            actualizeLocationView();
        }
        catch(java.lang.NullPointerException e){

        }
        line=mMap.addPolyline(new PolylineOptions()
                .addAll(locationHistory)
                .width(5)
                .color(Color.RED));
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(this, MarkerActivity.class);
        LatLng position = marker.getPosition();
        intent.putExtra("tripID",tId);
        intent.putExtra("title",marker.getTitle());
        intent.putExtra("lat",position.latitude);
        intent.putExtra("lon",position.longitude);
        startActivity(intent);
        return false;
    }
    public void goToMarker(View view)
    {
        Intent markerIntent = new Intent(this, MarkerActivity.class);
        startActivity(markerIntent);
        finish();
    }

    public void goToMenu(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
