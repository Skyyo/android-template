package com.skyyo.template.application.persistance

import androidx.datastore.core.DataStore
import com.skyyo.template.application.models.local.PaymentMethod
import com.skyyo.template.datastore.PaymentMethodProto
import com.skyyo.template.datastore.PaymentMethods
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentMethodsProtoStoreManager @Inject constructor(
    private val paymentMethodProtoStore: DataStore<PaymentMethods>
) {
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
