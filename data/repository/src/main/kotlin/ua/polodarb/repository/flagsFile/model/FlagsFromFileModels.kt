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
)