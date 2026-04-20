plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.root)
}

android {
    namespace = "ua.polodarb.repository.impl"
}

dependencies {

    implementation(libs.jackson.dataformat)
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.javax.xml.stream)
    implementation(libs.woodstox)
    implementation(libs.documentfile)

    implementation(projects.core.platform)

    implementation(projects.data.repository)
    implementation(projects.data.databases.gms)
}
