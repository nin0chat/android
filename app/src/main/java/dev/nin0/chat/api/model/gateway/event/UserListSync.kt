package dev.nin0.chat.api.model.gateway.event

import dev.nin0.chat.api.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserListSync(
    val users: List<User>
)