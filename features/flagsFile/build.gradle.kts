plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.flagsfile"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    // Navigation
    implementation(libs.navigation.compose)

    // Compose extras
    implementation(libs.work.runtime.ktx)

    implementation(projects.core.ui)
    implementation(projects.data.repository)
    implementation(projects.domain)
}
