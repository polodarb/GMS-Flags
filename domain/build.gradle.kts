plugins {
    alias(libs.plugins.gmsflags.android.library)
    alias(libs.plugins.gmsflags.android.koin)
}

android {
    namespace = "ua.polodarb.domain"

    defaultConfig {
        buildConfigField("long", "VERSION_CODE", libs.versions.version.code.get())
        buildConfigField("String","VERSION_NAME","\"${libs.versions.version.name.get()}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    implementation(projects.core.byteUtils)
    implementation(projects.data.repository)
}
