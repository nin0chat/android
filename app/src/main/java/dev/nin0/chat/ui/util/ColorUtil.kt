package dev.nin0.chat.ui.util

import androidx.compose.ui.graphics.Color

object ColorUtil {

    private val hexCodeRegex = "^#?(([0-9a-fA-F]{1,2})([0-9a-fA-F]{1,2})([0-9a-fA-F]{1,2}))$".toRegex()
    private val parsedColors = mutableMapOf<String, Color>()

    fun parseColorHex(hex: String): Color {
        if (parsedColors.containsKey(hex)) return parsedColors[hex]!!
        val match = hexCodeRegex.matchEntire(hex) ?: return Color.Unspecified

        val (_, r, g, b) = match.groups.toList()
            .mapNotNull { runCatching { it?.value?.toInt(16) }.getOrNull() }

        return try {
            parsedColors[hex] = Color(r, g, b)
            Color(r, g, b)
        } catch (e: Throwable) {
            Color.Unspecified
        }
    }

}