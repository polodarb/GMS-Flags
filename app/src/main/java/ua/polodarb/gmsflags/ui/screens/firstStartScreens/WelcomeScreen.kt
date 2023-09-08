package ua.polodarb.gmsflags.ui.screens.firstStartScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.ui.images.getGMSImagesWelcome

@Composable
fun WelcomeScreen(
    onStart: () -> Unit,
    onPolicyClick: (String) -> Unit,
    onTermsClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Image(
                imageVector = getGMSImagesWelcome(colorScheme = MaterialTheme.colorScheme),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
            )
            Text(
                text = "Welcome!",
                fontSize = 46.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
            )
            Text(
                text = """
            Explore Google Apps for hidden 
            features, redesigns, abilities to turn 
            off regional restrictions, and more... 
        """.trimIndent(),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp),
                lineHeight = 26.sp
            )
            Text(
                text = "Become part of the community!",
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Start",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append("By continuing, you agree to our ")
                }

                pushStringAnnotation(tag = "policy", annotation = "https://google.com/policy")
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
                pop()

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append(" and ")
                }

                pushStringAnnotation(tag = "terms", annotation = "https://google.com/terms")
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
                pop()
            }

            ClickableText(
                text = annotatedString, style = TextStyle(
                    textAlign = TextAlign.Center
                ), onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "policy",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onPolicyClick(it.item)
                    }

                    annotatedString.getStringAnnotations(
                        tag = "terms",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onTermsClick(it.item)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 36.dp, top = 24.dp)
            )
        }
    }
}