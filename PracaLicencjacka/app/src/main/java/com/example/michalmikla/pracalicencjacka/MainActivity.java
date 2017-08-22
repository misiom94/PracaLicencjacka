package com.example.michalmikla.pracalicencjacka;
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
//    PIXABAY - stronka z darmowymi zdjeciami
    private static final String LOG = "MAIN ACTIVITY";
    private static  DatabaseHelper db;
    Trip trip, takentrip;
    Button showDetails;
    private String result;
    Cursor c;
    Localization localization;
    Photo photo;
    List<Trip> tripList = new ArrayList<Trip>();
    private TableLayout tableView;
    TranslateAnimation ta1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        tableView = (TableLayout) findViewById(R.id.TableLayout);
        System.out.println("DATABASE NAME: " + db.getDatabaseName());
        try{
            tripList = db.getAllTrips();
            Log.i(LOG,"TRIPS LENGTH: "+tripList.size());
            for(final Trip trip:tripList)
            {
                TableRow tr = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,Gravity.CENTER_HORIZONTAL);
                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
                tr.setLayoutParams(lp);
                TextView tvLeft = new TextView(this);
                Typeface face = Typeface.createFromAsset(getAssets(),
                        "fonts/njnaruto.ttf");
                tvLeft.setTypeface(face);
                tvLeft.setLayoutParams(lp);
                tvLeft.setTextSize(15);
                tvLeft.setTextColor(Color.BLACK);
                tvLeft.setText(trip.getTrip_title());
                tr.addView(tvLeft);
                final Button showDetails = (Button)getLayoutInflater().inflate(R.layout.button_style, null);
                ta1=new TranslateAnimation(0,200,0,0);
                ta1.setDuration(1000); //1 second
                showDetails.setTextSize(20);
                showDetails.setTypeface(face);
                showDetails.setLayoutParams(buttonParams);
                showDetails.setText("Show details");
                showDetails.setTextColor(Color.BLACK);
                showDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDetails.startAnimation(ta1);
                        ta1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                goToShowTripActivity(trip);
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                        });
                    }
                });
                tr.addView(showDetails);
                tableView.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            }
        }catch(Exception e)
        {
            Log.e("MA","TABLE NOT CREATED");
        }

    }

    public void goToMapActivity(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToShowTripActivity(Trip trip){
        Intent intent = new Intent(this,DisplayTripActivity.class);
        intent.putExtra("tripId",trip.getTrip_id());
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
