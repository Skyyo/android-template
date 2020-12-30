package com.skyyo.template.application.models.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
    val firstLogin: Boolean
)
