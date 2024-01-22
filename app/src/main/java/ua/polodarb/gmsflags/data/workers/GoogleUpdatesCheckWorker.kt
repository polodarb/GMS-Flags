package ua.polodarb.gmsflags.data.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.core.platform.activity.BaseActivity
import ua.polodarb.gmsflags.core.platform.activity.CHANNEL_ID
import ua.polodarb.gmsflags.data.prefs.shared.PreferenceConstants
import ua.polodarb.gmsflags.data.prefs.shared.PreferencesManager
import ua.polodarb.gmsflags.data.remote.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.gmsflags.data.repo.mappers.GoogleUpdatesMapper
import ua.polodarb.gmsflags.data.repo.mappers.NewRssArticle
import ua.polodarb.gmsflags.ui.screens.UiStates
import java.util.concurrent.TimeUnit

const val GOOGLE_UPDATES_WORKER_TAG = "GOOGLE_UPDATES_WORKER_TAG"

class GoogleUpdatesCheckWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
    private val googleAppUpdatesService: GoogleAppUpdatesService,
    private val googleUpdatesMapper: GoogleUpdatesMapper,
    private val sharedPrefs: PreferencesManager
) :
    CoroutineWorker(context, workerParameters), KoinComponent {

    override suspend fun doWork(): Result {
        return try {
            val result = withContext(Dispatchers.IO) {
                val response = googleAppUpdatesService.getLatestRelease()
                googleUpdatesMapper.map(response)
            }
            val newArticles = getNewArticlesString(result.articles)
            if (newArticles.isNotEmpty()) {
                sendNotification("Google app updates", newArticles)
                sharedPrefs.saveData(
                    PreferenceConstants.GOOGLE_LAST_UPDATE,
                    "${result.articles.first().title}/${result.articles.first().version}"
                )
            }
            Result.success()
        } catch (err: Throwable) {
            Result.failure()
        }
    }

    private fun getNewArticlesString(newArticles: List<NewRssArticle>): String {
        val localData = sharedPrefs.getData(PreferenceConstants.GOOGLE_LAST_UPDATE, "")
        val (localTitle, localVersion) = localData.split("/")

        val filteredData = newArticles.filter { article ->
            article.title == localTitle && article.version == localVersion
        }

        val resultStringBuilder = StringBuilder()

        filteredData.forEachIndexed { index, article ->
            resultStringBuilder.append("${article.title} (${article.version})\n")
        }

        return resultStringBuilder.toString()
    }

    private fun sendNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notify_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(getRandomNotifyID(), builder.build())
        }
    }

    fun getRandomNotifyID(): Int {
        return System.currentTimeMillis().toInt()
    }

}