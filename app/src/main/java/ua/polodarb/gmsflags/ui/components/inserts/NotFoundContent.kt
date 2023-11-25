package ua.polodarb.gmsflags.ui.components.inserts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ua.polodarb.gmsflags.R

@Composable
fun NotFoundContent(
    type: NoFlagsOrPackages = NoFlagsOrPackages.FLAGS,
    customText: String? = null
) {
    val text = "¯\\_(ツ)_/¯\n\n" + (customText ?: when (type) {
            NoFlagsOrPackages.FLAGS -> stringResource(id = R.string.component_no_flags)
            NoFlagsOrPackages.APPS -> stringResource(id = R.string.component_no_apps)
            NoFlagsOrPackages.PACKAGES -> stringResource(R.string.component_no_packages)
        })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.surface),
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
    APPS, PACKAGES, FLAGS
}
