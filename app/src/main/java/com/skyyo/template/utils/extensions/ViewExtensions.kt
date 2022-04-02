package com.skyyo.template.utils.extensions

import android.view.KeyEvent
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.ViewCompat

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

@Suppress("Deprecation")
fun View.changeSystemBars(light: Boolean) =
    ViewCompat.getWindowInsetsController(this)?.let { controller ->
        if (controller.isAppearanceLightStatusBars != light) {
            controller.isAppearanceLightNavigationBars = light
            controller.isAppearanceLightStatusBars = light
        }
    }
