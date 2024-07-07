package ua.polodarb.platform.providers

import android.app.Application
import android.content.Context
import java.io.File
import java.io.IOException

class LocalFilesProvider(
    private val context: Context
) {

    private val gmsApplication = context.applicationContext as Application

    fun getLocalSuggestedFlagsFile(): File {
        return File(gmsApplication.filesDir.absolutePath + File.separator + "suggestedFlags.json")
    }

    fun getSuggestedFlagsDataFromAssets(): String {
        return try {
            val assetManager = context.assets
            val inputStream = assetManager.open("suggestedFlags.json")
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getSuggestedFlagsData(): String {
        val file = getLocalSuggestedFlagsFile()
        return if (file.exists()) {
            file.readText()
        } else {
            ""
        }
    }
}
