plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.compose)
}

android {
    namespace = "ua.polodarb.common"
}

dependencies {

    // Compose extras
    implementation(libs.work.runtime.ktx)
}
