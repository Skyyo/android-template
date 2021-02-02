package com.skyyo.template.application.network

import com.skyyo.template.extensions.tryOrNull
import com.skyyo.template.application.models.remote.RefreshTokenRequest
import com.skyyo.template.application.network.calls.AuthCalls
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.Lazy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        return when (val accessToken = refreshToken()) {
            null -> {
                @Suppress("GlobalCoroutineUsage")
                GlobalScope.launch { unauthorizedEventDispatcher.requestDeauthorization() }
                null
            }
            else -> {
                response.request.newBuilder().header("Authorization", "Bearer $accessToken").build()
            }
        }
    }

    private fun refreshToken(): String? = runBlocking {
        val refreshToken = dataStoreManager.getRefreshToken() ?: return@runBlocking null
        val response = tryOrNull { apiCalls.get().refreshToken(RefreshTokenRequest(refreshToken)) }
        response ?: return@runBlocking null
        dataStoreManager.setTokens(response.accessToken, response.refreshToken)
        response.accessToken
    }
}
