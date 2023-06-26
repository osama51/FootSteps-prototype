package com.toddler.footsteps.navbar

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.toddler.footsteps.R
import kotlin.properties.Delegates

/**
 *
 *          Thanks to Nour El Islam SAIDI   https://github.com/IslamBesto
 *
 *                              (all credit goes to him)
 *
 * */

class CustomBottomNavBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private val offset: Int = 5
    private val mPath: Path
    private val mPathBack: Path
    private val mPaint: Paint
    private val mPaintBack: Paint

    /** the radius represent the radius of the fab button  */
    private var radius: Int = 0
    // the coordinates of the first curve
    private val mFirstCurveStartPoint = Point()
    private val mFirstCurveEndPoint = Point()
    private val mFirstCurveControlPoint1 = Point()
    private val mFirstCurveControlPoint2 = Point()

    //the coordinates of the second curve
    private var mSecondCurveStartPoint = Point()
    private val mSecondCurveEndPoint = Point()
    private val mSecondCurveControlPoint1 = Point()
    private val mSecondCurveControlPoint2 = Point()

    // Navigation bar bounds (width & height)
    private var mNavigationBarWidth: Int = 0
    private var mNavigationBarHeight: Int = 0

    private val clickableBounds = RectF()


    init {

        // radius of fab button
        radius = 180 / 2
        mPath = Path()
        mPathBack = Path()
        mPaint = Paint()
        mPaintBack = Paint()
        with(mPaint) {
            style = Paint.Style.FILL_AND_STROKE
            color = resources.getColor(R.color.white)
        }

        with(mPaintBack) {
            style = Paint.Style.FILL_AND_STROKE
            color = resources.getColor(R.color.sweet)
        }
        setBackgroundColor(Color.TRANSPARENT)

        mPath.computeBounds(clickableBounds, true)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // get width and height of navigation bar
        mNavigationBarWidth = width
        mNavigationBarHeight = height
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(mNavigationBarWidth / 2 - radius * 2 - radius / 3, 0)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(mNavigationBarWidth / 2, radius + radius / 4)
        // same thing for the second curve
        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint.set(mNavigationBarWidth / 2 + radius * 2 + radius / 3, 0)

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + radius + radius / 4,
            mFirstCurveStartPoint.y
        )
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x - radius * 2 + radius,
            mFirstCurveEndPoint.y
        )

        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + radius * 2 - radius,
            mSecondCurveStartPoint.y
        )
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (radius + radius / 4),
            mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, offset.toFloat())
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat() + offset)

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(), mFirstCurveControlPoint1.y.toFloat() + offset,
                mFirstCurveControlPoint2.x.toFloat(), mFirstCurveControlPoint2.y.toFloat() + offset,
                mFirstCurveEndPoint.x.toFloat(), mFirstCurveEndPoint.y.toFloat() + offset
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(), mSecondCurveControlPoint1.y.toFloat() + offset,
                mSecondCurveControlPoint2.x.toFloat(), mSecondCurveControlPoint2.y.toFloat() + offset,
                mSecondCurveEndPoint.x.toFloat(), mSecondCurveEndPoint.y.toFloat() + offset
            )

            lineTo(mNavigationBarWidth.toFloat(), offset.toFloat())
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())
            close()
        }

        mPathBack.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(), mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(), mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(), mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(), mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(), mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(), mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())
            close()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y


        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                if (clickableBounds.contains(x!!, y!!)) {
                    // display the press effect within the path (mPath)
                    mPaint.color = resources.getColor(R.color.sweet)
                    invalidate()
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                // display the normal state
                mPaint.color = resources.getColor(R.color.white)
                invalidate()

            }
        }

        return super.onTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPathBack, mPaintBack)


        canvas?.drawPath(mPath, mPaint)
    }
}

