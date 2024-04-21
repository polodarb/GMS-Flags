plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ua.polodarb.flagschange"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // Coil
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    // Ktor
    implementation(libs.ktor.serialization.json)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.compose)
    implementation(libs.koin.work.manager)

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    // Navigation
    implementation(libs.navigation.compose)

    // Jetpack Compose
    platform(libs.compose.bom).let { bom ->
        implementation(bom)
        androidTestImplementation(bom)
        debugImplementation(bom)
    }
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation)
    implementation(libs.compose.icons)
    implementation(libs.work.runtime.ktx)
    androidTestImplementation(libs.compose.test.juni4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.test.manifest)

    implementation(libs.core.ktx)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)

    implementation(project(":data:repository"))
    implementation(project(":core:ui"))
    implementation(project(":core:platform"))
    implementation(project(":core:common"))

}