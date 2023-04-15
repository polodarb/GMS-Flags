package com.polodarb.gmsflags.ui.screens.savedScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.polodarb.gmsflags.R
import com.polodarb.gmsflags.ui.screens.packagesScreen.LazyItem
import com.polodarb.gmsflags.ui.screens.packagesScreen.result
import com.polodarb.gmsflags.ui.theme.Typography

@Composable
fun SavedPackagesScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(20) {
                LazyItem(packageName = "com.google.android.gms.cast", packagesCount = 0)
            }
        }
    }
}
