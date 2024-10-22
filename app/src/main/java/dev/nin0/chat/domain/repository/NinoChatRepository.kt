package dev.nin0.chat.domain.repository

import dev.nin0.chat.api.rest.NinoChatService
import dev.nin0.chat.api.rest.response.transform
import dev.nin0.chat.domain.model.DomainLoginResponse

class NinoChatRepository(
    private val ninoChatService: NinoChatService
) {

    suspend fun login(
        email: String,
        password: String
    ) = ninoChatService.login(email, password).transform {
        DomainLoginResponse.fromApi(it)
    }

}