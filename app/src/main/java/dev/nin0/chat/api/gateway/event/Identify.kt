package dev.nin0.chat.api.gateway.event

import dev.nin0.chat.api.model.User

data class IdentifyEvent(
    val data: User
): Event