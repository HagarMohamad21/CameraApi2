package com.example.cameraapi2.FaceRecognition


import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import com.example.cameraapi2.Utils.Graphic
import com.example.cameraapi2.Utils.GraphicalOverlay

class FaceGraphics(
    private var graphicalOverlay: GraphicalOverlay,
    var context: Context, var isFrontFacing: Boolean
) : Graphic(graphicalOverlay) {

    @Volatile
    private var mFaceData: FaceData? = null
    private val mHintTextPaint: Paint? = null
    private val mHintOutlinePaint: Paint? = null
    private val mEyeWhitePaint: Paint? = null
    private val mIrisPaint: Paint? = null
    private val mEyeOutlinePaint: Paint? = null
    private val mEyelidPaint: Paint? = null
    private val mPigNoseGraphic: Drawable? = null
    private val mMustacheGraphic: Drawable? = null
    private val mHappyStarGraphic: Drawable? = null
    private val mHatGraphic: Drawable? = null
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

    }

    private fun initPaints() {

    }


    override fun onDraw(canvas: Canvas) {
// confirm face date is still available
        if (mFaceData == null)
            return
        //face position
        var detectPos = mFaceData!!.position
        var leftEyePos=mFaceData!!.leftEyePosition
        var rightEye=mFaceData!!.rightEyePosition
        var nosePos= mFaceData!!.noseBasePosition
        var mouthLeftPos=mFaceData!!.mouthLeftPosition
        var mouthRightPos=mFaceData!!.mouthRightPosition
        var mouthBottomPos=mFaceData!!.mouthBottomPosition


    }

    fun update(faceData: FaceData) {
        mFaceData = faceData
        postInvalidate()
    }
}