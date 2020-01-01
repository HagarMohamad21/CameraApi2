package com.example.cameraapi2.Utils

import android.graphics.Canvas
import com.google.android.gms.vision.CameraSource

abstract class Graphic(private var graphicalOverlay: GraphicalOverlay) {

    public abstract fun onDraw(canvas: Canvas)
    private fun scaleX(horizontal: Float): Float = horizontal * graphicalOverlay.widthScaleFactor
    private fun scaleY(vertical: Float): Float = vertical * graphicalOverlay.heightScaleFactor
    private fun translateX(x: Float): Float {
        return if (graphicalOverlay.facing == CameraSource.CAMERA_FACING_FRONT) {
            graphicalOverlay.width - scaleX(x)
        } else scaleX(x)
    }

    private fun translateY(y: Float) = scaleY(y)
     fun postInvalidate() {
        graphicalOverlay.postInvalidate()
    }

}