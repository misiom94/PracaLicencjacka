package com.example.michalmikla.pracalicencjacka;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText tripNameEdit,tripDateEdit,tripNoteEdit;
    String tripName, tripDate, tripNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        tripNameEdit = (EditText)findViewById(R.id.editTextTripName);
        tripDateEdit = (EditText)findViewById(R.id.editTextStartingDate);
        tripNoteEdit = (EditText)findViewById(R.id.editTextTripNote);
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

                Intent tripIntentData = new Intent(this,MapsActivity.class);
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
