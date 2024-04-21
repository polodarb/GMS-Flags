package ua.polodarb.flagsfile

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.repository.uiStates.UiStates
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadFileScreen(
    fileUri: Uri?
) {

    val viewModel = koinViewModel<LoadFileScreenViewModel>(parameters = { parametersOf(fileUri) })
    val flagsData = viewModel.flagsData.collectAsState()

    when (val result = flagsData.value) {
        is UiStates.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
        }

        is UiStates.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${result.throwable}")
            }
        }

        is UiStates.Success -> {
            Scaffold(
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Text(
                                text = result.data.packageName,
                            )
                        }
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding()),
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = result.data.packageName)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = result.data.flags.toString())
                }
            }
        }
    }
}