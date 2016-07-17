package com.dahlstore.uploadimagetoserver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    ImageView ivCamera, ivGallery, ivUpload, ivStar;
    CameraPhoto cameraPhoto;
    final int CAMERA_REQUEST = 13323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        

        cameraPhoto = new CameraPhoto(this);

        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
        ivStar = (ImageView) findViewById(R.id.ivStar);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Something wrong while taking photo", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    ivStar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Something went wrong with requestSize", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, photoPath);
            }
        }
    }




}
