package com.toddler.footsteps.navbar

import android.graphics.*
import android.graphics.drawable.Drawable

class CustomPressDrawable(private val path: Path, private val pressedColor: Int) : Drawable() {
    private val paint = Paint().apply {
        color = pressedColor
        style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
