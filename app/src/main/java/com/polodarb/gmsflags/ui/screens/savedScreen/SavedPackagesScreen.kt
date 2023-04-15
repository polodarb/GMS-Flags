package com.polodarb.gmsflags.ui.screens.savedScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.polodarb.gmsflags.ui.screens.packagesScreen.LazyItem

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
