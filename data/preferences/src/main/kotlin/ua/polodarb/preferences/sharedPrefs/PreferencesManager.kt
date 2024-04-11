package ua.polodarb.preferences.sharedPrefs

interface PreferencesManager {

    fun saveData(key: String, value: String)

    fun getData(key: String, defaultValue: String): String

}