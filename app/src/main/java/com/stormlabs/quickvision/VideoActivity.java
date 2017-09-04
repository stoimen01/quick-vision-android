package com.stormlabs.quickvision;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VideoActivity extends Activity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    static{
        System.loadLibrary("opencv_java3");
    }

    public static final String  TAG = "QuickVision";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private CameraBridgeViewBase mOpenCvCameraView;
    private VideoStreamer videoStreamer;
    private String serverIP;
    private int serverPort, size, quality;

    /* Activity lifecycle methods */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // getting info
        Intent intent = getIntent();
        serverIP = intent.getStringExtra(MainActivity.IP);
        serverPort = Integer.parseInt(intent.getStringExtra(MainActivity.PORT));
        size = Integer.parseInt(intent.getStringExtra(MainActivity.SIZE));
        quality = Integer.parseInt(intent.getStringExtra(MainActivity.QUALITY));

        // Setting up the camera view
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.image_manipulations_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableFpsMeter();

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Checking the camera permission and waiting for callback
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            mOpenCvCameraView.enableView();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Starting processing
                    mOpenCvCameraView.enableView();
                } else {
                    // Closing activity
                    this.finish();
                }
                break;
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) mOpenCvCameraView.disableView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) mOpenCvCameraView.disableView();
    }


    /* Camera lifecycle methods */

    @Override
    public void onCameraViewStarted(int width, int height) {

        // Setting up the video streamer
        try {
            videoStreamer = new VideoStreamer(serverIP, serverPort, 6000, size, quality);
            videoStreamer.start();
        }catch (SocketException|UnknownHostException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Network error occurred", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // Getting the frame as mat
        Mat rgba = inputFrame.rgba();
        videoStreamer.setLastFrame(rgba);
        return rgba;
    }


    @Override
    public void onCameraViewStopped() {
        videoStreamer.stopStreaming();
    }

}