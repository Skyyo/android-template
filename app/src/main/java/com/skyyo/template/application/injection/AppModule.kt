package com.skyyo.template.application.injection

import android.content.Context
import androidx.datastore.createDataStore
import androidx.datastore.preferences.createDataStore
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.application.persistance.PaymentMethodsProtoStoreManager
import com.skyyo.template.protobuff.PaymentMethodsSerializer
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providePaymentMethodsProtoStoreManager(@ApplicationContext context: Context): PaymentMethodsProtoStoreManager =
        PaymentMethodsProtoStoreManager(
            context.createDataStore(
                fileName = "PaymentMethodsSerializer.pb",
                serializer = PaymentMethodsSerializer
            )
        )

    @Singleton
    @Provides
    fun provideUnauthorizedEventDispatcher(): UnauthorizedEventDispatcher =
        UnauthorizedEventDispatcher()
}
