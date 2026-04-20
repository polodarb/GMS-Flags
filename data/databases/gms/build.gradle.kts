plugins {
    alias(libs.plugins.gmsflags.android.library)
}

android {
    namespace = "ua.polodarb.gms"
    buildFeatures {
        buildConfig = true
        aidl = true
    }
}

dependencies {
    implementation(libs.lifecycle.runtime)
}
