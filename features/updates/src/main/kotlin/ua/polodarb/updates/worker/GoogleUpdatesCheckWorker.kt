package ua.polodarb.updates.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ua.polodarb.network.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.preferences.datastore.DatastoreManager
import ua.polodarb.preferences.datastore.models.LastUpdatesAppModel
import ua.polodarb.preferences.sharedPrefs.PreferenceConstants
import ua.polodarb.preferences.sharedPrefs.PreferencesManager
import ua.polodarb.repository.googleUpdates.mapper.GoogleUpdatesMapper
import ua.polodarb.repository.googleUpdates.model.MainRssArticle
import ua.polodarb.updates.R

const val GOOGLE_UPDATES_WORKER_TAG = "GOOGLE_UPDATES_WORKER_TAG"
const val CHANNEL_ID = "gms_flags_notify_channel"

class GoogleUpdatesCheckWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
    private val googleAppUpdatesService: GoogleAppUpdatesService,
    private val googleUpdatesMapper: GoogleUpdatesMapper,
    private val sharedPrefs: PreferencesManager,
    private val datastore: DatastoreManager
) : CoroutineWorker(context, workerParameters), KoinComponent {

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
                ) // TODO: delete after a few updates
                datastore.setLastUpdatedGoogleApp(
                    LastUpdatesAppModel(
                        appName = result.articles.first().title,
                        appVersion = result.articles.first().version
                    )
                )
            }
            Result.success()
        } catch (err: Throwable) {
            err.printStackTrace()
            Log.e("GMS Flags", "GoogleUpdatesCheckWorker - ${err.message}")
            Result.failure()
        }
    }

    private suspend fun getNewArticlesString(newArticles: List<MainRssArticle>): String {
        val localData = datastore.getLastUpdatedGoogleApp()

        val indexOfLocalArticle = newArticles.indexOfFirst { article ->
            article.title == localData.appName && article.version == localData.appVersion
        }

        val resultStringBuilder = StringBuilder()

        if (indexOfLocalArticle >= 0 && indexOfLocalArticle <= newArticles.lastIndex) {
            for (i in 0 until indexOfLocalArticle) {
                val article = newArticles.getOrNull(i)
                if (article != null) {
                    resultStringBuilder.append("${article.title} (${article.version})\n")
                }
            }
        } else {
            newArticles.forEach { article ->
                resultStringBuilder.append("${article.title} (${article.version})\n")
            }
        }

        return resultStringBuilder.toString()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationId = getRandomNotifyID()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notify_logo)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            val existingNotification = activeNotifications.find { it.id == notificationId }

            if (existingNotification != null) {
                builder.setContentText(
                    "$message\n" +
                    existingNotification.notification?.extras?.getString(
                        NotificationCompat.EXTRA_TEXT,
                        ""
                    )
                )
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }
    }


    private fun getRandomNotifyID(): Int {
        return System.currentTimeMillis().toInt()
    }

}