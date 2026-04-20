plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.updates"
}

dependencies {

    // Navigation
    implementation(libs.navigation.compose)

    // Compose extras
    implementation(libs.work.runtime.ktx)

    implementation(projects.data.repository)
    implementation(projects.core.ui)
}
