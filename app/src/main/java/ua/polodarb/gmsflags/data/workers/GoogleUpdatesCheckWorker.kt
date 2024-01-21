package ua.polodarb.gmsflags.data.workers

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
import ua.polodarb.gmsflags.data.prefs.shared.PreferenceConstants
import ua.polodarb.gmsflags.data.prefs.shared.PreferencesManager
import ua.polodarb.gmsflags.data.remote.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.gmsflags.data.repo.mappers.GoogleUpdatesMapper
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
        return withContext(Dispatchers.Main) {
            Toast.makeText(context, "234", Toast.LENGTH_SHORT).show()
            Log.d("GoogleUpdatesCheckWorker", "doWork")
            Result.success()
        }
//        return try {
//            val result = withContext(Dispatchers.IO) {
//                val response = googleAppUpdatesService.getLatestRelease()
//                googleUpdatesMapper.map(response)
//            }
//            sharedPrefs.saveData(
//                PreferenceConstants.GOOGLE_LAST_UPDATE,
//                "${result.articles.first().title}/${result.articles.first().version}"
//            )
//            Result.success()
//        } catch (err: Throwable) {
//            Result.failure()
//        }
    }

}