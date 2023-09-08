package ua.polodarb.gmsflags.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NoFlagsOrPackages(
    type: NoFlagsOrPackages = NoFlagsOrPackages.FLAGS
) {

    val text = when (type) {
        NoFlagsOrPackages.FLAGS -> "¯\\_(ツ)_/¯\n\nFlags not found"
        NoFlagsOrPackages.APPS -> "¯\\_(ツ)_/¯\n\nApps not found"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class NoFlagsOrPackages {
    APPS, FLAGS
}