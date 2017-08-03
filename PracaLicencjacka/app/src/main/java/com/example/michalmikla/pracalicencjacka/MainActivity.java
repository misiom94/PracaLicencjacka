package com.example.michalmikla.pracalicencjacka;
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static  DatabaseHelper db;
    Trip trip, takentrip;
    Button showDetails;
    private String result;
    Cursor c;
    Localization localization;
    Photo photo;
    List<Trip> tripList = new ArrayList<Trip>();
    private TableLayout tableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        System.out.println("DATABASE NAME: " + db.getDatabaseName());
//        db.createTripNoClass(db, "ALLAH AKBAR","21.01.2015", (float) 5.5,"TEST TRIP!");
        try{
            tableView = (TableLayout) findViewById(R.id.TableLayout);
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,20);
            tr.setLayoutParams(lp);
            TextView tvLeft = new TextView(this);
            tvLeft.setLayoutParams(lp);
            tvLeft.setBackgroundColor(Color.RED);
            tvLeft.setText("OMG");
            tr.addView(tvLeft);

            showDetails = new Button(this);
            showDetails.setText("Show details");
            showDetails.setBackgroundColor(Color.BLUE);
            showDetails.setTextColor(Color.YELLOW);
            tr.addView(showDetails);

            tableView.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            Toast.makeText(this,"TABLE CREATED!",Toast.LENGTH_SHORT).show();
        }catch(Exception e)
        {
            Log.e("MA","TABLE NOT CREATED");
            Toast.makeText(this,"TABLE NOT CREATED!",Toast.LENGTH_SHORT).show();
        }


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

    public void addRow(TableLayout tableLayout){



    }

    @Override
    public void finish() {
        super.finish();
        db.close();
    }
}
