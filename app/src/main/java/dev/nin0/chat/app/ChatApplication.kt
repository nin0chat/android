package dev.nin0.chat.app

import android.app.Application
import dev.nin0.chat.di.ApiModule
import dev.nin0.chat.di.KtorModule
import dev.nin0.chat.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ChatApplication)

            modules(
                KtorModule,
                ApiModule,
                ViewModelModule
            )
        }
    }

}