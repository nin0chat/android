package dev.nin0.chat.api.gateway.event

import dev.nin0.chat.api.model.gateway.event.UserListSync

data class UserListSyncEvent(
    val data: UserListSync
): Event