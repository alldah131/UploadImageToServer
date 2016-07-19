package com.dahlstore.uploadimagetoserver;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private static final int ACTIVITY_START_CAMERA_APP  = 0;
    private static final int REQUEST_CODE_PERMISSION = 2;
    ImageView ivCamera, ivGallery, ivUpload, ivStar;
    CameraPhoto cameraPhoto;
    private String []  mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

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
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                    if(ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[0])
                            != MockPackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[1])
                                    != MockPackageManager.PERMISSION_GRANTED)
                          {

                              ActivityCompat.requestPermissions(MainActivity.this,
                                      mPermission, REQUEST_CODE_PERMISSION);

                }
            }


        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION)
        {
            if(grantResults.length == 2 &&
                grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == MockPackageManager.PERMISSION_GRANTED){
                callCameraApp();
        }

                }
                else {
                    Toast.makeText(MainActivity.this, "External write permission has not been granted, cannot save images",
                            Toast.LENGTH_SHORT).show();
                }
            }



    private void callCameraApp() {

               try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), ACTIVITY_START_CAMERA_APP);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Something wrong while taking photo", Toast.LENGTH_SHORT).show();

                }
            }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == ACTIVITY_START_CAMERA_APP){
                String photoPath = cameraPhoto.getPhotoPath();
                try {

                    Bitmap photoCapturedBitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    ivStar.setImageBitmap(photoCapturedBitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Something went wrong with requestSize", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, photoPath);
            }
        }
    }
}
