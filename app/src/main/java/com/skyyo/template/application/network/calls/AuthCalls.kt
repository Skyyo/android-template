package com.skyyo.template.application.network.calls

import com.skyyo.template.application.models.remote.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthCalls {

    @POST("/api/Account/sign-up")
    suspend fun signUp(@Body body: SignUpRequest): Response<Unit>

    @POST("/api/Account/sign-in")
    suspend fun signIn(@Body body: SignInRequest): Response<SignInResponse>

    @POST("/api/Account/sign-in/google")
    suspend fun authGoogle(@Body body: SocialSignInRequest): SignInResponse

    @POST("/api/Account/sign-in/facebook")
    suspend fun authFacebook(@Body body: SocialSignInRequest): SignInResponse

    @POST("/api/Account/sign-in/appleId")
    suspend fun authApple(@Body body: SocialSignInRequest): SignInResponse

    @POST("/api/Account/sign-in/refresh")
    suspend fun refreshToken(@Body body: RefreshTokenRequest): SignInResponse
}
