package ua.polodarb.gmsflags.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import org.koin.android.ext.android.get
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import java.io.File
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.R

class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private val appContext = get<Context>() as GMSApplication

    private val configuredFilePath =
        "${appContext.filesDir.absolutePath}${File.separator}configured"
    private var isFirstStart = !File(configuredFilePath).exists()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "By turning off notifications, you will not receive new information or updates.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

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

        if (!isFirstStart) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                askNotificationPermission()
            }
        }

    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun setFirstLaunch() = File(configuredFilePath).createNewFile()
}