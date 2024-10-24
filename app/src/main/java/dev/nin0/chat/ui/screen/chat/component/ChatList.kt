package dev.nin0.chat.ui.screen.chat.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nin0.chat.domain.model.DomainMessage

@Composable
fun ChatList(
    messages: List<DomainMessage>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
        modifier = modifier
    ) { pv ->
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                Text(message.content)
            }
        }
    }
}