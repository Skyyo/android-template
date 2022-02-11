package com.skyyo.template.utils.extensions

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Stable
fun Modifier.enableRtlMirroring(): Modifier = composed {
    when (LocalLayoutDirection.current) {
        LayoutDirection.Rtl -> this.scale(scaleX = -1f, scaleY = 1f)
        else -> this
    }
}
