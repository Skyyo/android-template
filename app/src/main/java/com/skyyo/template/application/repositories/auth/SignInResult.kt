package com.skyyo.template.application.repositories.auth

sealed class SignInResult {
    object Success : SignInResult()
    object SuccessFirstTime : SignInResult()
    object NetworkError : SignInResult()
}
