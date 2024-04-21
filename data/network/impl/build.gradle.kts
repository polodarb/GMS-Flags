plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ua.polodarb.network.impl"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        buildConfigField("long", "VERSION_CODE", libs.versions.version.code.get())
        buildConfigField("String","VERSION_NAME","\"${libs.versions.version.name.get()}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    // Ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.core)
    implementation(libs.ktor.android)
    implementation(libs.ktor.negotation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.logging)

    // KtRssRss
    implementation(libs.rssReader)

    implementation(libs.core.ktx)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)

    implementation(project(":data:network"))
}