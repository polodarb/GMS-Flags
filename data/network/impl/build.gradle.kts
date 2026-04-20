plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.network.impl"

    defaultConfig {
        buildConfigField("long", "VERSION_CODE", libs.versions.version.code.get())
        buildConfigField("String","VERSION_NAME","\"${libs.versions.version.name.get()}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // Ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.core)
    implementation(libs.ktor.android)
    implementation(libs.ktor.negotation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.logging)

    // KtRssRss
    implementation(libs.rssReader)

    implementation(projects.data.network)
}
