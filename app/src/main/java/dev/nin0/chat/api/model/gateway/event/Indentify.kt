package dev.nin0.chat.api.model.gateway.event

import kotlinx.serialization.Serializable

@Serializable
data class Identify(
    val id: String,
    val username: String,
    val roles: String
)