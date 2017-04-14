package com.example.michalmikla.pracalicencjacka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTripActivity extends AppCompatActivity {

    EditText tripNameEdit,tripDateEdit,tripNoteEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        tripNameEdit = (EditText)findViewById(R.id.editTextTripName);
        tripDateEdit = (EditText)findViewById(R.id.editTextStartingDate);
        tripNoteEdit = (EditText)findViewById(R.id.editTextTripNote);
    }
    public boolean validate(EditText editText)
    {
        if(editText.getText()==null)
        {
            Toast.makeText(this,"Fill requested fields !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    public void createTrip()
    {

    }

    public void backToMainMenu(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
