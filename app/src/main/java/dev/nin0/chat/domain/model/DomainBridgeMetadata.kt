package dev.nin0.chat.domain.model

import androidx.compose.ui.graphics.Color
import dev.nin0.chat.api.model.BridgeMetadata
import dev.nin0.chat.ui.util.ColorUtil

data class DomainBridgeMetadata(
    val source: String,
    val color: Color
) {

    companion object {

        fun fromApi(apiMetadata: BridgeMetadata, fallbackSource: String = "") = with(apiMetadata) {
            if (source == null && color == null) return@with null

            DomainBridgeMetadata(
                source = source ?: fallbackSource,
                color = if (color != null) ColorUtil.parseColorHex(color) else Color.Unspecified
            )
        }

    }

}