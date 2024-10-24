package dev.nin0.chat.di

import dev.nin0.chat.store.MessageStore
import dev.nin0.chat.store.UserStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val StoreModule = module {

    singleOf(::UserStore)
    singleOf(::MessageStore)

}