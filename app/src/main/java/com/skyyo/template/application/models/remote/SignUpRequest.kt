package com.skyyo.template.application.models.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignUpRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String
)
