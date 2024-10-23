package dev.nin0.chat.api.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Device.Serializer::class)
enum class Device(val value: String) {
    Unknown("unknown"),
    Web("web"),
    Mobile("mobile"),
    Bot("bot");

    companion object {
        fun fromValue(value: String): Device {
            return entries.find { it.value == value.lowercase() } ?: Unknown
        }
    }

    object Serializer: KSerializer<Device> {

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("Device", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Device {
            return fromValue(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: Device) {
            return encoder.encodeString(value.value)
        }

    }

}