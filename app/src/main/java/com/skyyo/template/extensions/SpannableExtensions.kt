package com.skyyo.template.extensions

import android.graphics.Typeface
import android.text.Selection
import android.text.Spannable
import android.text.TextPaint
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

fun Spannable.colorize(start: Int, end: Int, @ColorInt color: Int): Spannable {
    val span = ForegroundColorSpan(color)
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.clickable(start: Int, end: Int, onClick: () -> Unit): Spannable {
    val span = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
        }

        override fun onClick(p0: View) {
            onClick.invoke()
            Selection.setSelection((p0 as TextView).text as Spannable, 0)
            p0.invalidate()
        }
    }
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.strikeThrough(start: Int, end: Int): Spannable {
    val span = StrikethroughSpan()
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.underline(start: Int, end: Int): Spannable {
    val span = UnderlineSpan()
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.boldify(start: Int, end: Int): Spannable {
    val span = StyleSpan(Typeface.BOLD)
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.italize(start: Int, end: Int): Spannable {
    val span = StyleSpan(Typeface.ITALIC)
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.mark(start: Int, end: Int, @ColorInt color: Int): Spannable {
    val span = BackgroundColorSpan(color)
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun Spannable.sizeInDp(start: Int, end: Int, sizeInDp: Int): Spannable {
    val span = AbsoluteSizeSpan(sizeInDp)
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}
