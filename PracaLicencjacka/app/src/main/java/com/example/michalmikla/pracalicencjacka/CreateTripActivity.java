package com.example.michalmikla.pracalicencjacka;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String LOG = "CreateTripActivity ";
    EditText tripNameEdit,tripDateEdit,tripNoteEdit;
    String tripName, tripDate, tripNote;
    DatabaseHelper db;
    List<Trip> trips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        tripNameEdit = (EditText)findViewById(R.id.editTextTripName);
        tripDateEdit = (EditText)findViewById(R.id.editTextStartingDate);
        tripNoteEdit = (EditText)findViewById(R.id.editTextTripNote);
        db = DatabaseHelper.getInstance(this);

    }


    public void createTrip(View view)
    {
            if (!validate(tripNameEdit)|| !validate(tripDateEdit))
            {
                Toast.makeText(this,"Bad data input",Toast.LENGTH_SHORT).show();
            }
            else
            {
                tripName = tripNameEdit.getText().toString();
                tripDate = tripDateEdit.getText().toString();
                tripNote = tripNoteEdit.getText().toString();

                try{
                    db.createTripNoClass(db,tripName,tripDate,0,tripNote);
                    Log.i(LOG," TRIP WAS CREATED !");

                }catch (Exception e){
                    e.printStackTrace();
                }
                Trip trip = db.selectTrip();
                Log.i("LAST TRIP: ",String.valueOf(trip.getTrip_id())+" "+String.valueOf(trip.getTrip_title()));
                Intent tripIntentData = new Intent(this,MapsActivity.class);
                tripIntentData.putExtra("tripID",trip.getTrip_id());
                tripIntentData.putExtra("tripName",tripName);
                tripIntentData.putExtra("tripDate",tripDate);
                tripIntentData.putExtra("tripNote",tripNote);
                startActivity(tripIntentData);
            }
    }


    public void backToMainMenu(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean validate(EditText editText) {
        if (editText.getText().toString().equals(String.valueOf(""))) {
            Toast.makeText(this, "Fill requested fields !", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    public void setDate(View view){
        DatePickerDialog dialog = new DatePickerDialog(this, this, 2015,5,15);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        tripDateEdit.setText(String.valueOf(day)+"."+String.valueOf(month)+"."+String.valueOf(year));
    }
}
