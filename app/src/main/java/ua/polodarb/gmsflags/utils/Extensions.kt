package ua.polodarb.gmsflags.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kotlinx.collections.immutable.toPersistentList
import ua.polodarb.gmsflags.R
import java.util.SortedMap

object Extensions {
    fun String.toFormattedInt(): Int {
        val digits = this.filter { it.isDigit() }
        return digits.toIntOrNull() ?: 0
    }

    fun Map<String, String>.toSortMap(): SortedMap<String, String> {
        return this.toSortedMap(
            compareByDescending<String> {
                it.toLongOrNull()
            }.thenBy { it }
        )
    }

    fun Map<String, String>.filterByEnabled(): Map<String, String> {
        val filteredMap = mutableMapOf<String, String>()
        for ((key, value) in this) {
            if (value == "1") {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    fun Map<String, String>.filterByDisabled(): Map<String, String> {
        val filteredMap = mutableMapOf<String, String>()
        for ((key, value) in this) {
            if (value == "0") {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    fun Boolean.toInt() = if (this) 1 else 0

    fun Context.sendEMail(subject: String, content: String?) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, getString(R.string.developer_email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.send_email_failed), Toast.LENGTH_SHORT).show()
        }
    }

    fun <T> Array<out T>.toPersistentList() = this.toList().toPersistentList()
}
