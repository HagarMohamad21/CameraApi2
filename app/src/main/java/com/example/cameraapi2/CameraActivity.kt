package com.example.cameraapi2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.vision.CameraSource

class CameraActivity : AppCompatActivity() {
    var cameraSource:CameraSource?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
