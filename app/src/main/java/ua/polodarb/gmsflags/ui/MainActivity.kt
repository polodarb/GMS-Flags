package ua.polodarb.gmsflags.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.topjohnwu.superuser.Shell
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme


class MainActivity : ComponentActivity() {

    private var shellInitialized: Boolean = false

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("db", "1 - ${(applicationContext as GMSApplication).isRootDatabaseInitialized}")

        if (!shellInitialized) Shell.getShell { shellInitialized = true }

        installSplashScreen().apply {
            setKeepOnScreenCondition { !shellInitialized }
        }

        if (!Shell.getShell().isRoot) {
            Toast.makeText(this, "Root is denied", Toast.LENGTH_SHORT).show()
        }

        Log.e("db", "2 - ${(applicationContext as GMSApplication).isRootDatabaseInitialized}")

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            GMSFlagsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootAppNavigation(
                        navController = rememberNavController(),
                        modifier = Modifier.fillMaxSize()
                    )
                    Log.e("db", "3 - ${(applicationContext as GMSApplication).isRootDatabaseInitialized}")
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(::shellInitialized.name, shellInitialized)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        shellInitialized = savedInstanceState.getBoolean(::shellInitialized.name)
        super.onRestoreInstanceState(savedInstanceState)
    }
}