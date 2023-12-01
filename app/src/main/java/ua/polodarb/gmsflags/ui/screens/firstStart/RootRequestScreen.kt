package ua.polodarb.gmsflags.ui.screens.firstStart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ua.polodarb.gmsflags.R

@Composable
fun RootRequestScreen(
    onExit: () -> Unit,
    onRootRequest: () -> Unit,
    isButtonLoading: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.verticalScroll(state = rememberScrollState())
        ) {
            AsyncImage(
                model = R.drawable.root_image,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 44.dp)
            )
            Text(
                text = stringResource(id = R.string.root_title),
                fontSize = 46.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 44.sp,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
            )
            Text(
                text = stringResource(id = R.string.root_msg),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Text(
                text = stringResource(id = R.string.root_advice),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 36.dp)
            ) {
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.root_exit),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                    if (isButtonLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.root_request),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
