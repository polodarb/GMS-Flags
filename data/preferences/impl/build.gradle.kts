plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.preferences.impl"
}

dependencies {

    // DataStore
    implementation(libs.datastore.preferences)

    implementation(projects.data.preferences)
}
