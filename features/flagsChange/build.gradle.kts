plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.flagschange"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // Coil
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    // Ktor
    implementation(libs.ktor.serialization.json)

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    // Navigation
    implementation(libs.navigation.compose)

    // Compose extras
    implementation(libs.work.runtime.ktx)

    implementation(projects.data.repository)
    implementation(projects.core.ui)
    implementation(projects.core.platform)
    implementation(projects.core.common)
    implementation(projects.domain)
}
