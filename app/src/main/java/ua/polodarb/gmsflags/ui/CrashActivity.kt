package ua.polodarb.gmsflags.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.R
import ua.polodarb.common.Constants
import ua.polodarb.gmsflags.ui.ExceptionHandler.Companion.STACK_TRACE_KEY
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import ua.polodarb.platform.utils.OSUtils
import kotlin.system.exitProcess

class CrashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, false)

        val intentMain = Intent(this, MainActivity::class.java)
        if (intent.getStringExtra(STACK_TRACE_KEY)?.contains(Constants.GMS_DATABASE_CRASH_MSG) == true) {
            startActivity(intentMain)
            finishAffinity()
        }

        setContent {
            GMSFlagsTheme {
                CrashScreen(
                    sendReport = {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("gmsflags@gmail.com"))
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    getString(R.string.crash_report_subject)
                                )
                                putExtra(Intent.EXTRA_TEXT, intent.getStringExtra(STACK_TRACE_KEY))
                            }
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "No app to send email. Please install at least one", Toast.LENGTH_SHORT).show()
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

    @Composable
    private fun CrashScreen(
        sendReport: () -> Unit,
        restartApp: () -> Unit
    ) {
        Surface {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.error_image),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 44.dp)
                )
                Text(
                    text = stringResource(id = R.string.crash_title),
                    fontSize = 46.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
                )
                Text(
                    text = stringResource(id = R.string.crash_msg),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Text(
                    text = stringResource(id = R.string.crash_advice),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 36.dp)
                ) {
                    OutlinedButton(
                        onClick = sendReport,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.crash_report),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Button(
                        onClick = restartApp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.crash_restart),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
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
                ExceptionHandler(context, Thread.getDefaultUncaughtExceptionHandler()!!, activity)
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
