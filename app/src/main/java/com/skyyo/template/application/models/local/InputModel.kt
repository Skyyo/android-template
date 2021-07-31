package com.skyyo.template.application.models.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputModel(
    var inputValue: String = "",
    var errorId: Int? = null
) : Parcelable
