package com.example.cameraapi2

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cameraapi2.FaceRecognition.FaceTracker
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    private val RC_HANDLE_GMS = 9001
    // permission request codes need to be < 256
    private val RC_HANDLE_CAMERA_PERM = 255

    private var mCameraSource: CameraSource? = null
    private var mIsFrontFacing = true


    // Activity event handlers
    // =======================

    // Activity event handlers
// =======================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Start using the camera if permission has been granted to this app,
// otherwise ask for permission to use it.
        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }
    }

    private val mSwitchCameraButtonListener =
        View.OnClickListener {
            mIsFrontFacing = !mIsFrontFacing
            if (mCameraSource != null) {
                mCameraSource!!.release()
                mCameraSource = null
            }
            createCameraSource()
            startCameraSource()
        }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()
        cameraPreview.stop()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean("IsFrontFacing", mIsFrontFacing)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCameraSource != null) {
            mCameraSource!!.release()
        }
    }

    // Handle camera permission requests
    // =================================

    // Handle camera permission requests
// =================================
    private fun requestCameraPermission() {
        val permissions =
            arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            createCameraSource()
            return
        }
        else return
    }

    // Camera source
    // =============

    // Camera source
// =============
    private fun createCameraSource() {
        val context = applicationContext
        val detector = createFaceDetector(context)
        var facing = CameraSource.CAMERA_FACING_FRONT
        if (!mIsFrontFacing) {
            facing = CameraSource.CAMERA_FACING_BACK
        }
        // The camera source is initialized to use either the front or rear facing camera.  We use a
// relatively low resolution for the camera preview, since this is sufficient for this app
// and the face detector will run faster at lower camera resolutions.
//
// However, note that there is a speed/accuracy trade-off with respect to choosing the
// camera resolution.  The face detector will run faster with lower camera resolutions,
// but may miss smaller faces, landmarks, or may not correctly detect eyes open/closed in
// comparison to using higher camera resolutions.  If you have any of these issues, you may
// want to increase the resolution.
        mCameraSource = CameraSource.Builder(context, detector)
            .setFacing(facing)
            .setRequestedPreviewSize(320, 240)
            .setRequestedFps(60.0f)
            .setAutoFocusEnabled(true)
            .build()
    }

    private fun startCameraSource() {
        // Make sure that the device has Google Play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            applicationContext
        )
        if (code != ConnectionResult.SUCCESS) {
            val dlg =
                GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg.show()
        }
        if (mCameraSource != null) {
            try {
                cameraPreview.start(mCameraSource, graphicOverlay)
            } catch (e: IOException) {
                mCameraSource!!.release()
                mCameraSource = null
            }
        }
    }

    // Face detector
    // =============

    // Face detector
// =============
    /**
     * Create the face detector, and check if it's ready for use.
     */
    @NonNull
    private fun createFaceDetector(context: Context): FaceDetector {
        val detector =
            FaceDetector.Builder(this)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setTrackingEnabled(true)
                .setMode(FaceDetector.FAST_MODE)
                .setProminentFaceOnly(mIsFrontFacing)
                .setMinFaceSize(if (mIsFrontFacing) 0.35f else 0.15f)
                .build()
        val factory: MultiProcessor.Factory<Face> =
            MultiProcessor.Factory<Face> { FaceTracker(this,  mIsFrontFacing,graphicOverlay) }
        val processor: Detector.Processor<Face> =
            MultiProcessor.Builder(factory).build()
        detector.setProcessor(processor)
        return detector
    }

}
