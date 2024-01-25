package ua.polodarb.gmsflags.data.prefs.shared

import android.content.Context
import android.content.SharedPreferences

object PreferenceConstants {
    const val PREFERENCES_NAME = "gms_flags_prefs"
    const val START_SCREEN_KEY = "settings_navigation"
    const val GOOGLE_LAST_UPDATE = "google_last_update"
}

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PreferenceConstants.PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}
