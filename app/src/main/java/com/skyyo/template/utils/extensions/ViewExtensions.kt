package com.skyyo.template.utils.extensions

import android.view.View
import androidx.core.view.ViewCompat

@Suppress("Deprecation")
fun View.changeSystemBars(light: Boolean) =
    ViewCompat.getWindowInsetsController(this)?.let { controller ->
        if (controller.isAppearanceLightStatusBars != light) {
            controller.isAppearanceLightNavigationBars = light
            controller.isAppearanceLightStatusBars = light
        }
    }
