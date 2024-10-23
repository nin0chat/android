package dev.nin0.chat.api.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = MessageType.Serializer::class)
enum class MessageType(val value: Int) {
    Unknown(-1),
    Default(0),
    Join(1),
    Leave(2),
    Automod(3),
    Bridge(4);

    companion object {
        fun fromValue(value: Int): MessageType {
            return entries.find { it.value == value } ?: Unknown
        }
    }

    object Serializer: KSerializer<MessageType> {

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("MessageType", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): MessageType {
            return fromValue(decoder.decodeInt())
        }

        override fun serialize(encoder: Encoder, value: MessageType) {
            return encoder.encodeInt(value.value)
        }

    }

}