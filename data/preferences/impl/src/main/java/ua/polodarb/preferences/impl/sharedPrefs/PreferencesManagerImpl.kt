package ua.polodarb.preferences.impl.sharedPrefs

import android.content.Context
import android.content.SharedPreferences
import ua.polodarb.preferences.sharedPrefs.PreferenceConstants
import ua.polodarb.preferences.sharedPrefs.PreferencesManager

class PreferencesManagerImpl(context: Context): PreferencesManager {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PreferenceConstants.PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

}
