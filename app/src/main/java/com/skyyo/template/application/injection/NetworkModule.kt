package com.skyyo.template.application.injection

import com.skyyo.template.BuildConfig
import com.skyyo.template.application.network.RetrofitAuthenticator
import com.skyyo.template.application.network.calls.AuthCalls
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitAuthenticator(
        apiCalls: Lazy<AuthCalls>,
        dataStoreManager: DataStoreManager,
        unauthorizedEventDispatcher: UnauthorizedEventDispatcher
    ): RetrofitAuthenticator =
        RetrofitAuthenticator(apiCalls, dataStoreManager, unauthorizedEventDispatcher)

    @Singleton
    @Provides
    fun provideHeaderInjector(dataStoreManager: DataStoreManager): Interceptor {
        return Interceptor { chain ->
            return@Interceptor chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(
                        "Authorization",
                        runBlocking { dataStoreManager.getAccessToken() ?: "" }
                    )
                    .build()
            )
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInjector: Interceptor,
        authenticator: RetrofitAuthenticator
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(headerInjector)
        addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        authenticator(authenticator)
        dispatcher(Dispatcher())
        connectTimeout(timeout = 10, TimeUnit.SECONDS)
        writeTimeout(timeout = 10, TimeUnit.SECONDS)
        readTimeout(timeout = 10, TimeUnit.SECONDS)
    }.build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideAuthCalls(retrofit: Retrofit): AuthCalls = retrofit.create(AuthCalls::class.java)
}
