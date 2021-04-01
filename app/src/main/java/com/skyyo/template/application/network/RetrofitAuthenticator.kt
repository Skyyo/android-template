package com.skyyo.template.application.network

import com.skyyo.template.extensions.tryOrNull
import com.skyyo.template.application.models.remote.RefreshTokenRequest
import com.skyyo.template.application.network.calls.AuthCalls
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitAuthenticator @Inject constructor(
    private val apiCalls: Lazy<AuthCalls>,
    private val dataStoreManager: DataStoreManager,
    private val unauthorizedEventDispatcher: UnauthorizedEventDispatcher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val accessToken = getNewAccessToken()
            if (accessToken == null) {
                unauthorizedEventDispatcher.requestDeauthorization()
                null
            } else {
                response.request.newBuilder().header("Authorization", "Bearer $accessToken").build()
            }
        }
    }

    private suspend fun getNewAccessToken(): String? {
        val refreshToken = dataStoreManager.getRefreshToken() ?: return null
        val response = tryOrNull { apiCalls.get().refreshToken(RefreshTokenRequest(refreshToken)) }
        response ?: return null
        dataStoreManager.setTokens(response.accessToken, response.refreshToken)
        return response.accessToken
    }
}
