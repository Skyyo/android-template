package com.skyyo.template.extensions

import android.content.Context
import android.content.res.Configuration
import android.view.View

fun Context.isDarkThemeOn(): Boolean =
    this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun Context.isRtlLayoutDirection() =
    this.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
