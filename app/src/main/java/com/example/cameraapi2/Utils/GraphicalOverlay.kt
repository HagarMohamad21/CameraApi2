package com.example.cameraapi2.Utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource

class GraphicalOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mLock = Any()
    public var previewHeight: Int = 0
    public var heightScaleFactor = 1.0f
    public var previewWidth: Int = 0
    public var widthScaleFactor = 1.0f
    public var facing = CameraSource.CAMERA_FACING_BACK
    private var graphics = HashSet<Graphic>()


    fun clear() {
        synchronized(mLock) {
            graphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: Graphic) {
        synchronized(mLock) {
            graphics.add(graphic)
        }
        postInvalidate()
    }

    fun remove(graphic: Graphic) {
        synchronized(mLock) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }

    fun setCameraInfo(prevWidth:Int,prevHeight:Int,fac:Int){
      synchronized(mLock){
          previewWidth=prevWidth
          previewHeight=prevHeight
          facing=fac
      }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if(previewHeight!=0&&previewWidth!=0){
            heightScaleFactor=(height/previewHeight).toFloat()
            widthScaleFactor=(width/previewWidth).toFloat() }
        for (graphic in graphics) {
            graphic.onDraw(canvas!!)
        }
    }

}