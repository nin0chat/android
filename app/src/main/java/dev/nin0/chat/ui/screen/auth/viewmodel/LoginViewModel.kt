package dev.nin0.chat.ui.screen.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.nin0.chat.api.rest.response.ifSuccessful
import dev.nin0.chat.domain.manager.AuthManager
import dev.nin0.chat.domain.repository.NinoChatRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val ninoChatRepository: NinoChatRepository,
    private val authManager: AuthManager
): ScreenModel {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var loading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    fun login() {
        screenModelScope.launch {
            loading = true
            ninoChatRepository.login(
                email = email,
                password = password
            ).ifSuccessful { res ->
                authManager.token = res.token
                authManager.authedUserId = res.id
                loginSuccess = true
            }
            loading = false
        }
    }

}