package ua.polodarb.gmsflags.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.remote.github.GithubApiServiceImpl
import ua.polodarb.gmsflags.ui.components.UpdateDialog
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    private val appContext = get<Context>() as GMSApplication

    private val githubApiService by inject<GithubApiServiceImpl>()

    private val configuredFilePath =
        "${appContext.filesDir.absolutePath}${File.separator}configured"

    private var isFirstStart = !File(configuredFilePath).exists()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.DEBUG)
            ExceptionHandler.initialize(this, CrashActivity::class.java)

        analytics = Firebase.analytics

        if (!isFirstStart) {
            appContext.initShell()
            appContext.initDB()
        }

        installSplashScreen().apply {
            // TODO: Navigation to ErrorRootPermissionScreen
            if (!isFirstStart) setKeepOnScreenCondition { !appContext.isRootDatabaseInitialized }
        }

        Log.d("intent", intent.toString())

        if (intent != null && intent.action == Intent.ACTION_VIEW && intent.type == "text/plain") {
            Toast.makeText(this, "Load from file", Toast.LENGTH_SHORT).show()
            val uri = intent.data
            Log.d("intent", "data - ${intent.data}")
            Log.d("intent", "data-encodedQuery - ${intent.data?.encodedQuery}")
            Log.d("intent", "extras - ${intent.extras}")
            Log.d("intent", "action - ${intent.action}")
            Log.d("intent", "categories - ${intent.categories}")
            Log.d("intent", "flags - ${intent.flags}")
            Log.d("intent", "identifier - ${intent.identifier}")
            Log.d("intent", "identifier - ${intent.`package`}")
            Log.d("intent", "type - ${intent.type}")
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            GMSFlagsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UpdateDialog(
                        githubApiService = githubApiService,
                        isFirstStart = isFirstStart
                    )
                    RootAppNavigation(
                        navController = rememberNavController(),
                        isFirstStart = isFirstStart,
                        loadFlagIntent = intent,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

    }

    fun setFirstLaunch() = File(configuredFilePath).createNewFile()
}
