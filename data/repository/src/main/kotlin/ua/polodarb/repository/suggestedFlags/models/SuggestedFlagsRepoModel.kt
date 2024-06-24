package ua.polodarb.repository.suggestedFlags.models

import ua.polodarb.network.suggestedFlags.model.SuggestedFlagsNetModel
import ua.polodarb.repository.suggestedFlags.models.FlagInfoRepoModel.Companion.toRepoModel

data class SuggestedFlagsRepoModel(
    val title: String,
    val flagPackage: String,
    val appPackage: String,
    val isBeta: Boolean? = false,
    val isEnabled: Boolean? = true,
    val isPrimary: Boolean? = false,
    val group: String? = null,
    val minAppVersionCode: Int? = null,
    val minAndroidSdkCode: Int? = null,
    val source: String? = null,
    val note: String? = null,
    val warning: String? = null,
    val detailsLink: String? = null,
    val tag: String? = null,
    val flags: List<FlagInfoRepoModel>
) {
    companion object {
        fun SuggestedFlagsNetModel.toRepoModel() =
            SuggestedFlagsRepoModel(
                title = title,
                flagPackage = flagPackage,
                isBeta = isBeta,
                isEnabled = isEnabled,
                isPrimary = isPrimary,
                group = group,
                appPackage = appPackage,
                minAppVersionCode = minAppVersionCode,
                minAndroidSdkCode = minAndroidSdkCode,
                source = source,
                note = note,
                warning = warning,
                detailsLink = detailsLink,
                tag = tag,
                flags = flags.map { it.toRepoModel() }
            )
    }
}