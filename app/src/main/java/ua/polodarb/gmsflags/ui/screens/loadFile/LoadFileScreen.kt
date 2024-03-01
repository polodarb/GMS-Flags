package ua.polodarb.gmsflags.ui.screens.loadFile

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadFileScreen(
    fileUri: Uri?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Load flags from file", // TODO: Move to res
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            Text(text = Uri.parse(fileUri.toString()).toString())
        }
    }
}