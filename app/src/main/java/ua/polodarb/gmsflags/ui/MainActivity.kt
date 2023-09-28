package ua.polodarb.gmsflags.ui

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.get
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import java.io.File

class MainActivity : ComponentActivity() {
    
    private val appContext = get<Context>() as GMSApplication

    private val configuredFilePath =
        "${appContext.filesDir.absolutePath}${File.separator}configured"
    private var isFirstStart = !File(configuredFilePath).exists()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isFirstStart) {
            appContext.initShell()
            appContext.initDB()
        }

        installSplashScreen().apply {
            // TODO: Navigation to ErrorRootPermissionScreen
            if (!isFirstStart) setKeepOnScreenCondition { !appContext.isRootDatabaseInitialized }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        Toast.makeText(this, "$isFirstStart", Toast.LENGTH_SHORT).show()

        setContent {
            GMSFlagsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootAppNavigation(
                        navController = rememberNavController(),
                        activity = this@MainActivity,
                        isFirstStart = isFirstStart,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    fun setFirstLaunch() = File(configuredFilePath).createNewFile()
}