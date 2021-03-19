package com.skyyo.template.application.repositories.auth

sealed class SocialSignInResult {
    object Success : SocialSignInResult()
    object SuccessFirstTime : SocialSignInResult()
    object NetworkError : SocialSignInResult()
}
