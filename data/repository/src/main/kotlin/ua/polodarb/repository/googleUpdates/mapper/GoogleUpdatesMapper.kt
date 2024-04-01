package ua.polodarb.repository.googleUpdates.mapper

import tw.ktrssreader.kotlin.model.channel.RssStandardChannel
import tw.ktrssreader.kotlin.model.item.RssStandardItem
import ua.polodarb.repository.googleUpdates.model.MainRssArticle
import ua.polodarb.repository.googleUpdates.model.MainRssModel
import java.text.SimpleDateFormat
import java.util.Locale

class GoogleUpdatesMapper {

    fun map(response: RssStandardChannel): MainRssModel {
        return MainRssModel(
            articles = mapArticle(response.items.orEmpty())
        )
    }

    private fun mapArticle(response: List<RssStandardItem>): List<MainRssArticle> {
        return response.mapNotNull { article ->
            val regex = Regex("""(.+?)\s(\d+\.\d+\.\d+)""")
            val matchResult = regex.find(article.title.orEmpty())

            matchResult?.let {
                val appName = it.groupValues[1]
                val appVersion = it.groupValues[2]

                MainRssArticle(
                    title = appName,
                    version = appVersion,
                    date = convertDateString(article.pubDate.orEmpty()),
                    link = article.link.orEmpty()
                )
            }
        }.filter {
            !(it.title.contains("Wear OS", ignoreCase = true) ||
                    it.title.contains("Android TV", ignoreCase = true) ||
                    it.title.contains("Trichrome", ignoreCase = true))


        }
    }

}

fun convertDateString(inputDateString: String): String {
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
    val outputFormat = SimpleDateFormat("dd.MM - HH:mm", Locale.getDefault())

    return try {
        val date = inputFormat.parse(inputDateString)
        outputFormat.format(date ?: "Invalid date format")
    } catch (e: Exception) {
        e.printStackTrace()
        "Invalid date format"
    }
}