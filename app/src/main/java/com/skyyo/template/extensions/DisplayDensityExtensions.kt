package com.skyyo.template.extensions

import android.content.res.Resources

const val ZERO_FIVE = 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

val scaledDensity: Float
    get() = Resources.getSystem().displayMetrics.scaledDensity

fun Float.dp(): Float = this * density + ZERO_FIVE

fun Int.dp(): Int = (this * density + ZERO_FIVE).toInt()

fun Int.sp(): Float = this * scaledDensity + ZERO_FIVE
