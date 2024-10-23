package dev.nin0.chat.api.model.gateway.event

import dev.nin0.chat.api.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class History(
    val history: List<Message>
)