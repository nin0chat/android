package dev.nin0.chat.store

import dev.nin0.chat.api.gateway.Gateway
import dev.nin0.chat.api.gateway.event.MessageEvent
import dev.nin0.chat.api.gateway.event.MessageHistoryEvent
import dev.nin0.chat.api.gateway.ext.on
import dev.nin0.chat.api.model.Message
import dev.nin0.chat.domain.model.DomainMessage
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
 * In-memory cache for [messages][DomainMessage]
 *
 * @param gateway Used to listen for relevant updates
 * @param userStore Used to keep authors up to date
 */
class MessageStore(
    gateway: Gateway,
    private val userStore: UserStore
): CoroutineScope {

    private val _messages = MutableStateFlow<Map<String, DomainMessage>>(emptyMap())
    private val messagesSnapshot: Map<String, DomainMessage>
        get() = _messages.value

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    /**
     * Retrieves a user from the cache if possible
     *
     * @param id The id of the user to retrieve
     */
    operator fun get(id: String): DomainMessage? {
        return messagesSnapshot.getOrDefault(id, null)
    }

    /**
     * Observe changes to the list of messages
     *
     * @param onChange Function called when new messages are received or old ones are updated
     */
    fun observeMessages(
        onChange: (List<DomainMessage>) -> Unit
    ) {
        _messages
            .buffer(Channel.UNLIMITED)
            .onEach {
                launch {
                    onChange(
                        it.values.sortedByDescending { msg -> msg.timestamp }
                    )
                }
            }.launchIn(this)
    }

    /**
     * Adds or modifies a list of messages to the cache all at once
     *
     * @param apiMessages The messages to cache
     */
    private suspend fun upsertMessagesBulk(apiMessages: List<Message>) {
        val mutableSnapshot = messagesSnapshot.toMutableMap()
        val domainMessages = apiMessages.map {
            DomainMessage.fromApi(it, bridgeClientName = userStore[it.author.id]?.username ?: "")
        }

        domainMessages.forEach { message ->
            mutableSnapshot[message.id] = message
        }

        _messages.emit(mutableSnapshot)
    }

    /**
     * Adds or modifies the specified [message][apiMessage] to the cache
     *
     * @param apiMessage The message to cache
     */
    private suspend fun upsertMessage(apiMessage: Message) {
        val mutableSnapshot = messagesSnapshot.toMutableMap()
        mutableSnapshot[apiMessage.id] = DomainMessage.fromApi(apiMessage, bridgeClientName = userStore[apiMessage.author.id]?.username ?: "")

        _messages.emit(mutableSnapshot)
    }

    init {
        gateway.on<MessageEvent> { (message) ->
            upsertMessage(message)
        }

        gateway.on<MessageHistoryEvent> { (data) ->
            upsertMessagesBulk(data.history)
        }
    }

}