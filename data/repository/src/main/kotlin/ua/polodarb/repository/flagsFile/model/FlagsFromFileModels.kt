package ua.polodarb.repository.flagsFile.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("package")
data class PackageFlags(
    @JsonProperty("name") val packageName: String,
    @JsonProperty("flags") val flags: List<Flag>
)

data class Flag(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("value") val value: String
)

data class LoadedFlags(
    val packageName: String,
    val flags: LoadedFlagData
)

data class LoadedFlagData(
    val bool: Map<String, Boolean>,
    val int: Map<String, Int>,
    val float: Map<String, Float>,
    val string: Map<String, String>,
    val extVal: Map<String, String>
) {
    fun size(): Int {
        return bool.size + int.size + float.size + string.size + extVal.size
    }

    fun getFlagsList(): List<LoadedFlagDetails> {
        return mutableListOf<LoadedFlagDetails>().apply {
            bool.forEach { (key, value) -> add(LoadedFlagDetails(key, "Boolean", value)) }
            int.forEach { (key, value) -> add(LoadedFlagDetails(key, "Int", value)) }
            float.forEach { (key, value) -> add(LoadedFlagDetails(key, "Float", value)) }
            string.forEach { (key, value) -> add(LoadedFlagDetails(key, "String", value)) }
            extVal.forEach { (key, value) -> add(LoadedFlagDetails(key, "ExtensionVal", value)) }
        }
    }

}

data class LoadedFlagDetails(
    val name: String,
    val type: String,
    val value: Any,
    val override: Boolean = true
)