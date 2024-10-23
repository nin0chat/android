package dev.nin0.chat.api.gateway.event

import dev.nin0.chat.api.model.Message

data class MessageEvent(
    val data: Message
): Event