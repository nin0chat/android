package dev.nin0.chat.api.model.gateway.payload

import kotlinx.serialization.Serializable

@Serializable
data class SendMessage(
    val content: String
)