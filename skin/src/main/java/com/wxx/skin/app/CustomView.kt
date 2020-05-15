package com.wxx.skin.app

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.wxx.skin.utils.SkinResources
import com.wxx.skin.utils.SkinViewSupport

/**
 * @author ：wuxinxi on 2020/5/14 .
 * @packages ：com.wxx.skin .
 * TODO:一句话描述
 */
class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs), SkinViewSupport {

    private var windowWidth = 0
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintOut = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    private var circleInColor = Color.GRAY
    private var circleOutColor = Color.RED

    var circleInColorResId = 0
    var circleOutColorResId = 0

    var sweepAngle = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    init {
        paint.strokeWidth = 15f
        paint.style = Paint.Style.STROKE
        paintOut.strokeWidth = 16f
        paintOut.style = Paint.Style.STROKE
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
        //circleInColor = typedArray.getColor(R.styleable.CustomView_colorInColor, Color.GRAY)
        //paint.color = circleInColor
        circleInColorResId = typedArray.getResourceId(R.styleable.CustomView_colorInColor, 0)
        paint.color = context.resources.getColor(circleInColorResId)

        //circleOutColor = typedArray.getColor(R.styleable.CustomView_colorOutColor, Color.RED)
        //paintOut.color = circleOutColor
        circleOutColorResId = typedArray.getResourceId(R.styleable.CustomView_colorOutColor, 0)
        paintOut.color = context.resources.getColor(circleOutColorResId)

        paintOut.strokeCap = Paint.Cap.ROUND
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val displayMetrics = context.resources.displayMetrics
        windowWidth = displayMetrics.widthPixels
        radius = 100f
        centerX = windowWidth / 2f
        centerY = radius * 2
        val left = windowWidth / 2 - radius
        val top = centerY - radius
        val right = windowWidth / 2 + radius
        val bottom = centerY + radius
        rectF.set(left, top, right, bottom)

        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawArc(rectF, 0f, sweepAngle, false, paintOut)
    }


    private fun startAnimation() {
        val animation = ObjectAnimator.ofFloat(this, "sweepAngle", 0f, 360f)
        animation.duration = 2000
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = ValueAnimator.INFINITE
        animation.start()
    }

    override fun applySkin() {
        Log.d("wuxinxi", "applySkin: ")
        if (circleInColorResId != 0 && circleOutColorResId != 0) {
            paint.color = SkinResources.instance.getColor(circleInColorResId)
            paintOut.color = SkinResources.instance.getColor(circleOutColorResId)
            invalidate()
        } else if (circleInColorResId != 0) {
            paint.color = SkinResources.instance.getColor(circleInColorResId)
            invalidate()
        } else if (circleOutColorResId != 0) {
            paintOut.color = SkinResources.instance.getColor(circleOutColorResId)
            invalidate()
        }
    }

}