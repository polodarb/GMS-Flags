package ua.polodarb.suggestions.specialFlags.callScreen

import kotlinx.serialization.Serializable

@Serializable
internal data class A6(
    val a7: ByteArray
)

@Serializable
internal data class Language(
    val languageCode: String,
    val a6: A6
)

@Serializable
internal data class LanguageConfig(
    val languages: List<Language>
)

@Serializable
internal data class CountryConfig(
    val country: String,
    val languageConfig: LanguageConfig
)

@Serializable
internal data class CallScreenI18nConfig(
    val countryConfigs: List<CountryConfig>
)