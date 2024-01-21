package ua.polodarb.gmsflags.data.remote.googleUpdates.dto

data class RssMainModel(
    val articles: List<Article>
)

data class Article(
    val title: String,
    val link: String,
    val pubDate: String,
)