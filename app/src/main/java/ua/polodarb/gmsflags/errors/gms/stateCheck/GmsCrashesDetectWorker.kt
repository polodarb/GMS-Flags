package ua.polodarb.gmsflags.errors.gms.stateCheck

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.delay
import ua.polodarb.gmsflags.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class GmsCrashesDetectWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val gmsPID = getGmsPid() ?: return@withContext Result.failure()

                val logs = getLogsForLastMinute(gmsPID)

                if (logs.isEmpty() && runAttemptCount < 3) {
                    delay(5000)
                    return@withContext Result.retry()
                }

                if (logs.any { it.contains("no such table: config_packages") }) {
                    showNotification()
                }

                Result.success()
            } catch (e: Exception) {
                Log.e("LogCheckWorker", "Error reading logs", e)
                Result.failure()
            }
        }
    }

    private fun getGmsPid(): String? {
        val process = Shell.cmd("pidof -s com.google.android.gms").exec()
        return process.out.firstOrNull()?.toString()
    }

    private fun getLogsForLastMinute(pid: String): List<String> {
        val logs = mutableListOf<String>()
        val process = Shell.cmd("logcat -d --pid=$pid *:E").exec()

        val oneMinuteAgo = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1)

        process.out.forEach { line ->
            parseLogLine(line)?.let { (timestamp, log) ->
                if (timestamp >= oneMinuteAgo) logs.add(log)
            }
        }

        return logs
    }

    private fun parseLogLine(line: String): Pair<Long, String>? {
        val logTimePattern = Pattern.compile("""(\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3})""")
        val matcher = logTimePattern.matcher(line)
        return if (matcher.find()) {
            val logTimeString = matcher.group(1)
            convertToDateTimestamp(logTimeString)?.let { timestamp ->
                timestamp to line
            }
        } else {
            null
        }
    }

    private fun convertToDateTimestamp(dateString: String?): Long? {
        if (dateString.isNullOrEmpty()) return null
        return try {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val updatedDateString = "$currentYear-$dateString"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }
            dateFormat.parse(updatedDateString)?.time
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing log time: $dateString", e)
            null
        }
    }

    private fun showNotification() {
        val channelId = "gms_crash_channel"
        val notificationId = 101

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling ActivityCompat#requestPermissions
            return
        }

        val notificationManager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(channelId, "GMS Crash Notifications", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Notifications for GMS crashes and critical states"
        }
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(context, GmsCrashesDetectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notify_logo)
            .setContentTitle(context.getString(R.string.gms_crashes_notifications_title))
            .setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.gms_crashes_notifications_content)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_home, context.getString(R.string.gms_crashes_notifications_action), pendingIntent)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(Color.RED)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val TAG = "GmsCrashesDetectWorker"

        fun initWorker(): PeriodicWorkRequest {
            val constraints =
                Constraints.Builder()
                    .build()

            return PeriodicWorkRequestBuilder<GmsCrashesDetectWorker>(8, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(TAG)
                .build()
        }
    }
}
