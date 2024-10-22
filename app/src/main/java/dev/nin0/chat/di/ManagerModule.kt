package dev.nin0.chat.di

import dev.nin0.chat.domain.manager.AuthManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ManagerModule = module {

    singleOf(::AuthManager)

}