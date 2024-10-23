@file:Suppress("NOTHING_TO_INLINE")

package dev.nin0.chat.api.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@JvmInline
@Serializable(with = Roles.Serializer::class)
value class Roles(
    val bitmask: Long
) {

    /**
     * Checks if this set of roles is contained in the provided [roles]
     *
     * Enables use of the `in` operator
     */
    inline operator fun contains(roles: Roles): Boolean = (this and roles) == roles

    /**
     * Performs the bitwise `and` operator on the underlying bitmasks
     */
    inline infix fun and(roles: Roles): Roles = Roles(bitmask and roles.bitmask)

    /**
     * Combines the current roles with the provided [roles]
     */
    inline operator fun plus(roles: Roles): Roles = Roles(bitmask or roles.bitmask)

    companion object {

        val Guest = Roles(1 shl 0)
        val User = Roles(1 shl 1)
        val Bot = Roles(1 shl 2)
        val System = Roles(1 shl 3)
        val Mod = Roles(1 shl 4)
        val Admin = Roles(1 shl 5)

    }

    object Serializer: KSerializer<Roles> {

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("Roles", PrimitiveKind.LONG)

        override fun deserialize(decoder: Decoder): Roles {
            return Roles(decoder.decodeLong())
        }

        override fun serialize(encoder: Encoder, value: Roles) {
            return encoder.encodeLong(value.bitmask)
        }

    }

}