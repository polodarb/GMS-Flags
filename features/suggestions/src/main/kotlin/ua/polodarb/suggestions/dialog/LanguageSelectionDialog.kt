package ua.polodarb.suggestions.dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LanguageSelectionDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onLocaleSelected: (String) -> Unit
) {
    var selectedLocale by remember { mutableStateOf<String?>(null) }
    var customLocale by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    val locales = mapOf(
        "en" to "English",
        "en-AU" to "English (Australia)",
        "en-GB" to "English (United Kingdom)",
        "en-HI" to "English (India)",
        "ja-JP" to "Japanese (Japan)",
        "fr-FR" to "French (France)",
        "hi-IN" to "Hindi (India)",
        "de-DE" to "German (Germany)",
        "it-IT" to "Italian (Italy)",
        "es-ES" to "Spanish (Spain)"
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Select Locale") },
            text = {
                Column(
                    modifier = Modifier
                        .imePadding()
                ) {
                    LazyColumn(
                        modifier = Modifier.height(286.dp)
                    ) {
                        items(locales.keys.toList()) { locale ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        selected = (locale == selectedLocale),
                                        onClick = { selectedLocale = locale }
                                    )
                                    .padding(vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (locale == selectedLocale),
                                    onClick = null
                                )
                                Text(
                                    text = locales[locale] ?: locale,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customLocale,
                        onValueChange = {
                            customLocale = it
                            selectedLocale = null
                        },
                        shape = MaterialTheme.shapes.medium,
                        label = { Text("Custom Locale") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = selectedLocale != null || customLocale.isNotEmpty(),
                    onClick = {
                        if (customLocale.isNotEmpty()) {
                            onLocaleSelected(customLocale)
                        } else if (selectedLocale != null) {
                            onLocaleSelected(selectedLocale!!)
                        } else {
                            onDismiss()
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}