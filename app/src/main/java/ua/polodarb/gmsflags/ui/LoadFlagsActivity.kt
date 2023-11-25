package ua.polodarb.gmsflags.ui

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme

class LoadFlagsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (intent != null) {
            val uri: Uri? = intent.data
            if (uri == null) {
                Toast.makeText(this, "Empty data", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Uri: ${uri}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Empty intent", Toast.LENGTH_LONG).show()
        }

        setContent {
            GMSFlagsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Not implemented")
                    }
                }
            }
        }
    }
}