package com.example.cameraapi2.FaceRecognition


import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.drawable.Drawable
import com.example.cameraapi2.R
import com.example.cameraapi2.Utils.Graphic
import com.example.cameraapi2.Utils.GraphicalOverlay

class FaceGraphics(
    private var graphicalOverlay: GraphicalOverlay,
    var context: Context, var isFrontFacing: Boolean
) : Graphic(graphicalOverlay) {

    @Volatile
    private var mFaceData: FaceData? = null
//    private val mHintTextPaint: Paint? = null
//    private val mHintOutlinePaint: Paint? = null
//    private val mEyeWhitePaint: Paint? = null
//    private val mIrisPaint: Paint? = null
//    private val mEyeOutlinePaint: Paint? = null
//    private val mEyelidPaint: Paint? = null
    private var catNose: Drawable? = null
//    private val mMustacheGraphic: Drawable? = null
//    private val mHappyStarGraphic: Drawable? = null
//    private val mHatGraphic: Drawable? = null
    // We want each iris to move independently,
// so each one gets its own physics engine.
    private val mLeftPhysics = EyePhysics()
    private val mRightPhysics = EyePhysics()
    private val mIsFrontFacing = false
    var resource: Resources = context.resources

    init {
        initPaints()
        initGraphics()
    }

    private fun initGraphics() {
      catNose=resource.getDrawable(R.drawable.cat_nose)
    }

    private fun initPaints() {

    }

    fun update(faceData: FaceData) {
        mFaceData = faceData
        postInvalidate()
    }
    override
    fun onDraw(canvas: Canvas) {
// confirm face date is still available
        var faceData: FaceData? = mFaceData ?: return
        //get the face data
        var detectPos = faceData!!.position
        var leftEyePosDetected = faceData!!.leftEyePosition
        var rightEyeDetected = faceData!!.rightEyePosition
        var nosePosDetected = faceData!!.noseBasePosition
        var mouthLeftPosDetected = faceData!!.mouthLeftPosition
        var mouthRightPosDetected = faceData!!.mouthRightPosition
        var mouthBottomPosDetected = faceData!!.mouthBottomPosition

        // face position ,dimension and angle
        var position = PointF(translateX(detectPos!!.x), translateY(detectPos.y))
        var width = scaleX(faceData!!.width)
        var height = scaleY(faceData!!.height)

        // eye coordinates
        var leftEyePos = PointF(translateX(leftEyePosDetected!!.x), translateY(leftEyePosDetected.y))
        var rightEyePos = PointF(translateX(rightEyeDetected!!.x), translateY(rightEyeDetected.y))

        //eye state
        var isLeftEyeOpen=faceData!!.isLeftEyeOpen
        var isRightEyeOpen=faceData!!.isRightEyeOpen

        //nose coordinates
        var nosePos=PointF(translateX(nosePosDetected!!.x),translateY(nosePosDetected.y)+100)

        //mouth coordinates
        var mouthLeftPos=PointF(translateX(mouthLeftPosDetected!!.x)
            ,translateY(mouthRightPosDetected!!.y))
        var mouthRightPos=PointF(translateX(mouthRightPosDetected!!.x),translateY(mouthRightPosDetected.y))
        var mouthBottomPos=PointF(translateX(mouthBottomPosDetected!!.x),translateY(mouthBottomPosDetected.y))

        // smile state
        var isSmiling=faceData!!.isSmiling

        // head tilt
        var eulerY=faceData!!.eulerY
        var eulerZ=faceData!!.eulerZ

 /*  calculate the distance between the eyes using Pythagoras' and
         we will use that distance to set the size of the eyes
         and the irises */
        var eyeRadiusProportion=0.45f
        var irisRadiusProportion=eyeRadiusProportion/2

        //drawNose
        drawNose(canvas,nosePos,leftEyePos,rightEyePos,width)

    }

    private fun drawNose(
        canvas: Canvas,
        nosePos: PointF,
        leftEyePos: PointF,
        rightEyePos: PointF,
        faceWidth: Float
    ) {
        val noseFaceWidthRatio:Float=1/2f
        var noseWidth=faceWidth*noseFaceWidthRatio
        var leftBound:Int=((nosePos.x)-(noseWidth/2)).toInt()
        var rightBound:Int=((nosePos.x)+(noseWidth/2)).toInt()
        var topBound:Int=((leftEyePos.y+rightEyePos.y)/2).toInt()
        var bottomBound:Int=(nosePos.y).toInt()

     catNose!!.setBounds(leftBound,topBound,rightBound,bottomBound)
     catNose!!.draw(canvas)

    }



}