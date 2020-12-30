package com.skyyo.template.application.models.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SocialSignInRequest(
    val idToken: String
)
