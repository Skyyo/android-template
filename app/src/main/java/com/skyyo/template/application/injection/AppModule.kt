package com.skyyo.template.application.injection

import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideUnauthorizedEventDispatcher(): UnauthorizedEventDispatcher = UnauthorizedEventDispatcher()

    @DispatcherDefault
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @DispatcherIO
    @Provides
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DispatcherDefault

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DispatcherIO
