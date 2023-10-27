package ua.polodarb.gmsflags.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ipc.RootService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.IGmsFlagsRootService
import ua.polodarb.gmsflags.data.remote.github.GithubApiServiceImpl
import ua.polodarb.gmsflags.ui.components.UpdateDialog
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    private lateinit var rootDatabase: IGmsFlagsRootService
    var isRootDatabaseInitialized = false

    private companion object {
        const val SHELL_TIMEOUT = 10L
    }

    private val shellConfig = Shell.Builder.create()
        .setFlags(Shell.FLAG_REDIRECT_STDERR or Shell.FLAG_MOUNT_MASTER)
        .setTimeout(SHELL_TIMEOUT)

    private val githubApiService by inject<GithubApiServiceImpl>()

    private val configuredFilePath =
        "${this.filesDir.absolutePath}${File.separator}configured"
    private var isFirstStart = !File(configuredFilePath).exists()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        if (!isFirstStart) {
            initShell()
            initDB()
        }

        installSplashScreen().apply {
            if (!isFirstStart) setKeepOnScreenCondition { !isRootDatabaseInitialized }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (!isFirstStart) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                askNotificationPermission()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        requestPermissionLauncher.unregister()
    }

    fun initShell() {
        try {
            Shell.setDefaultBuilder(shellConfig)
        } catch (_: IllegalStateException) {
        }
    }

    fun initDB() {
        val intent = Intent(this, RootService::class.java)
        val service = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                rootDatabase = IGmsFlagsRootService.Stub.asInterface(service)
                isRootDatabaseInitialized = true
                setContent {
                    GMSFlagsTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            UpdateDialog(
                                githubApiService = githubApiService,
                                isFirstStart = isFirstStart
                            )
                            RootAppNavigation(
                                rootDatabase = rootDatabase,
                                navController = rememberNavController(),
                                activity = this@MainActivity,
                                isFirstStart = isFirstStart,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                setContent {
                    GMSFlagsTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Service disconnected")
                            }
                        }
                    }
                }
            }
        }

        RootService.bind(intent, service)
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
