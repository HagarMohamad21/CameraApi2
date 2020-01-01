package com.example.cameraapi2.Utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class CameraPreview(context: Context, private var attrs:AttributeSet):ViewGroup(context,attrs) {
    private val TAG = "CameraPreview"
    var graphicalOverlay:GraphicalOverlay?=null
    var cameraSource: CameraSource?=null
    var startRequested=false
    var surfaceIsAvailable=false
    private var surfaceView:SurfaceView?=null

     init{
         surfaceView=SurfaceView(context)
         surfaceView?.holder?.addCallback(SurfaceCallback())
         addView(surfaceView)
     }

    @Throws(IOException::class)
     private fun start(cameraSource: CameraSource?){
        if(cameraSource==null) stop()

         this.cameraSource=cameraSource
         if(this.cameraSource!=null){
             startRequested=true
             startIfReady()
         }
     }

    @Throws(IOException::class)
    private fun startIfReady() {
       if(startRequested&&surfaceIsAvailable){
           cameraSource!!.start(surfaceView!!.holder)
           if(graphicalOverlay!=null){
               val size=cameraSource!!.previewSize
               val min= min(size.height,size.width)
               val max=max(size.height,size.width)
               graphicalOverlay!!.setCameraInfo(min,max,cameraSource!!.cameraFacing)
               graphicalOverlay!!.clear()
           }

           startRequested=false
       }
    }

    @Throws(IOException::class)
    private fun stop() {
        if(cameraSource!=null)
            cameraSource!!.stop()
    }

    @Throws(IOException::class)
    fun start(cameraSource: CameraSource?,graphicalOverlay: GraphicalOverlay){
     this.graphicalOverlay=graphicalOverlay
        if(cameraSource!=null){
            start(cameraSource)
        }
    }

    inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
                   surfaceIsAvailable=false        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
                 surfaceIsAvailable=true
            try{
                startIfReady()
            }
            catch (e:Exception){}
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
       var prevWidth=0
       var prevHeight=0
        if(cameraSource!=null){
            var size=cameraSource!!.previewSize
            if (size != null) {
                prevHeight=size.height
                prevWidth=size.width
            }
        }
        var viewHeight=b-t //bottom minus top of screen
        var viewWidth=r-l // right minus left of screen
        var childWidth=0
        var childHeight=0
        var childXOffest=0
        var childYOffest=0

        var widthRatio=viewWidth/prevWidth
        val heightRatio=viewHeight/prevHeight
        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
// it is usually necessary to slightly oversize the child and to crop off portions along one
// of the dimensions.  We scale up based on the dimension requiring the most correction, and
// compute a crop offset for the other dimension.
        if (widthRatio > heightRatio) {
            childWidth = viewWidth
            childHeight = prevHeight  * widthRatio
            childYOffest = (childHeight - viewHeight) / 2
        } else {
            childWidth = prevWidth * heightRatio
            childHeight = viewHeight
            childXOffest = (childWidth - viewWidth) / 2
        }
        for(i in 0 until childCount){
            getChildAt(i).layout(-1*childXOffest,-1*childYOffest,
                 childWidth-childXOffest,childHeight-childYOffest)
        }
        try {
            startIfReady()
        } catch (e: Exception) {
            Log.d(TAG, "onLayout: "+e.toString())
        }


    }

}