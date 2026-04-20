plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
}

android {
    namespace = "ua.polodarb.onboarding"
}

dependencies {

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    // Coil
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    // Navigation
    implementation(libs.navigation.compose)

    // Compose extras
    implementation(libs.work.runtime.ktx)

    implementation(projects.core.common)
    implementation(projects.core.ui)
}
