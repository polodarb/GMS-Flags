plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.room)
}

android {
    namespace = "ua.polodarb.local.impl"
}

dependencies {

    implementation(projects.data.databases.local)
}
