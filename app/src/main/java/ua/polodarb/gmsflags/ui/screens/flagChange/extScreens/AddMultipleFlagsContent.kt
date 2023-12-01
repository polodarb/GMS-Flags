package ua.polodarb.gmsflags.ui.screens.flagChange.extScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.components.inserts.NotFoundContent
import ua.polodarb.gmsflags.ui.screens.flagChange.SelectFlagsType

@Composable
fun AddFlagListContent(
    flagList: List<String>,
    flagType: SelectFlagsType,
    boolCheckedState: Boolean = true,
    boolOnStateChange: (Boolean) -> Unit = {},
    onFlagListChange: (List<String>) -> Unit
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        EnteredFlags(
            flagList = flagList,
            flagType = flagType,
            checkedState = boolCheckedState,
            onStateChange = {
                boolOnStateChange(it)
            },
            onFlagListChange = { newFlags ->
                onFlagListChange(newFlags)
            }
        )
        HorizontalDivider(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp))
        FormedFlags(flagList = flagList, flagType = flagType)
    }
}

@Composable
fun EnteredFlags(
    flagList: List<String>,
    flagType: SelectFlagsType,
    checkedState: Boolean,
    onStateChange: (Boolean) -> Unit,
    onFlagListChange: (List<String>) -> Unit
) {
    AddFlagContentLabel(text = stringResource(R.string.enter_the_flags))
    AddFlagContentTextField(flagList, flagType, onFlagListChange)
    if (flagType == SelectFlagsType.BOOLEAN) {
        AddFlagContentBooleanActivate(
            checkedState = checkedState,
            onStateChange = onStateChange
        )
    }
}

@Composable
fun FormedFlags(
    flagList: List<String>,
    flagType: SelectFlagsType
) {
    AddFlagContentLabel(text = stringResource(R.string.formed_flags))
    AddFlagContentGeneratedChips(flagList, flagType)
    Box(modifier = Modifier.padding(48.dp)) {
        if (flagType == SelectFlagsType.BOOLEAN) {
            if (flagList.isEmpty() || flagList.first()
                    .isEmpty()
            ) NotFoundContent(customText = stringResource(R.string.no_flags_added))
        } else {
            if (
                flagList.isEmpty() ||
                flagList.first().isEmpty() ||
                flagList.first().split("=").size < 2 ||
                flagList.first().split("=")[0].isEmpty() ||
                flagList.first().split("=")[1].isEmpty()
            ) NotFoundContent(customText = stringResource(R.string.no_flags_added))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun AddFlagContentBooleanActivate(
    checkedState: Boolean = true,
    onStateChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(56.dp)
            .toggleable(
                value = checkedState,
                onValueChange = onStateChange,
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
            text = stringResource(R.string.activate_added_flags),
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
    flagType: SelectFlagsType,
    onFlagListChange: (List<String>) -> Unit
) {

    val supportingText = remember {
        mutableStateOf(
            when (flagType) {
                SelectFlagsType.BOOLEAN -> "151=1 98352"
                SelectFlagsType.INTEGER -> "151=1 98352=12"
                SelectFlagsType.FLOAT -> "151=1.5 98352=0.8"
                SelectFlagsType.STRING -> "151=abc 98352=x0x"
            }
        )
    }

    OutlinedTextField(
        value = flagList.joinToString("\n"),
        onValueChange = {
            val values = it.split("\n", " ")
            onFlagListChange(values)
        },
        placeholder = {
            Text(
                text = when (flagType) {
                    SelectFlagsType.BOOLEAN -> stringResource(R.string.enter_flags_using_a_space)
                    else -> stringResource(R.string.enter_flags_using_the_equal_symbol)
                }
            )
        },
        supportingText = {
            Text(text = stringResource(R.string.example) + supportingText.value)
        },
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddFlagContentGeneratedChips(
    flagList: List<String>,
    flagType: SelectFlagsType
) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        flagList
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .forEach { flag ->
                if (flagType != SelectFlagsType.BOOLEAN) {
                    val parts = flag.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(text = flag)
                            }
                        )
                    }
                } else {
                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(text = flag)
                        }
                    )
                }
            }
    }
}