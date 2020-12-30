package com.skyyo.template.extensions

import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputEditText
import com.skyyo.template.utils.TopStartGravityDrawable

inline fun EditText.onImeDoneListener(crossinline onKeyDonePressed: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, keyEvent ->

        when {
            keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER -> onKeyDonePressed.invoke()
            actionId == EditorInfo.IME_ACTION_DONE -> onKeyDonePressed.invoke()
            actionId == EditorInfo.IME_ACTION_NEXT -> onKeyDonePressed.invoke()
        }
        false
    }
}

fun AppCompatTextView.addDrawableEnd(@DrawableRes drawable: Int?, drawablePadding: Int = 32) {
    val imgDrawable = drawable?.let { ContextCompat.getDrawable(this.context, it) }
    if (drawablePadding != 0) {
        this.compoundDrawablePadding = drawablePadding
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, imgDrawable, null)
}

fun AppCompatTextView.addDrawableStart(@DrawableRes drawable: Int?, drawablePadding: Int = 32) {
    val imgDrawable = drawable?.let { ContextCompat.getDrawable(this.context, it) }
    if (drawablePadding != 0) {
        this.compoundDrawablePadding = drawablePadding
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(imgDrawable, null, null, null)
}

fun AppCompatTextView.addDrawableStart(drawableIcon: Drawable?, drawablePadding: Int = 32) {
    val imgDrawable = drawableIcon?.let { drawableIcon }
    if (drawablePadding != 0) {
        this.compoundDrawablePadding = drawablePadding
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(imgDrawable, null, null, null)
}

fun AppCompatTextView.addDrawableStartEnd(
    @DrawableRes drawableLeft: Int,
    @DrawableRes drawableRight: Int,
    drawablePadding: Int = 32
) {
    val imgDrawableL = ContextCompat.getDrawable(this.context, drawableLeft)
    val imgDrawableR = ContextCompat.getDrawable(this.context, drawableRight)
    if (drawablePadding != 0) {
        this.compoundDrawablePadding = drawablePadding
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(imgDrawableL, null, imgDrawableR, null)
}

fun AppCompatTextView.addDrawableTop(@DrawableRes drawable: Int?, drawablePadding: Int = 16) {
    val imgDrawable = drawable?.let { ContextCompat.getDrawable(this.context, it) }
    if (drawablePadding != 0) {
        val p = drawablePadding.dp()
        this.setPadding(0, p, 0, p)
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, imgDrawable, null, null)
}

fun AppCompatTextView.addDrawableTopStartEnd(
    @DrawableRes drawableStart: Int?,
    @DrawableRes drawableEnd: Int?,
    drawablePadding: Int = 32
) {
    val imgDrawableStart = drawableStart?.let { ContextCompat.getDrawable(this.context, it) }
    val imgDrawableEnd = drawableEnd?.let { ContextCompat.getDrawable(this.context, it) }
    imgDrawableStart ?: return
    if (drawablePadding != 0) {
        this.compoundDrawablePadding = drawablePadding
    }
    val gravityDrawable = TopStartGravityDrawable(imgDrawableStart)
    imgDrawableStart.setBounds(0, 0, imgDrawableStart.intrinsicWidth, imgDrawableStart.intrinsicHeight)
    gravityDrawable.setBounds(0, 0, imgDrawableStart.intrinsicWidth, imgDrawableStart.intrinsicHeight)
    setCompoundDrawablesRelativeWithIntrinsicBounds(gravityDrawable, null, imgDrawableEnd, null)
}

fun TextInputEditText.addDrawableEnd(@DrawableRes drawable: Int?, drawablePadding: Int = 32) {
    val imgDrawable = drawable?.let { ContextCompat.getDrawable(this.context, it) }
    if (drawablePadding != 0) {
        this.compoundDrawablePadding = drawablePadding
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, imgDrawable, null)
}

fun TextInputEditText.addDrawableTop(@DrawableRes drawable: Int?, drawablePadding: Int = 16) {
    val imgDrawable = drawable?.let { ContextCompat.getDrawable(this.context, it) }
    if (drawablePadding != 0) {
        val p = drawablePadding.dp()
        this.setPadding(0, p, 0, p)
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, imgDrawable, null, null)
}

inline fun AppCompatTextView.setOnDrawableStartClickListener(
    crossinline onDrawableStartClicked: () -> Unit
) {
    val extraClickSpace = 48
    this.setOnTouchListener { _, event ->
        val drawableStart = 0
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (context.isRtlLayoutDirection()) {
                    if (event.x >= width - this.compoundDrawablesRelative[drawableStart].bounds.width() - extraClickSpace) {
                        onDrawableStartClicked.invoke()
                        return@setOnTouchListener true
                    }
                } else {
                    if (event.x <= this.compoundDrawablesRelative[drawableStart].bounds.width() + extraClickSpace) {
                        onDrawableStartClicked.invoke()
                        return@setOnTouchListener true
                    }
                }
            }
        }
        true
    }
}

inline fun AppCompatTextView.setOnDrawableEndClickListener(
    crossinline onDrawableEndClicked: () -> Unit
) {
    val extraClickSpace = 48
    this.setOnTouchListener { _, event ->
        val drawableEnd = 2
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (context.isRtlLayoutDirection()) {
                    if (event.x <= this.compoundDrawablesRelative[drawableEnd].bounds.width() + extraClickSpace) {
                        onDrawableEndClicked.invoke()
                        return@setOnTouchListener true
                    }
                } else {
                    if (event.x >= width - this.compoundDrawablesRelative[drawableEnd].bounds.width() - extraClickSpace) {
                        onDrawableEndClicked.invoke()
                        return@setOnTouchListener true
                    }
                }
            }
        }
        true
    }
}

inline fun TextInputEditText.setOnDrawableEndClickListener(
    crossinline onDrawableEndClicked: () -> Unit
) {
    val extraClickSpace = 48
    this.setOnTouchListener { _, event ->
        val drawableEnd = 2
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (context.isRtlLayoutDirection()) {
                    if (event.x <= this.compoundDrawablesRelative[drawableEnd].bounds.width() + extraClickSpace) {
                        onDrawableEndClicked.invoke()
                        return@setOnTouchListener true
                    }
                } else {
                    if (event.x >= width - this.compoundDrawablesRelative[drawableEnd].bounds.width() - extraClickSpace) {
                        onDrawableEndClicked.invoke()
                        return@setOnTouchListener true
                    }
                }
            }
        }
        false
    }
}

inline fun AppCompatTextView.setOnDrawableStartEndClickListener(
    crossinline onDrawableStartClicked: () -> Unit,
    crossinline onDrawableEndClicked: () -> Unit
) {
    this.setOnTouchListener { _, event ->
        val drawableStart = 0
        val drawableEnd = 2
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (context.isRtlLayoutDirection()) {
                    if (event.x >= width - this.compoundDrawablesRelative[drawableStart].bounds.width()) {
                        onDrawableStartClicked.invoke()
                        return@setOnTouchListener true
                    }
                    if (event.x <= this.compoundDrawablesRelative[drawableEnd].bounds.width()) {
                        onDrawableEndClicked.invoke()
                        return@setOnTouchListener true
                    }
                } else {
                    if (event.x <= this.compoundDrawablesRelative[drawableStart].bounds.width()) {
                        onDrawableStartClicked.invoke()
                        return@setOnTouchListener true
                    }
                    if (event.x >= width - this.compoundDrawablesRelative[drawableEnd].bounds.width()) {
                        onDrawableEndClicked.invoke()
                        return@setOnTouchListener true
                    }
                }
            }
        }
        true
    }
}

fun View.detach() = (parent as? ViewGroup)?.removeView(this)

fun View.changeSystemBars(light: Boolean) =
    ViewCompat.getWindowInsetsController(this)?.let { controller ->
        if (controller.isAppearanceLightStatusBars != light) {
            controller.isAppearanceLightNavigationBars = light
            controller.isAppearanceLightStatusBars = light
        }
    }
