package ua.polodarb.gmsflags.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.core.platform.activity.BaseActivity
import ua.polodarb.gmsflags.data.remote.github.GithubApiServiceImpl
import ua.polodarb.gmsflags.data.workers.GOOGLE_UPDATES_WORKER_TAG
import ua.polodarb.gmsflags.data.workers.GoogleUpdatesCheckWorker
import ua.polodarb.gmsflags.ui.components.UpdateDialog
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private val appContext = get<Context>() as GMSApplication

    private val githubApiService by inject<GithubApiServiceImpl>()

    private val configuredFile = File(appContext.filesDir, "configured")

    private var isFirstStart = !configuredFile.exists()

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
        .build()

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

        if (intent != null && intent.action == Intent.ACTION_VIEW && intent.type == "text/plain") {
            Toast.makeText(this, "Load from file", Toast.LENGTH_SHORT).show()
            val uri = intent.data
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val workerRequester = PeriodicWorkRequestBuilder<GoogleUpdatesCheckWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(GOOGLE_UPDATES_WORKER_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            GOOGLE_UPDATES_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            workerRequester
        )

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

    fun setFirstLaunch() = configuredFile.createNewFile()
}
