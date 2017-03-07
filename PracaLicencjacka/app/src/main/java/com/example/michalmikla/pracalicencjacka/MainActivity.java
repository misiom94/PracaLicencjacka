package com.example.michalmikla.pracalicencjacka;
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
