package com.skyyo.template.application.repositories.auth

import com.skyyo.template.application.models.remote.SignInRequest
import com.skyyo.template.application.models.remote.SignUpRequest
import com.skyyo.template.application.models.remote.SocialSignInRequest
import com.skyyo.template.application.network.calls.AuthCalls
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.utils.extensions.tryOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authCalls: AuthCalls,
    private val dataStoreManager: DataStoreManager
) {

    suspend fun signIn(request: SignInRequest): Boolean {
        val response = tryOrNull { authCalls.signIn(request) }
        // TODO do conversions/store tokens etc.
        response ?: return false
        return true
    }

    suspend fun signUp(signUpRequest: SignUpRequest): Boolean {
        val response = tryOrNull { authCalls.signUp(signUpRequest) }
        // TODO do conversions/store tokens etc.
        return response != null
    }

    suspend fun authGoogle(request: SocialSignInRequest): SocialSignInResult {
        val response = tryOrNull { authCalls.authGoogle(request) }
        response ?: return SocialSignInResult.NetworkError
        // TODO do conversions/store tokens etc.
        return if (response.firstLogin) SocialSignInResult.SuccessFirstTime else SocialSignInResult.Success
    }

    suspend fun authApple(request: SocialSignInRequest): SocialSignInResult {
        val response = tryOrNull { authCalls.authApple(request) }
        response ?: return SocialSignInResult.NetworkError
        // TODO do conversions/store tokens etc.
        return if (response.firstLogin) SocialSignInResult.SuccessFirstTime else SocialSignInResult.Success
    }

    suspend fun authFacebook(request: SocialSignInRequest): SocialSignInResult {
        val response = tryOrNull { authCalls.authFacebook(request) }
        response ?: return SocialSignInResult.NetworkError
        // TODO do conversions/store tokens etc.
        return if (response.firstLogin) SocialSignInResult.SuccessFirstTime else SocialSignInResult.Success
    }
}
