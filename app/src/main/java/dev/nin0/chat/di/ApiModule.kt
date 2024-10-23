package dev.nin0.chat.di

import dev.nin0.chat.api.gateway.Gateway
import dev.nin0.chat.api.rest.NinoChatService
import dev.nin0.chat.api.rest.request.RequestHandler
import dev.nin0.chat.domain.repository.NinoChatRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ApiModule = module {

    singleOf(::RequestHandler)
    singleOf(::NinoChatService)
    singleOf(::NinoChatRepository)
    singleOf(::Gateway)

}