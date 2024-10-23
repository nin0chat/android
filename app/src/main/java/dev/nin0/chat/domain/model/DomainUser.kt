package dev.nin0.chat.domain.model

import dev.nin0.chat.api.model.Roles
import dev.nin0.chat.api.model.User

/**
 * Represents a user that can send messages
 *
 * @param username The username of this user
 * @param roles The roles for the user
 * @param id The id for the user
 * @param isOnline Whether or not this user is currently online
 */
data class DomainUser(
    val username: String,
    val roles: Roles,
    val id: String,
    var isOnline: Boolean = false
) {

    /**
     * Whether or not this user is a bot
     */
    val isBot = Roles.Bot in roles

    /**
     * Whether or not this user can perform moderation actions
     */
    val isMod = Roles.Mod in roles

    /**
     * Whether or not this user is an admin
     */
    val isAdmin = Roles.Admin in roles

    override fun toString(): String {
        return buildString {
            append("DomainUser(")
            append("username=$username, ")
            append("roles=$roles, ")
            append("id=$id, ")
            append("isBot=$isBot, ")
            append("isMod=$isMod, ")
            append("isAdmin=$isAdmin, ")
            append("isOnline=$isOnline")
            append(")")
        }
    }

    companion object {

        /**
         * Converts an [api user][User] to a [DomainUser]
         *
         * @param user The api user to convert
         */
        fun fromApi(user: User) = with(user) {
            DomainUser(
                username = username,
                roles = roles,
                id = id
            )
        }

    }

}