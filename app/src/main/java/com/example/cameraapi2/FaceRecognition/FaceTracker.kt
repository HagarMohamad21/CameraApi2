package com.example.cameraapi2.FaceRecognition

import android.content.Context
import android.graphics.PointF
import com.example.cameraapi2.Utils.GraphicalOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.Landmark
import java.util.HashMap

class FaceTracker(var context: Context,
                  var isFrontFacing:Boolean,
                  var graphicOverlay:GraphicalOverlay)  : Tracker<Face>() {
    lateinit var faceGraphics: FaceGraphics
     var faceData= FaceData()
     var map:MutableMap<Int,PointF> =HashMap()
    var isLeftEyeOpenPrev=true
    var isRightEyeOpenPrev=true

    override fun onNewItem(p0: Int, p1: Face?) {
    faceGraphics= FaceGraphics(graphicOverlay,context,isFrontFacing)

    }

    override fun onMissing(p0: Detector.Detections<Face>?) {
       removeGraphics()
    }

    override fun onUpdate(p0: Detector.Detections<Face>?, face: Face?) {
        graphicOverlay.add(faceGraphics)
        updatePrevLandMarks(face!!)
        //set  face dimensions
       faceData.position=face.position
       faceData.width=face.width
       faceData.height=face.height
     //set head angles
        faceData.eulerY=face.eulerY
        faceData.eulerZ=face.eulerZ
        //set position of facial landmarks
        faceData.leftEyePosition=getLandMarkPos(face,Landmark.LEFT_EYE)
        faceData.rightEyePosition=getLandMarkPos(face,Landmark.RIGHT_EYE)
        faceData.mouthBottomPosition=getLandMarkPos(face,Landmark.BOTTOM_MOUTH)
        faceData.noseBasePosition=getLandMarkPos(face,Landmark.NOSE_BASE)
        faceData.mouthLeftPosition=getLandMarkPos(face, Landmark.LEFT_MOUTH)
        faceData.mouthRightPosition=getLandMarkPos(face,Landmark.RIGHT_MOUTH)
        var  eyeClosedThreshold=0.4f
        var leftEyeOpen=face.isLeftEyeOpenProbability

        if(leftEyeOpen==Face.UNCOMPUTED_PROBABILITY)
            faceData.isLeftEyeOpen=isLeftEyeOpenPrev

        else{
            faceData.isLeftEyeOpen=leftEyeOpen>eyeClosedThreshold
            isLeftEyeOpenPrev=faceData.isLeftEyeOpen
        }
       var smilingThreshold=.8f
        faceData.isSmiling=face.isSmilingProbability >smilingThreshold
        faceGraphics.update(faceData)


    }

    override fun onDone() {
        removeGraphics()
    }

    private fun removeGraphics() {
        graphicOverlay.remove(faceGraphics)
    }

    private fun updatePrevLandMarks(face:Face){
        for(landmark in face.landmarks){
            var position=landmark.position
            var propX=(position.x-face.position.x)/face.width
            var propY=(position.y-face.position.y)/face.height
            map.put(landmark.type, PointF(propX,propY))
        }
    }

    private fun getLandMarkPos(face:Face,landmarkId:Int):PointF?{
        for(landmark in face.landmarks){
            if (landmark.type==landmarkId){
                return landmark.position
            }
        }
        var landmarkPrevPos: PointF? = map[landmarkId] ?: return null

        var x=face.position.x+(landmarkPrevPos?.x!!*face.width)
        var y=face.position.y+(landmarkPrevPos?.y!!*face.height)
        return PointF(x,y)
    }
}