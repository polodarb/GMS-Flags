package ua.polodarb.platform.utils

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import java.util.Locale

/*
 * Taken from https://bit.ly/3PA1IYS. Credits to https://github.com/Z-P-J!
 */
@Suppress("SpellCheckingInspection")
object OSUtils {
    private const val ROM_MIUI = "MIUI"
    private const val ROM_EMUI = "EMUI"
    private const val ROM_FLYME = "FLYME"
    private const val ROM_OPPO = "OPPO"
    private const val ROM_SMARTISAN = "SMARTISAN"
    private const val ROM_VIVO = "VIVO"
    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"

    val sName: String
    var sVersion: String

    init {
        if (!TextUtils.isEmpty(getProperty(KEY_VERSION_MIUI).also {
                sVersion = it
            })) {
            sName = ROM_MIUI
        } else if (!TextUtils.isEmpty(getProperty(KEY_VERSION_EMUI).also {
                sVersion = it
            })) {
            sName = ROM_EMUI
        } else if (!TextUtils.isEmpty(getProperty(KEY_VERSION_OPPO).also {
                sVersion = it
            })) {
            sName = ROM_OPPO
        } else if (!TextUtils.isEmpty(getProperty(KEY_VERSION_VIVO).also {
                sVersion = it
            })) {
            sName = ROM_VIVO
        } else if (!TextUtils.isEmpty(getProperty(KEY_VERSION_SMARTISAN).also {
                sVersion = it
            })) {
            sName = ROM_SMARTISAN
        } else {
            sVersion = Build.DISPLAY
            if (sVersion.uppercase(Locale.getDefault()).contains(ROM_FLYME)) {
                sName = ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.uppercase(Locale.getDefault())
            }
        }
    }

    @SuppressLint("PrivateApi")
    private fun getProperty(key: String): String {
        return Class.forName("android.os.SystemProperties")
            .getMethod("get", String::class.java).invoke(null, key) as String
    }
}