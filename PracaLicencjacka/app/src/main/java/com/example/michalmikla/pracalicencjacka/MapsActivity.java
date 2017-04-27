package com.example.michalmikla.pracalicencjacka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    private Marker myMarker;
    private TextView lat, lng;
    Localization localization;
    private Location mLocation;
    private LatLng yourLocalization;
    private LocationManager locationManager;
    public static GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    LatLng lastKnownLocation;
    private double latitude, longitude;
    private List<LatLng> locationHistory = new ArrayList<LatLng>();
    Polyline line;
    private static final int currentZoom = 15;
    Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this,this)
                    .build();
        }

        lat = (TextView) findViewById(R.id.textViewLatitude);
        lng = (TextView) findViewById(R.id.textViewLongitude);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        refreshMaps(mMap);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission problem !", Toast.LENGTH_SHORT).show();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation!=null){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        lat.setText(String.valueOf("LAT: "+latitude));
        lng.setText(String.valueOf("LON: "+longitude));
        startLocationUpdates();
        try{
            locationHistory.add(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        }
        catch (NullPointerException e)
        {
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
            LatLng lastLoc=new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(yourLocalization).title("You are here"));

        }
        catch(java.lang.NullPointerException e){

        }
        line=mMap.addPolyline(new PolylineOptions()
                .addAll(locationHistory)
                .width(5)
                .color(Color.RED));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent intent = new Intent(this, MarkerActivity.class);
        LatLng position = marker.getPosition();
        Double markerLat = position.latitude;
        Double markerLon = position.longitude;
        intent.putExtra("title",marker.getTitle());
        intent.putExtra("lat",markerLat);
        intent.putExtra("lon",markerLon);
        startActivity(intent);
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}
