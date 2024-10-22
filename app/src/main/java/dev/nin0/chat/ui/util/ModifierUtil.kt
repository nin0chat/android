package dev.nin0.chat.ui.util

import androidx.compose.ui.Modifier

fun Modifier.thenIf(predicate: Boolean, block: Modifier.() -> Modifier) =
    if (predicate) then(Modifier.block()) else this