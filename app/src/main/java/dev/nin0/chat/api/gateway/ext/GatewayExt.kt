package dev.nin0.chat.api.gateway.ext

import dev.nin0.chat.api.gateway.Gateway
import dev.nin0.chat.api.gateway.GatewayState
import dev.nin0.chat.api.gateway.event.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <reified E: Event> Gateway.on(
    noinline filter: suspend (E) -> Boolean = { true },
    crossinline block: suspend (E) -> Unit
) {
    events.buffer(Channel.UNLIMITED)
        .filterIsInstance<E>()
        .filter(filter)
        .onEach {
            launch {
                block(it)
            }
        }.launchIn(this)
}

inline fun Gateway.observeState(
    crossinline block: suspend (GatewayState) -> Unit
) {
    state.buffer(Channel.UNLIMITED)
        .onEach {
            launch {
                block(it)
            }
        }.launchIn(this)
}