package ua.polodarb.flagsfile.model

import ua.polodarb.repository.flagsFile.model.LoadedFlags

fun LoadedFlags.toLoadedFlagsUIList(): LoadedFlagsUI {
    val packageName = this.packageName
    val flagsUI = mutableListOf<LoadedFlagUI>()
    this.flags.bool.forEach { (key, value) -> flagsUI.add(LoadedFlagUI(key, "Boolean", value)) }
    this.flags.int.forEach { (key, value) -> flagsUI.add(LoadedFlagUI(key, "Int", value)) }
    this.flags.float.forEach { (key, value) -> flagsUI.add(LoadedFlagUI(key, "Float", value)) }
    this.flags.string.forEach { (key, value) -> flagsUI.add(LoadedFlagUI(key, "String", value)) }
    this.flags.extVal.forEach { (key, value) -> flagsUI.add(LoadedFlagUI(key, "ExtensionVal", value)) }
    return LoadedFlagsUI(packageName, flagsUI)
}
