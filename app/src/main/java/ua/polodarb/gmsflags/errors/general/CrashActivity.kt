package ua.polodarb.gmsflags.errors.general

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.R
import ua.polodarb.common.Constants
import ua.polodarb.gmsflags.errors.general.ExceptionHandler.Companion.STACK_TRACE_KEY
import ua.polodarb.gmsflags.errors.gms.missingDB.MissingDbScreen
import ua.polodarb.gmsflags.errors.gms.phixit.PhixitDetectScreen
import ua.polodarb.gmsflags.ui.MainActivity
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import ua.polodarb.platform.utils.OSUtils
import kotlin.system.exitProcess

class CrashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, false)

        val intentMain = Intent(this, MainActivity::class.java)
        val stackTraceKey = intent.getStringExtra(STACK_TRACE_KEY)

        if (intent.getStringExtra(STACK_TRACE_KEY)?.contains(Constants.GMS_DB_CRASH_MSG) == true) {
            startActivity(intentMain)
            finishAffinity()
        }

        setContent {
            GMSFlagsTheme {
                when {
                    stackTraceKey?.contains(Constants.GMS_DB_CRASH_MSG_PHIXIT) == true -> {
                        PhixitDetectScreen()
                    }

                    stackTraceKey?.contains("Database not found") == true -> {
                        MissingDbScreen()
                    }

                    else -> {
                        GeneralCrashScreen(
                            sendReport = {
                                try {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:")
                                        putExtra(Intent.EXTRA_EMAIL, arrayOf("gmsflags@gmail.com"))
                                        putExtra(
                                            Intent.EXTRA_SUBJECT,
                                            getString(R.string.crash_report_subject)
                                        )
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            intent.getStringExtra(STACK_TRACE_KEY)
                                        )
                                    }
                                    if (intent.resolveActivity(packageManager) != null) {
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "No app to send email. Please install at least one",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (_: ActivityNotFoundException) {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.crash_report_failed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            restartApp = {
                                startActivity(Intent(this, MainActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        }
    }
}

/*
 * This error handler implementation is taken from the https://bit.ly/3LgGlJJ with minor
 * modifications. Credits to https://github.com/emirhankolver for the original code.
 */
class ExceptionHandler(
    private val context: Context,
    private val handler: Thread.UncaughtExceptionHandler,
    private val activity: Class<*>
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val intent = Intent(context, activity).apply {
                putExtra(STACK_TRACE_KEY, throwable.stackTraceToReport())
                putExtra(CRASH_MESSAGE, throwable.message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
            FirebaseCrashlytics.getInstance().recordException(throwable)
            exitProcess(0)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            handler.uncaughtException(thread, throwable)
        }
    }

    companion object {
        fun initialize(
            context: Context,
            activity: Class<*>
        ) {
            Thread.setDefaultUncaughtExceptionHandler(
                Thread.getDefaultUncaughtExceptionHandler()?.let {
                    ExceptionHandler(context, it, activity)
                }
            )
        }

        const val STACK_TRACE_KEY = "STACK_TRACE"
        const val CRASH_MESSAGE = "CRASH_MESSAGE"
    }

    private fun Throwable.stackTraceToReport(): String {
        return """
            Model: ${Build.DEVICE} (${Build.BOARD})
            Manufacturer: ${Build.MANUFACTURER}
            Android: ${Build.VERSION.RELEASE}
            Manufacturer OS: ${OSUtils.sName} (${OSUtils.sVersion})
            GMS Flags: ${BuildConfig.VERSION_CODE} (${BuildConfig.VERSION_NAME})
            
            ${this.stackTraceToString()}
        """.trimIndent()
    }
}
