package ua.polodarb.gmsflags.ui.screens.flagChange.extScreens

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.ui.components.inserts.NotFoundContent
import ua.polodarb.gmsflags.ui.screens.flagChange.SelectFlagsType

@Composable
fun AddFlagListContent(flagType: SelectFlagsType) {

    var flagList by remember { mutableStateOf(emptyList<String>()) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        AddFlagContentLabel(text = "Enter the flags")
        AddFlagContentTextField(flagList) { newList -> flagList = newList }
        if (flagType == SelectFlagsType.BOOLEAN) { AddFlagContentBooleanActivate() }
        HorizontalDivider(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp))
        AddFlagContentLabel(text = "Formed flags")
        AddFlagContentGeneratedChips(flagList)
        Box(modifier = Modifier.padding(48.dp)) {
            if (flagList.isEmpty() || flagList[0].isBlank()) NotFoundContent(customText = "No flags added")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AddFlagContentBooleanActivate() {
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(56.dp)
            .toggleable(
                value = checkedState,
                onValueChange = { onStateChange(!checkedState) },
                role = Role.Checkbox
            )
            .padding(start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null
        )
        Text(
            text = "Activate added flags",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun AddFlagContentLabel(
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp),
        )
    }
}

@Composable
fun AddFlagContentTextField(
    flagList: List<String>,
    onFlagListChange: (List<String>) -> Unit
) {
    OutlinedTextField(
        value = flagList.joinToString("\n"),
        onValueChange = {
            onFlagListChange(it.split(" ", "\n"))
        },
        placeholder = {
            Text(
                text = "Enter flags using a space",
            )
        },
        supportingText = {
            Text(
                text = "Example: 151 98352 2353"
            )
        },
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddFlagContentGeneratedChips(flagList: List<String>) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        flagList
            .filter { it.isNotBlank() }
            .forEach { flag ->
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(text = flag)
                    }
                )
            }
    }
}
