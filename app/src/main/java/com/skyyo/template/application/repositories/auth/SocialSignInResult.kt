package com.skyyo.template.application.repositories.auth

sealed class SocialSignInResult

object SocialSignInSuccess : SocialSignInResult()
object SocialSignInSuccessFirstTime : SocialSignInResult()
object SocialSignInNetworkError : SocialSignInResult()
