package com.example.michalmikla.pracalicencjacka;

import android.Manifest;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private TextView lat, lng;
    Localization localization;
    private Location mLocation;
    private LatLng yourLocalization;
    private int currentZoom;
    private LocationManager locationManager;
    public static GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    LatLng lastKnownLocation;
    private double latitude, longitude;
    private List<LatLng> locationHistory;
    Polyline line;


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
                    .build();
            locationHistory = new ArrayList<LatLng>();
        }
        lat = (TextView) findViewById(R.id.textViewLatitude);
        lng = (TextView) findViewById(R.id.textViewLongitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        currentZoom = 12;
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        refreshMaps(googleMap);
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

    protected void startLocationUpdates() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             lng.setText("DONT HAVE PERMISSION");

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
        lat.setText("Connected!");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        lat.setText(String.valueOf(mLocation.getLatitude()));
        lng.setText(String.valueOf(mLastLocation.getLongitude()));

        refreshMaps(mMap);
        boolean mRequestingLocationUpdates=true;

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        try{
            locationHistory.add(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
            lat.setText(String.valueOf(latitude));
            lng.setText(String.valueOf(longitude));
        }
        catch (NullPointerException e)
        {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    Location mLastLocation;
    public void refreshMaps(GoogleMap map)
    {
        try {
            yourLocalization = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            map.clear();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocalization, map.getCameraPosition().zoom));
            LatLng lastLoc=new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(lastLoc).title("Marker in your localization"));
            map.addMarker(new MarkerOptions().position(yourLocalization).title("Marker in your localization"));

        }
        catch(java.lang.NullPointerException e){

        }
        System.out.println(locationHistory.size());
        line=mMap.addPolyline(new PolylineOptions()
                .addAll(locationHistory)
                .width(5)
                .color(Color.RED));
    }

    public void localizate(View view){
        refreshMaps(mMap);
        lat.setText(String.valueOf(latitude));
        lng.setText(String.valueOf(longitude));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
