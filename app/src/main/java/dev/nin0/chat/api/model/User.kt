package dev.nin0.chat.api.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val roles: Roles,
    val id: String,
    val device: Device = Device.Unknown
)