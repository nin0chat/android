package dev.nin0.chat.api.model

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * [Instant] that can be represented as a unix timestamp (in milliseconds)
 */
internal typealias UnixInstant = @Serializable(with = UnixInstantSerializer::class) Instant

/**
 * Serializes unix timestamps into [Instant]s
 */
internal object UnixInstantSerializer: KSerializer<Instant> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("kotlinx.datetime.Instant", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.fromEpochMilliseconds(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        return encoder.encodeLong(value.toEpochMilliseconds())
    }

}