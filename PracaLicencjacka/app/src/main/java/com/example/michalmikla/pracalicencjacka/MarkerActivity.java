package com.example.michalmikla.pracalicencjacka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MarkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

    }

    public void backToMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

}
