package ua.polodarb.gmsflags.errors.gms.sharedUI

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HeaderIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            null,
            modifier = Modifier
                .size(172.dp),
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
internal fun HeaderDescription(
    modifier: Modifier = Modifier,
    text: String,
    vararg descriptions: String
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium
        )
        descriptions.forEach {
            Text(
                text = it,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}