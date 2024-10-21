package dev.nin0.chat.di

import dev.nin0.chat.ui.screen.auth.viewmodel.LoginViewModel
import dev.nin0.chat.ui.screen.chat.viewmodel.ChatViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val ViewModelModule = module {

    factoryOf(::LoginViewModel)
    factoryOf(::ChatViewModel)

}