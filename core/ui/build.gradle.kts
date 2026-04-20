plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
}

android {
    namespace = "ua.polodarb.ui"
}

dependencies {

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    // Scrollbar library for Jetpack Compose
    implementation(libs.lazyColumnScrollbar)

    // Compose extras
    implementation(libs.work.runtime.ktx)
}
