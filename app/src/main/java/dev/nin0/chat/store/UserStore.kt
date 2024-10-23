package dev.nin0.chat.store

import dev.nin0.chat.api.gateway.Gateway
import dev.nin0.chat.api.gateway.event.MessageEvent
import dev.nin0.chat.api.gateway.event.MessageHistoryEvent
import dev.nin0.chat.api.gateway.event.UserListSyncEvent
import dev.nin0.chat.api.gateway.ext.on
import dev.nin0.chat.api.model.MessageType
import dev.nin0.chat.api.model.User
import dev.nin0.chat.domain.model.DomainUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * In-memory cache for [users][DomainUser]
 *
 * @param gateway Used to listen for relevant updates
 */
class UserStore(
    gateway: Gateway
): CoroutineScope {

    private val _users = MutableStateFlow<Map<String, DomainUser>>(emptyMap())
    private val usersSnapshot: Map<String, DomainUser>
        get() = _users.value

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    /**
     * Retrieves a user from the cache if possible
     *
     * @param id The id of the user to retrieve
     */
    operator fun get(id: String): DomainUser? {
        return usersSnapshot.getOrDefault(id, null)
    }

    /**
     * Observe changes to the list of online users
     *
     * @param onChange Function called when the user list changes
     */
    fun observeUserList(
        onChange: (List<DomainUser>) -> Unit
    ) {
        _users
            .buffer(Channel.UNLIMITED)
            .onEach {
                launch {
                    onChange(it.values.toList().filter { u -> u.isOnline })
                }
            }.launchIn(this)
    }

    /**
     * Adds or modifies a list of users to the cache all at once
     *
     * @param apiUsers The users to cache
     */
    private suspend fun upsertUsersBulk(apiUsers: List<User>) {
        val mutableSnapshot = usersSnapshot.toMutableMap()
        val domainUsers = apiUsers.map {
            DomainUser.fromApi(it).apply { isOnline = true }
        }

        domainUsers.forEach { user ->
            mutableSnapshot[user.id] = user
        }

        _users.emit(mutableSnapshot)
    }

    /**
     * Adds or modifies the specified [user][apiUser] to the cache
     *
     * @param apiUser The user to cache
     * @param isOnline Whether nor not the user is online
     */
    private suspend fun upsertUser(apiUser: User, isOnline: Boolean = false) {
        if (apiUser.id == "1") return
        val mutableSnapshot = usersSnapshot.toMutableMap()
        mutableSnapshot[apiUser.id] = DomainUser.fromApi(apiUser).apply {
            this.isOnline = isOnline
        }

        _users.emit(mutableSnapshot)
    }

    /**
     * Marks all known users as offline
     */
    private suspend fun markAllOffline() {
        val mutableSnapshot = usersSnapshot.toMutableMap()
        mutableSnapshot.forEach { (id, user) ->
            mutableSnapshot[id] = user.apply { isOnline = false }
        }

        _users.emit(mutableSnapshot)
    }

    init {
        gateway.on<UserListSyncEvent> { (data) ->
            markAllOffline()
            upsertUsersBulk(data.users)
        }

        gateway.on<MessageEvent> { (message) ->
            if (message.type == MessageType.Default) upsertUser(message.author, isOnline = true)
        }

        gateway.on<MessageHistoryEvent> { (data) ->
            upsertUsersBulk(
                data.history.filter {
                    it.type == MessageType.Default
                }.map { it.author }
            )
        }
    }

}