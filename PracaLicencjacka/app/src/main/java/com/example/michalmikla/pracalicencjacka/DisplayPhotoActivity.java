package com.example.michalmikla.pracalicencjacka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DisplayPhotoActivity extends AppCompatActivity {
    private static final String LOG = "DISPLAY PHOTO ACTIVITY";
    boolean displayFlag;
    int locId;
    TableLayout tableLayout;
    DatabaseHelper db;
    List<Photo> photos = new ArrayList<>();


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
        if(photos.size()!=0)
        {
            for(Photo p:photos){
                Log.i(LOG,"----RECIEVED PHOTO DATA FOR CURRENT MARKER----\n"+p.getPhoto_ID()+"\n"+p.getPhoto_path()+"\n"+p.getLoc_ID_fk());
            }
        }
    }
    public void refreshTable() throws FileNotFoundException {
        tableLayout.removeAllViews();
        for(Photo p:photos) {
            String tmpPath = p.getPhoto_path();
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            int leftMargin=20;
            int topMargin=200;
            int rightMargin=10;
            int bottomMargin=200;
            lp.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
            tr.setLayoutParams(lp);
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            setPic(tmpPath, iv);
            tr.addView(iv);
            tableLayout.addView(tr);
            Toast.makeText(this, "TABLE REFRESHED !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPic(String path, ImageView imageview) throws FileNotFoundException {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        imageview.setImageBitmap(bitmap);
        imageview.setRotation(90);
    }
}
