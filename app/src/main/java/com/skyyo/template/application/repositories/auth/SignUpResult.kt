package com.skyyo.template.application.repositories.auth

sealed class SignUpResult {
    object Success : SignUpResult()
    object NetworkError : SignUpResult()
}
