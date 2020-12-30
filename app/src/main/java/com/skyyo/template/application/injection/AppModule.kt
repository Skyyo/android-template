package com.skyyo.template.application.injection

import android.content.Context
import androidx.datastore.preferences.createDataStore
import com.skyyo.template.application.network.calls.AuthCalls
import com.skyyo.template.application.repositories.auth.AuthRepository
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context.createDataStore(name = "TemplateDataStore"))

    @Singleton
    @Provides
    fun provideUnauthorizedEventDispatcher(): UnauthorizedEventDispatcher = UnauthorizedEventDispatcher()

    @Singleton
    @Provides
    fun provideAuthRepository(
        authCalls: AuthCalls,
        dataStoreManager: DataStoreManager
    ): AuthRepository = AuthRepository(authCalls, dataStoreManager)
}
