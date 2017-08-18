package com.example.michalmikla.pracalicencjacka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DisplayPhotoActivity extends AppCompatActivity {
    private static final String LOG = "DISPLAY PHOTO ACTIVITY";
    boolean displayFlag;
    int locId;
    String locName;
    TableLayout tableLayout;
    DatabaseHelper db;
    List<Photo> photos = new ArrayList<>();
    Localization localization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        Intent displayIntent = getIntent();
        recieveDataFromDisplay(displayIntent);
        displayPhotos(displayFlag,locId);
        try {
            refreshTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void recieveDataFromDisplay(Intent intent){
        locId = intent.getIntExtra("locId",0);
        displayFlag = intent.getBooleanExtra("showAll",true);
        Log.i(LOG,"----RECIEVED DATA FROM DISPLAY TRIP: \nLOC ID: " + locId +"\n DISPLAY FLAG: " + displayFlag );

    }

    public void backToDisplayTrip(View view){
        finish();
    }

    public void displayPhotos(boolean displayFlag,int locId){
        db = DatabaseHelper.getInstance(this);
        photos = db.getPhotosById(locId);
        localization = db.getLocalizationById(locId);
        if(photos.size()!=0)
        {
            for(Photo p:photos){
                Log.i(LOG,"----RECIEVED PHOTO DATA FOR CURRENT MARKER----\n"+p.getPhoto_ID()+"\n"+p.getPhoto_path()+"\n"+p.getLoc_ID_fk());
            }
        }
    }
    public void refreshTable() throws FileNotFoundException {
        tableLayout.removeAllViews();
        TableRow.LayoutParams displayParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        displayParams.setMargins(0,15,0,0);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(100,0,0,0);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        for(final Photo p:photos) {

            String tmpPath = p.getPhoto_path();
            TableRow row = new TableRow(this);
            row.setLayoutParams(lp);
            TextView displayText = new TextView(this);
            displayText.setGravity(Gravity.CENTER_HORIZONTAL);
            displayText.setLayoutParams(displayParams);
            String date = p.getPhoto_date();
            Log.i(LOG,"table date: " + date);
            displayText.setText("Created at: " + p.getPhoto_date());
            row.addView(displayText);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(lp);
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            setPic(tmpPath, iv);
            tr.addView(iv);
            TableRow buttonRow = new TableRow(this);
//            buttonRow.setLayoutParams(lp);
            buttonRow.setGravity(Gravity.CENTER_HORIZONTAL);
            Button button = new Button(this);
            button.setText("Delete");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletePhoto(p);
                }
            });
            buttonRow.addView(button);
            tableLayout.addView(row);
            tableLayout.addView(tr);
            tableLayout.addView(buttonRow);
        }
    }
    private void deletePhoto(Photo photo){
        db.deletePhoto(photo.getPhoto_ID());
        try {
            refreshTable();
            Log.i(LOG,"PHOTO" +photo.getPhoto_ID()+" DELETED !");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setPic(String path, ImageView imageview) throws FileNotFoundException {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        imageview.setImageBitmap(bitmap);
        imageview.setRotation(90);
    }

}
