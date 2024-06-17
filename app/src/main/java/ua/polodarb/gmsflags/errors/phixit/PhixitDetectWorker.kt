package ua.polodarb.gmsflags.errors.phixit

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ua.polodarb.domain.OverrideFlagsUseCase
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.platform.init.InitShell
import ua.polodarb.preferences.datastore.DatastoreManager
import ua.polodarb.repository.databases.gms.GmsDBRepository
import java.util.concurrent.TimeUnit

class PhixitDetectWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
    private val rootDB: InitRootDB,
    private val repository: GmsDBRepository,
    private val datastore: DatastoreManager,
    private val overrideUseCase: OverrideFlagsUseCase
) : CoroutineWorker(context, workerParameters), KoinComponent {

    override suspend fun doWork(): Result {

        val executionCount = datastore.phixitWorkerExecutionCount.first()

        return if (executionCount <= 8) {
            withContext(Dispatchers.Main) {
                InitShell.initShell()
                rootDB.initDB()
            }

            val flags = mapOf(
                "Database__enable_phixit_schema_version" to "0",
                "Database__enable_database_schema_version_1034" to "0", // ??
                "Database__enable_database_schema_version_31" to "0",
                "Database__enable_database_schema_version_32" to "0",
                "Database__phixit_update_registration_generation_on_sync" to "0" // ??
            )

            try {
                val check1 = repository.isPhixitSchemaUsed().first()
                val check2 = repository.isDbFullyRecreated().first()
                val check3 = repository.isFlagOverridesTableEmpty().first()
                val check4 = datastore.isOpenGmsSettingsBtnClicked.first()

                val checkResult = !check1 && check2 && check3 && check4

                Log.e("GMS Flags", "checkResult - $checkResult")

                if (checkResult) {
                    flags.onEachIndexed { index, (name, value) ->
                        val isLast = index == flags.size - 1
                        overrideUseCase(
                            packageName = "com.google.android.gms.phenotype",
                            name = name,
                            boolVal = value,
                            clearData = isLast
                        )
                    }
                    datastore.setOpenGmsSettingsBtnClicked(false)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Setup is complete, GMS Flags is ready for use", Toast.LENGTH_SHORT).show()
                    }
                }

                datastore.phixitWorkerIncrementExecutionCount()

                Result.success()
            } catch (e: Exception) {
                Log.e("GMS Flags", "PhixitDetectWorker Error: ${e.message}")
                Result.failure()
            }
        } else {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
            Result.success()
        }
    }

    companion object {
        const val TAG = "GMSFlagsPhixitCheckWorker"

        fun initWorker(): PeriodicWorkRequest {
            val constraints =
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()

            return PeriodicWorkRequestBuilder<PhixitDetectWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(TAG)
                .build()
        }
    }

}