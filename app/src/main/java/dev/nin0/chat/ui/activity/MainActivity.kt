package dev.nin0.chat.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.nin0.chat.domain.manager.AuthManager
import dev.nin0.chat.ui.screen.auth.LoginScreen
import dev.nin0.chat.ui.screen.chat.ChatScreen
import dev.nin0.chat.ui.theme.NinoChatTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authManager: AuthManager = koinInject()
            val startingScreen = if (authManager.isLoggedIn) ChatScreen() else LoginScreen()

            NinoChatTheme {
                Navigator(startingScreen) {
                    SlideTransition(it)
                }
            }
        }
    }

}