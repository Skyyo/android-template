package com.skyyo.template.application.persistance

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.skyyo.template.application.models.local.PaymentMethod
import com.skyyo.template.datastore.PaymentMethodProto
import com.skyyo.template.datastore.PaymentMethods
import com.skyyo.template.protobuff.PaymentMethodsSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

val Context.paymentMethodsDataStore: DataStore<PaymentMethods> by dataStore(
    fileName = "paymentMethods.pb",
    serializer = PaymentMethodsSerializer
)

@Singleton
class PaymentMethodsProtoStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val paymentMethodProtoStore = context.paymentMethodsDataStore

    // TODO use only proto generated model if possible
    suspend fun getPaymentMethods(): List<PaymentMethod>? =
        paymentMethodProtoStore.data.firstOrNull()?.paymentMethodList?.map { protoModel ->
            PaymentMethod(
                id = protoModel.id,
                type = protoModel.type,
                isDefault = protoModel.isDefault,
            )
        }?.toList()

    suspend fun updatePaymentMethods(paymentMethods: List<PaymentMethod>) {
        val protoList = paymentMethods.map { kotlinModel ->
            PaymentMethodProto.newBuilder()
                .setId(kotlinModel.id)
                .setType(kotlinModel.type)
                .setIsDefault(kotlinModel.isDefault)
                .build()
        }.asIterable()
        paymentMethodProtoStore.updateData { proto ->
            proto.toBuilder()
                .clearPaymentMethod()
                .addAllPaymentMethod(protoList)
                .build()
        }
    }
}
