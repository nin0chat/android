package dev.nin0.chat.api.gateway.event

import dev.nin0.chat.api.model.gateway.event.History

data class MessageHistoryEvent(
    val data: History
): Event