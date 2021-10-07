package com.skyyo.template.utils.extensions

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun Fragment.shortToast(text: String) {
    Toast.makeText(context ?: return, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(text: String) {
    Toast.makeText(context ?: return, text, Toast.LENGTH_LONG).show()
}

inline fun Fragment.askForMultiplePermissions(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
) = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
    val granted = result.map { it.value }.filter { it == false }
    if (granted.isEmpty()) onPermissionsGranted() else onDenied()
}

inline fun Fragment.askForSinglePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
) = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    if (it) onPermissionsGranted() else onDenied()
}
