package dev.nin0.chat.ui.screen.chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import dev.nin0.chat.ui.screen.chat.component.ChatList
import dev.nin0.chat.ui.screen.chat.viewmodel.ChatViewModel
import io.github.materiiapps.panels.SwipePanels
import io.github.materiiapps.panels.SwipePanelsValue
import io.github.materiiapps.panels.rememberSwipePanelsState

class ChatScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: ChatViewModel = koinScreenModel()
        val swipePanelsState = rememberSwipePanelsState()

        // === Center Panel (Chat List) State ===
        val shouldClipCenter = remember(swipePanelsState.targetValue, swipePanelsState.isDragging) {
            swipePanelsState.isDragging || (swipePanelsState.targetValue != SwipePanelsValue.Center)
        }

        val shouldDarkenCenter = remember(swipePanelsState.targetValue) {
            swipePanelsState.targetValue != SwipePanelsValue.Center
        }

        // === Center Panel Properties ===
        val centerTint by animateColorAsState(
            if (shouldDarkenCenter) Color.Black.copy(alpha = 0.6f) else Color.Transparent,
            label = "Center Panel Darkening"
        )

        val centerRadius by animateIntAsState(
            if (shouldClipCenter) 5 else 0,
            label = "Center Panel Border Radius"
        )


        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            SwipePanels(
                state = swipePanelsState,
                inBetweenPadding = 8.dp,
                start = {},
                end = {},
                center = {
                    ChatList(
                        messages = viewModel.messages,
                        modifier = Modifier
                            .drawWithContent {
                                drawContent()
                                drawRoundRect(
                                    color = centerTint,
                                    cornerRadius = CornerRadius(x = 0.05f * size.minDimension)
                                )
                            }
                            .clip(RoundedCornerShape(centerRadius))
                    )
                }
            )
        }
    }

}