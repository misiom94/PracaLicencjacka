package com.example.michalmikla.pracalicencjacka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

public class MarkerActivity extends AppCompatActivity {
    String name;
    double lat,lng;
    private final double DEFAULT_VALUE = 0;
    private EditText editTextTitle,editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        lat = intent.getDoubleExtra("lat",DEFAULT_VALUE);
        lng = intent.getDoubleExtra("lon",DEFAULT_VALUE);
        editTextTitle = (EditText)findViewById(R.id.editTextName);
        editTextLocation = (EditText)findViewById(R.id.editTextLocalization);
        editTextTitle.setHint(name);
        editTextLocation.setHint("Place localization: \n" + lat +"\n"+ lng);

    }

    public void backToMap(View view){
        this.finish();
    }

}
