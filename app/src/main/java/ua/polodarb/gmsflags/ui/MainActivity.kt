package ua.polodarb.gmsflags.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.core.platform.activity.BaseActivity
import ua.polodarb.network.impl.appUpdates.AppUpdatesApiServiceImpl
import ua.polodarb.updates.worker.GOOGLE_UPDATES_WORKER_TAG
import ua.polodarb.updates.worker.GoogleUpdatesCheckWorker
import ua.polodarb.gmsflags.core.updates.UpdateDialog
import ua.polodarb.gmsflags.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import ua.polodarb.platform.init.InitShell
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private val appContext = get<Context>() as GMSApplication

    val rootDBInitializer: InitRootDB by inject()

    private val githubApiService by inject<AppUpdatesApiServiceImpl>()

    private val configuredFile = File(appContext.filesDir, "configured")

    private var isFirstStart = !configuredFile.exists()

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
        .build()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.DEBUG)
            ExceptionHandler.initialize(this, CrashActivity::class.java)

        analytics = Firebase.analytics

        if (!isFirstStart) {
            InitShell.initShell()
            rootDBInitializer.initDB()
        }

//        installSplashScreen().apply {
            // TODO: Navigation to ErrorRootPermissionScreen
//            if (!isFirstStart) setKeepOnScreenCondition { !rootDBInitializer.isRootDatabaseInitialized }
//        }

        val content: View = findViewById(android.R.id.content)
//        lifecycleScope.launch(Dispatchers.Main) {
//            rootDBInitializer.databaseInitializationStateFlow.collect { isInitialized ->
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    Log.e("InitRootDBImpl", "isInitialized: ${rootDBInitializer.isRootDatabaseInitialized}")
                    return if (rootDBInitializer.isRootDatabaseInitialized) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            })
//            }
//        }

        if (intent != null && intent.action == Intent.ACTION_VIEW && intent.type == "text/plain") {
            Toast.makeText(this, "Load from file", Toast.LENGTH_SHORT).show()
            val uri = intent.data
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        val workerRequester = PeriodicWorkRequestBuilder<GoogleUpdatesCheckWorker>(15, TimeUnit.MINUTES)
//            .setConstraints(constraints)
//            .addTag(GOOGLE_UPDATES_WORKER_TAG)
//            .build()
//
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            GOOGLE_UPDATES_WORKER_TAG,
//            ExistingPeriodicWorkPolicy.KEEP,
//            workerRequester
//        )

        setContent {
            GMSFlagsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UpdateDialog(
                        appUpdatesApiService = githubApiService,
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
