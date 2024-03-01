package ua.polodarb.gmsflags.ui.screens.firstStart

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ua.polodarb.gmsflags.R

@Composable
fun WelcomeScreen(
    onStart: () -> Unit,
    openLink: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState())
    ) {
        AsyncImage(
            model = R.drawable.welcome_image,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 64.dp, bottom = 32.dp, start = 32.dp, end = 32.dp)
        )
        Text(
            text = stringResource(id = R.string.setup_welcome_title),
            fontSize = 46.sp,
            fontWeight = FontWeight.W600,
            lineHeight = 44.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
        )
        Text(
            text = stringResource(id = R.string.setup_welcome_msg),
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            text = stringResource(id = R.string.setup_welcome_advice),
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
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
                text = stringResource(id = R.string.setup_welcome_start),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp
            )
        }
        Disclaimer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 36.dp, top = 24.dp),
            disclaimer = context.getString(R.string.setup_welcome_disclaimer),
            links = persistentListOf(
                Pair(
                    context.getString(R.string.setup_welcome_terms_chunk),
                    context.getString(R.string.setup_welcome_terms_url)
                ),
                Pair(
                    context.getString(R.string.setup_welcome_policy_chunk),
                    context.getString(R.string.setup_welcome_policy_url)
                )
            ),
            openLink = openLink
        )
    }
}

@Composable
private fun Disclaimer(
    modifier: Modifier = Modifier,
    disclaimer: String,
    links: PersistentList<Pair<String, String>>,
    openLink: (String) -> Unit
) {
    val chunks = mutableListOf<Pair<String, String?>>()
    var start = 0

    links.forEach { link ->
        val end = disclaimer.indexOf(link.first)
        require (end != -1) { "Links mismatch!" }
        chunks.add(Pair(disclaimer.substring(start, end), null))
        chunks.add(link)
        start = end + link.first.length
    }
    if (start < disclaimer.length) {
        chunks.add(Pair(disclaimer.substring(start, disclaimer.length), null))
    }

    val annotatedString = buildAnnotatedString {
        chunks.forEach { chunk ->
            if (chunk.second != null)
                pushStringAnnotation(tag = chunk.first, annotation = chunk.second!!)
            withStyle(
                style = if (chunk.second != null) {
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    SpanStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            ) {
                append(chunk.first)
            }
            if (chunk.second != null) pop()
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(textAlign = TextAlign.Center),
        onClick = { offset ->
            chunks.forEach { chunk ->
                if (chunk.second != null) {
                    annotatedString.getStringAnnotations(
                        tag = chunk.first,
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        openLink(chunk.second!!)
                    }
                }
            }
        },
        modifier = modifier
    )
}
