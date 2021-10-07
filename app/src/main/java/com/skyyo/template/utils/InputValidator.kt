package com.skyyo.template.utils

import com.skyyo.template.R
import com.skyyo.template.utils.extensions.isEmail
import com.skyyo.template.utils.extensions.isPassword

object InputValidator {
    private const val OTP_LENGTH = 4

    fun getEmailErrorIdOrNull(input: String): Int? = when {
        input.isEmpty() -> R.string.error_email_empty
        input.isEmail -> null
        else -> R.string.error_email_invalid
    }

    fun getPasswordErrorIdOrNull(input: String): Int? = when {
        input.isEmpty() -> R.string.error_password_empty
        input.isPassword -> null
        else -> R.string.error_password_invalid
    }
}
