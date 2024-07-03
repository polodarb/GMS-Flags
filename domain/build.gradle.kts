plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ua.polodarb.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        buildConfigField("long", "VERSION_CODE", libs.versions.version.code.get())
        buildConfigField("String","VERSION_NAME","\"${libs.versions.version.name.get()}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.compose)
    implementation(libs.koin.work.manager)

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    testImplementation(libs.junit)

    implementation(project(":core:byteUtils"))
    implementation(project(":data:repository"))

}