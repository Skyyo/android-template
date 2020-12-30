package com.skyyo.template.utils

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.skyyo.template.extensions.dp

class TopStartGravityDrawable(
    private val drawableIcon: Drawable
) : Drawable() {

    override fun getIntrinsicWidth() = drawableIcon.intrinsicWidth

    override fun getIntrinsicHeight() = drawableIcon.intrinsicHeight

    override fun draw(canvas: Canvas) {
        val halfCanvas = canvas.height / 2 - 30.dp()
        val halfDrawable = drawableIcon.intrinsicHeight / 2
        canvas.save()
        canvas.translate(0F, (-halfCanvas + halfDrawable).toFloat())
        drawableIcon.draw(canvas)
        canvas.restore()
    }

    override fun setColorFilter(p0: ColorFilter?) {}

    override fun setAlpha(p0: Int) {}

    override fun getOpacity() = PixelFormat.OPAQUE
}
