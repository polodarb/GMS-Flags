package ua.polodarb.gmsflags.ui.screens.firstStartScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.ui.images.getGMSImagesRoot

@Composable
fun RootRequestScreen(
    onExit: () -> Unit,
    onRootRequest: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Image(
                imageVector = getGMSImagesRoot(colorScheme = MaterialTheme.colorScheme),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 44.dp)
            )
            Text(
                text = "Root required",
                fontSize = 46.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
            )
            Text(
                text = """
            Unfortunately, the application 
            requires superuser rights to function. 
        """.trimIndent(),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp),
                lineHeight = 26.sp
            )
            Text(
                text = "To request click the \"Request root\"",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
            ) {
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Exit",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Button(
                    onClick = onRootRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Request root",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}