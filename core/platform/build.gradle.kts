plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.root)
}

android {
    namespace = "ua.polodarb.platform"
}

dependencies {

    implementation(projects.core.common)
}
