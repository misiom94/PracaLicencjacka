package com.example.michalmikla.pracalicencjacka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MarkerActivity extends AppCompatActivity {
    private final String LOG = "MARKER ACTIVITY";
    private int localizationFlag = 0;
    private static final int REQUEST_TAKE_PHOTO = 1;
    String name;
    double lat, lng;
    private int tripID,locId;
    private final double DEFAULT_VALUE = 0;
    private EditText editTextTitle, editTextLocation;
    private Button cameraButton;
    static protected String currentPhotoPath;
    private List<String> photoPathList = new ArrayList<>();
    private TableLayout tableLayout;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Intent mapIntent = getIntent();
        recieveDataFromMap(mapIntent);
        db = DatabaseHelper.getInstance(this);
        cameraButton = (Button) findViewById(R.id.buttonPhoto);
        editTextTitle = (EditText) findViewById(R.id.editTextName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocalization);
        editTextTitle.setHint(name);
        editTextLocation.setHint("Place localization: \n" + lat + "\n" + lng);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableLayout.setShrinkAllColumns(true);
        manageCameraButton();
    }

    public void recieveDataFromMap(Intent intent)
    {
        tripID = intent.getIntExtra("tripID",0);
        name = intent.getStringExtra("title");
        lat = intent.getDoubleExtra("lat", DEFAULT_VALUE);
        lng = intent.getDoubleExtra("lon", DEFAULT_VALUE);
        Log.i(LOG,"----Recieved data----" + "\nTrip id: " + tripID + "\nLocalization name: " + name + "\nLocalization lat: "
        + lat + "\nLocalization long: " + lng);
    }

    public void manageCameraButton()
    {
        if(localizationFlag!=0){
            cameraButton.setEnabled(true);
        }
        else{
            cameraButton.setEnabled(false);
        }
    }

    public void backToMap(View view)
    {
        finish();
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.i(LOG,"---CURRENT PHOTO PATH----\n" + currentPhotoPath);
        return image;
    }

    public void cameraClick(View view) throws FileNotFoundException {
        takePicureIntent();
    }

    public void takePicureIntent() throws FileNotFoundException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                photoPathList.add(photoFile.getAbsolutePath());
            }
        }
    }

    public void refreshTable() throws FileNotFoundException {
        tableLayout.removeAllViews();
        for (int i = 0; i < photoPathList.size(); i++) {
            String tmpPath = photoPathList.get(i);
            TableRow tr = new TableRow(this);
            ImageView iv = new ImageView(this);
            setPic(tmpPath, iv);
            tr.addView(iv);
            tableLayout.addView(tr);
            Toast.makeText(this, "TABLE REFRESHED !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPic(String path, ImageView imageview) throws FileNotFoundException {
        // Get the dimensions of the View
        int targetW = imageview.getWidth();
        int targetH = imageview.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //bmOptions.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image

        int scaleFactor = Math.min(15, 15);//photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        imageview.setImageBitmap(bitmap);
    }

    public void saveLocalizationToDatabase(View view)
    {
        name = String.valueOf(editTextTitle.getText());
        db.createLocalizationNoClass(lat, lng, name, tripID);
        Localization localization = db.getLastLocalization();
        locId = localization.getLoc_ID();
        Log.i(LOG,"----CREATED LOCALIZATION DATA----:\n ID: " + localization.getLoc_ID()+ "\n LAT: " + localization.getLoc_latitude()
        +"\n LNG: "+ localization.getLoc_longitude() + "\n TRIP ID: " + localization.getTrip_ID_fk());
        localizationFlag = 1;
        manageCameraButton();
    }

    public void savePhotoToDatabase(View view)
    {
        for(int i=0;i<photoPathList.size();i++)
        {
            db.createPhoto(photoPathList.get(i),locId);
            Photo photo = db.getLastPhoto();
            Log.i(LOG,"----CREATED PHOTO DATA----:\n ID:" + photo.getPhoto_ID()+ "\n PATH: " + photo.getPhoto_path() + "\n LocID: " +
            photo.getLoc_ID_fk());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            refreshTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


