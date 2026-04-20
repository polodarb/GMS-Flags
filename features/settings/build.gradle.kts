plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.root)
}

android {
    namespace = "ua.polodarb.settings"

    defaultConfig {
        buildConfigField("long", "VERSION_CODE", libs.versions.version.code.get())
        buildConfigField("String","VERSION_NAME","\"${libs.versions.version.name.get()}\"")
    }

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

    implementation(projects.data.repository)
    implementation(projects.data.preferences)
    implementation(projects.core.ui)
}
