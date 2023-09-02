package ua.polodarb.gmsflags.ui.screens.welcomeScreens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.ui.images.welcomeimage.getGMSImagesWelcome

@Preview
@Composable
fun WelcomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Image(
                imageVector = getGMSImagesWelcome(colorScheme = MaterialTheme.colorScheme),
                contentDescription = "",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            )
            Text(
                text = "Welcome!",
                fontSize = 46.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)
            )
            Text(
                text = """
            Explore Google Apps for hidden 
            features, redesigns, abilities to turn 
            off regional restrictions, and more... 
        """.trimIndent(),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 32.dp),
                lineHeight = 28.sp
            )
            Text(
                text = "Become part of the community!",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Start",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp)) {
                        append("By continuing, you agree to our ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Terms of Service")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp)) {
                        append(" and ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Privacy Policy")
                    }
                },
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            )
        }
    }
}