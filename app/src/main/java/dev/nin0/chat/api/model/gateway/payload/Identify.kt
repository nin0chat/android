package dev.nin0.chat.api.model.gateway.payload

import dev.nin0.chat.api.model.Device
import kotlinx.serialization.Serializable

@Serializable
data class Identify(
    val anon: Boolean,
    val username: String? = null,
    val token: String? = null,
    val device: Device
)