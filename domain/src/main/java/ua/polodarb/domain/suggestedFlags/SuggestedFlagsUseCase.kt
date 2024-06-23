package ua.polodarb.domain.suggestedFlags

import android.os.Build
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ua.polodarb.domain.BuildConfig
import ua.polodarb.domain.suggestedFlags.models.SuggestedFlagsModel
import ua.polodarb.repository.appsList.AppsListRepository
import ua.polodarb.repository.suggestedFlags.SuggestedFlagsRepository
import ua.polodarb.repository.suggestedFlags.models.MergedAllTypesOverriddenFlags
import ua.polodarb.repository.suggestedFlags.models.SuggestedFlagsRepoModel

class SuggestedFlagsUseCase(
    val repository: SuggestedFlagsRepository,
    private val appsRepository: AppsListRepository
) {

    operator fun invoke(): Flow<List<SuggestedFlagsModel>?> = flow {
        try {
            val rawSuggestedFlag = repository.loadSuggestedFlags() ?: emptyList()
            val overriddenFlags = fetchOverriddenFlags(rawSuggestedFlag.map { it.flagPackage })
            val data = mutableListOf<SuggestedFlagsModel>()
            val isCurrentAppStable = !BuildConfig.VERSION_NAME.contains("beta", ignoreCase = true)

            rawSuggestedFlag.forEach { flag ->
                val minAndroidSdkCode = flag.minAndroidSdkCode
                val minVersionCode = flag.minAppVersionCode
                val appVersionCode = appsRepository.getAppVersionCode(flag.appPackage)
                val isEnabled = flag.isEnabled ?: true
                val versionAndroidCheck =
                    minAndroidSdkCode == null || Build.VERSION.SDK_INT >= minAndroidSdkCode
                val versionAppCheck = minVersionCode == null || minVersionCode <= appVersionCode
                val flagIsBeta = flag.isBeta ?: false
                val isBetaCheck = !(isCurrentAppStable && flagIsBeta)

                if (isBetaCheck && isEnabled && appVersionCode != -1L && versionAndroidCheck && versionAppCheck) {
                    val isFlagEnabled = isFlagOverridden(flag, overriddenFlags)
                    data.add(SuggestedFlagsModel(flag = flag, enabled = isFlagEnabled))
                }
            }

            emit(data.distinct().toImmutableList())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchOverriddenFlags(packages: List<String>): Map<String, MergedAllTypesOverriddenFlags> {
        val overriddenFlagsMap = mutableMapOf<String, MergedAllTypesOverriddenFlags>()

        packages.forEach { pkg ->
            if (overriddenFlagsMap[pkg] == null) {
                overriddenFlagsMap[pkg] = repository.getMergedOverriddenFlagsByPackage(pkg).first()
            }
        }

        return overriddenFlagsMap
    }

    private fun isFlagOverridden(flag: SuggestedFlagsRepoModel, overriddenFlags: Map<String, MergedAllTypesOverriddenFlags>): Boolean {
        return flag.flags.firstOrNull {
            overriddenFlags[flag.flagPackage]?.boolFlag?.get(it.name) != it.value &&
                    overriddenFlags[flag.flagPackage]?.intFlag?.get(
                        it.name
                    ) != it.value &&
                    overriddenFlags[flag.flagPackage]?.floatFlag?.get(
                        it.name
                    ) != it.value &&
                    overriddenFlags[flag.flagPackage]?.stringFlag?.get(
                        it.name
                    ) != it.value
        } == null
    }

}
