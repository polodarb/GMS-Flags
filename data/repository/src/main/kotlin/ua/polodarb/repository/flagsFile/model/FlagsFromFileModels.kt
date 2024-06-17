package ua.polodarb.repository.flagsFile.model

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@Keep
@JsonRootName("package")
data class PackageFlags(
    @JsonProperty("name") val packageName: String,
    @JsonProperty("flags") val flags: List<Flag>
)

@Keep
data class Flag(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("value") val value: String
)

@Keep
data class LoadedFlags(
    val packageName: String,
    val flags: LoadedFlagData
)

@Keep
data class LoadedFlagData(
    val bool: Map<String, Boolean> = emptyMap(),
    val int: Map<String, Int> = emptyMap(),
    val float: Map<String, Float> = emptyMap(),
    val string: Map<String, String> = emptyMap(),
    val extVal: Map<String, String> = emptyMap()
) {
    fun size(): Int {
        return bool.size + int.size + float.size + string.size + extVal.size
    }

    fun getFlagsList(): List<LoadedFlagDetails> {
        return mutableListOf<LoadedFlagDetails>().apply {
            bool.forEach { (key, value) -> add(LoadedFlagDetails(key, "boolean", value)) }
            int.forEach { (key, value) -> add(LoadedFlagDetails(key, "int", value)) }
            float.forEach { (key, value) -> add(LoadedFlagDetails(key, "float", value)) }
            string.forEach { (key, value) -> add(LoadedFlagDetails(key, "string", value)) }
            extVal.forEach { (key, value) -> add(LoadedFlagDetails(key, "extVal", value)) }
        }
    }

}

@Keep
data class LoadedFlagDetails(
    val name: String,
    val type: String,
    val value: Any,
    val override: Boolean = true
)