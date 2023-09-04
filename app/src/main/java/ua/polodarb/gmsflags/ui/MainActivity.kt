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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.polodarb.gmsflags.data.datastore.DataStoreManager
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.navigation.RootAppNavigation
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme


class MainActivity : ComponentActivity() {

    private var shellInitialized: Boolean = false
    private var isFirstStart: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val datastore = DataStoreManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            datastore.getFromDataStore().collect {
                isFirstStart = it
                Log.e("dts", it.toString())
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

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

    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putBoolean(::shellInitialized.name, shellInitialized)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        shellInitialized = savedInstanceState.getBoolean(::shellInitialized.name)
        super.onRestoreInstanceState(savedInstanceState)
    }
}