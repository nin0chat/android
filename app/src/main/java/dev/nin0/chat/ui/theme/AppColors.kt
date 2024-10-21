package dev.nin0.chat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class AppColors(
    val guestTag: Color,
    val botTag: Color,
    val modTag: Color,
    val adminTag: Color
)

val LocalAppColors = compositionLocalOf<AppColors> { LightAppColors }

val MaterialTheme.appColors
    @Composable @ReadOnlyComposable get() = LocalAppColors.current

val DarkAppColors = AppColors(
    guestTag = Color(0xffc4c4c4),
    botTag = Color(0xff8181ff),
    modTag = Color(0xff0cc487),
    adminTag = Color(0xffff5f53)
)

val LightAppColors = AppColors(
    guestTag = Color(0xff949494),
    botTag = Color(0xff3e3eff),
    modTag = Color(0xff059364),
    adminTag = Color(0xfffc2510)
)