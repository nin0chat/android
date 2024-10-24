package dev.nin0.chat.ui.screen.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.nin0.chat.api.gateway.Gateway
import dev.nin0.chat.api.gateway.GatewayState
import dev.nin0.chat.api.gateway.ext.on
import dev.nin0.chat.api.gateway.event.MessageEvent
import dev.nin0.chat.api.gateway.ext.observeState
import dev.nin0.chat.domain.model.DomainMessage
import dev.nin0.chat.domain.model.DomainUser
import dev.nin0.chat.store.MessageStore
import dev.nin0.chat.store.UserStore
import kotlinx.coroutines.launch

class ChatViewModel(
    private val gateway: Gateway,
    userStore: UserStore,
    messageStore: MessageStore
): ScreenModel {

    var onlineUsers by mutableStateOf(emptyList<DomainUser>())
        private set

    var messages by mutableStateOf(emptyList<DomainMessage>())
        private set

    var gatewayState by mutableStateOf<GatewayState>(GatewayState.Uninitialized)
        private set

    init {
        connect() // TODO: Move to more sane place

        gateway.observeState {
            gatewayState = it
        }

        userStore.observeUserList {
            onlineUsers = it
        }

        messageStore.observeMessages {
            messages = it
        }
    }

    private fun connect() {
        screenModelScope.launch {
            gateway.connect()
        }
    }

}