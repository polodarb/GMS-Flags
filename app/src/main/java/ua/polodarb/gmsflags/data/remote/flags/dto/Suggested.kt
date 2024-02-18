package ua.polodarb.gmsflags.data.remote.flags.dto

import kotlinx.serialization.Serializable

@Serializable
sealed class Suggested {
    abstract val name: String
    abstract val source: String?
    abstract val note: String?
    abstract val flags: List<FlagInfo>
    abstract val flagPackage: String
    abstract val appPackage: String
    abstract val minVersionCode: Int?
    abstract val minAndroidSdkCode: Int?
    abstract val details: String?
    abstract val enabled: Boolean
}
