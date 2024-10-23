package dev.nin0.chat.api.gateway

sealed interface GatewayState {

    val isActive: Boolean
        get() = this is Started || this is Connected || this is Authenticated

    data object Uninitialized: GatewayState
    data object Started: GatewayState
    data object Connected: GatewayState
    data object Authenticated: GatewayState
    data object Disconnected: GatewayState
    data object Reconnecting: GatewayState
    data object Stopped: GatewayState

}