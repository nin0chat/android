package dev.nin0.chat.api.model.gateway

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = OpCode.Serializer::class)
enum class OpCode(
    val code: Int,
    val canSend: Boolean
) {
    Unknown(-2, false),
    Error(-1, false),
    SendOrReceiveMessage(0, true),
    Identify(1, true),
    Heartbeat(2, true),
    History(3, false),
    UserListSync(4, false);

    override fun toString(): String {
        return "$name(${code})"
    }

    object Serializer: KSerializer<OpCode> {

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("OpCode", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): OpCode {
            val code = decoder.decodeInt()
            return entries.firstOrNull { it.code == code } ?: Unknown
        }

        override fun serialize(encoder: Encoder, value: OpCode) {
            return encoder.encodeInt(value.code)
        }

    }

}