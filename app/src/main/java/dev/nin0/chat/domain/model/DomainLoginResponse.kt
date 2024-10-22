package dev.nin0.chat.domain.model

import dev.nin0.chat.api.model.response.LoginResponse

data class DomainLoginResponse(
    val id: String,
    val token: String
) {

    companion object {

        fun fromApi(api: LoginResponse) = with(api) {
            DomainLoginResponse(
                id = id,
                token = token
            )
        }

    }

}