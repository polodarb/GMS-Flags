package ua.polodarb.gmsflags.data.repo.mappers

import tw.ktrssreader.kotlin.model.channel.RssStandardChannel
import tw.ktrssreader.kotlin.model.channel.RssStandardChannelData
import tw.ktrssreader.kotlin.model.item.RssStandardItem
import ua.polodarb.gmsflags.data.remote.googleUpdates.dto.Article
import ua.polodarb.gmsflags.data.remote.googleUpdates.dto.RssMainModel
import java.text.SimpleDateFormat
import java.util.Locale

class GoogleUpdatesMapper {

    fun map(response: RssStandardChannel): NewRssModel {
        return NewRssModel(
            articles = mapArticle(response.items.orEmpty())
        )
    }

    private fun mapArticle(response: List<RssStandardItem>): List<NewRssArticle> {
        return response.mapNotNull { article ->
            val regex = Regex("""(.+?)\s(\d+\.\d+\.\d+)""")
            val matchResult = regex.find(article.title.orEmpty())

            matchResult?.let {
                val appName = it.groupValues[1]
                val appVersion = it.groupValues[2]

                NewRssArticle(
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

data class NewRssModel(
    val articles: List<NewRssArticle>
)

data class NewRssArticle(
    val title: String,
    val version: String,
    val date: String,
    val link: String,
)

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