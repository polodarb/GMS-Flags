plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.protobuf"
    defaultConfig {
        minSdk = 24
    }
}

dependencies {
}
