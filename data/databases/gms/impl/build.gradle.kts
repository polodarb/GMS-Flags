plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.root)
}

android {
    namespace = "ua.polodarb.gms.impl"
}

dependencies {

    // SQLite
    implementation(libs.requery.sqlite)
    implementation(libs.protobuf.java)

    implementation(projects.data.databases.gms)
    implementation(projects.core.common)
    implementation(projects.core.xposedInfo)
}
