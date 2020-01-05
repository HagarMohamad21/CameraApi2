package com.example.cameraapi2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.cameraapi2.FaceRecognition.FaceTracker
import com.example.cameraapi2.Utils.CameraPreview
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class CameraActivity : AppCompatActivity() {
    var cameraSource:CameraSource?=null
   var isFrontCameraOn=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       checkCameraPermission()

    }

    private fun checkCameraPermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),2000)
           else createCameraSource()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==2000){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //proceed
                createCameraSource()
            }
            else finish()
        }
    }

    private fun createCameraSource() {
    var detector=createFaceDetector()
        var facing=CameraSource.CAMERA_FACING_FRONT
       if(!isFrontCameraOn)
           facing=CameraSource.CAMERA_FACING_BACK
       cameraSource=CameraSource.Builder(this,detector)
           .setFacing(facing)
           .setRequestedPreviewSize(360,640)
           .setRequestedFps(60f)
           .setAutoFocusEnabled(true)
           .build()
    }

    private fun createFaceDetector():FaceDetector{
        var faceSize = if(isFrontCameraOn) .35f
        else .15f
       var detector=FaceDetector
           .Builder(this).
               setLandmarkType(FaceDetector.ALL_LANDMARKS).
               setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).
               setTrackingEnabled(true).
               setMode(FaceDetector.FAST_MODE).
               setProminentFaceOnly(isFrontCameraOn).
               setMinFaceSize(faceSize).
               build()

             var factory = MultiProcessor.Factory<Face> {
                 return@Factory FaceTracker(this,isFrontCameraOn,graphicOverlay)
             }
        var processor:Detector.Processor<Face> =MultiProcessor.Builder(factory).build()
        detector.setProcessor(processor)
        return detector
    }


    fun startCameraSource()
    {
        var code=GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
    if(code!=ConnectionResult.SUCCESS){
        var dialog=GoogleApiAvailability.getInstance().getErrorDialog(this,code,9001)
        dialog.show()
    }
    if(cameraSource!=null){
        try{
cameraPreview!!.start(cameraSource,graphicOverlay)
        }
        catch ( e:IOException){
    cameraSource!!.release()
            cameraSource=null
        }
    }

    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()
       cameraPreview.stop()
    }

}
