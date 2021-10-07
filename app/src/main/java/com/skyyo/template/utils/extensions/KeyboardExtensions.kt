package com.skyyo.template.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

fun View?.hideKeyboard(activity: Activity?) {
    (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?)?.let {
        if (it.isActive) it.hideSoftInputFromWindow(this@hideKeyboard?.windowToken, 0)
    }
}

fun Fragment?.showKeyboard() {
    (this?.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        view?.findFocus(),
        InputMethodManager.SHOW_IMPLICIT,
    )
}

fun View.imeVisibilityListener(onVisibilityChanged: (Boolean) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        onVisibilityChanged(insets.isVisible(WindowInsetsCompat.Type.ime()))
        insets
    }
}
