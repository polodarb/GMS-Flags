package ua.polodarb.flagsChange.mappers

fun List<String>.mapToExtractBoolData(data:  Map<String, String>): Map<String, Boolean> {
    val arr: MutableMap<String, Boolean> = mutableMapOf()
    this.map { selectedItem ->
        val value = data[selectedItem]
        if (value != null) {
            val valueData = value == "1"
            arr[selectedItem] = valueData
        }
    }
    return arr
}
