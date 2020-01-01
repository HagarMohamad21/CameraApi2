package com.example.cameraapi2.FaceRecognition

import android.graphics.PointF

data class FaceData(
    var id :Int=-1,
    // Face dimensions
    var position: PointF? = null,
    var width :Float=0f,
    var height :Float= 0f,
    // Head orientation
    var eulerY:Float = 0f,
    var eulerZ:Float = 0f,
    // Facial states
    var isLeftEyeOpen:Boolean = false,
    var isRightEyeOpen:Boolean = false,
    var isSmiling:Boolean = false,
    // Facial landmarks
    var leftEyePosition: PointF? = null,
    var rightEyePosition: PointF? = null,
    var leftCheekPosition: PointF? = null,
    var rightCheekPosition: PointF? = null,
    var noseBasePosition: PointF? = null,
    var leftEarPosition: PointF? = null,
    var leftEarTipPosition: PointF? = null,
    var rightEarPosition: PointF? = null,
    var rightEarTipPosition: PointF? = null,
    var mouthLeftPosition: PointF? = null,
    var mouthBottomPosition: PointF? = null,
    var mouthRightPosition: PointF? = null

)
