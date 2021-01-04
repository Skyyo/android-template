package com.skyyo.template.protobuff

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.skyyo.template.datastore.PaymentMethods
import java.io.InputStream
import java.io.OutputStream

object PaymentMethodsSerializer : Serializer<PaymentMethods> {
    override val defaultValue: PaymentMethods = PaymentMethods.getDefaultInstance()

    override fun readFrom(input: InputStream): PaymentMethods {
        try {
            return PaymentMethods.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: PaymentMethods, output: OutputStream) = t.writeTo(output)
}
