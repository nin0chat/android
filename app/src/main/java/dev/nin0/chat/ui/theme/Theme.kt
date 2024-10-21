package dev.nin0.chat.ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
fun NinoChatTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current as ComponentActivity
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val darkTheme = isSystemInDarkTheme()

    val (colorScheme, appColors) = when {
        dynamicColor -> {
            if (darkTheme)
                dynamicDarkColorScheme(context) to DarkAppColors
            else
                dynamicLightColorScheme(context) to LightAppColors
        }

        darkTheme -> darkColorScheme() to DarkAppColors
        else -> lightColorScheme() to LightAppColors
    }

    val systemBarStyle = remember(darkTheme, dynamicColor) {
        SystemBarStyle.auto(
            lightScrim = colorScheme.scrim.toArgb(),
            darkScrim = colorScheme.scrim.toArgb(),
            detectDarkMode = { _ -> darkTheme }
        )
    }

    context.enableEdgeToEdge(systemBarStyle)

    CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}