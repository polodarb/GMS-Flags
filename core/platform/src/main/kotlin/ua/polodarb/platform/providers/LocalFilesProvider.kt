package ua.polodarb.platform.providers

import android.app.Application
import android.content.Context
import java.io.File

class LocalFilesProvider(
    private val context: Context
) {

    private val gmsApplication = context.applicationContext as Application

    fun getLocalSuggestedFlagsFile(): File {
        return File(gmsApplication.filesDir.absolutePath + File.separator + "suggestedFlags.json")
    }

    fun getSuggestedFlagsData(): String {
        val stream = context.assets.open("suggestedFlags.json")
        return stream.bufferedReader().use { it.readText() }
    }

}