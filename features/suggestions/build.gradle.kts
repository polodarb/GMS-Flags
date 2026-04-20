plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.serialization)
}

android {
    namespace = "ua.polodarb.suggestions"

    defaultConfig {
        buildConfigField("long", "VERSION_CODE", libs.versions.version.code.get())
        buildConfigField("String","VERSION_NAME","\"${libs.versions.version.name.get()}\"")
    }

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

    implementation(libs.kotlinx.serialization.protobuf)

    implementation(projects.data.repository)
    implementation(projects.core.ui)
    implementation(projects.core.platform)
    implementation(projects.domain)
}
