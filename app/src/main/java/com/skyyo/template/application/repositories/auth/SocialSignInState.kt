package com.skyyo.template.application.repositories.auth

sealed class SocialSignInState

object SocialSignInSuccess : SocialSignInState()
object SocialSignInSuccessFirstTime : SocialSignInState()
object SocialSignInNetworkError : SocialSignInState()
