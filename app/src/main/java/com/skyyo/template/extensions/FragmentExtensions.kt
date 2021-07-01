package com.skyyo.template.extensions

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.skyyo.template.application.MainActivity

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

// Used to navigate to the top destination of first bottom navigation parent
// instead of the the root itself.
// Basically a workaround for issue https://issuetracker.google.com/issues/188963883
fun Fragment.interceptBackPress() {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).returnToTopOfRootStack()
            }
        }
    )
}
