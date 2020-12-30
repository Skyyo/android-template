package com.skyyo.template.application.models.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignInRequest(
    val email: String,
    val password: String
)
