package ua.polodarb.repository.googleUpdates.model

data class MainRssModel(
    val articles: List<MainRssArticle>
)

data class MainRssArticle(
    val title: String,
    val version: String,
    val date: String,
    val link: String,
)