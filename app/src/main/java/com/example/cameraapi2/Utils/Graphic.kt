package com.example.cameraapi2.Utils

import android.graphics.Canvas
import com.google.android.gms.vision.CameraSource

abstract class Graphic(private var graphicalOverlay: GraphicalOverlay) {

     abstract fun onDraw(canvas: Canvas)
    fun scaleX(horizontal: Float): Float = horizontal * graphicalOverlay.widthScaleFactor
    fun scaleY(vertical: Float): Float = vertical * graphicalOverlay.heightScaleFactor
    fun translateX(x: Float): Float {
        return if (graphicalOverlay.facing == CameraSource.CAMERA_FACING_FRONT) {
            graphicalOverlay.width - scaleX(x)
        } else scaleX(x)
    }

    fun translateY(y: Float) = scaleY(y)
     fun postInvalidate() {
        graphicalOverlay.postInvalidate()
    }

}