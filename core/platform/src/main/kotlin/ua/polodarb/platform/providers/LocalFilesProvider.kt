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
        val file = getLocalSuggestedFlagsFile()
        return if (file.exists()) {
            file.readText()
        } else {
            ""
        }
    }
}
