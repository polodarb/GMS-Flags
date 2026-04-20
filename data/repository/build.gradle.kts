plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.serialization)
}

android {
    namespace = "ua.polodarb.repository"
}

dependencies {

    // XML
    implementation(libs.jackson.dataformat)
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.javax.xml.stream)

    // Ktor serialization
    api(libs.ktor.serialization.json)

    // KtRssRss
    api(libs.rssReader)

    api(projects.data.preferences)
    api(projects.data.network)
    api(projects.data.databases.local)
    api(projects.core.common)
}
