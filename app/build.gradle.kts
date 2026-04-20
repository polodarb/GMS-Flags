import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.gmsflags.android.application)
    alias(libs.plugins.gmsflags.android.compose)
    alias(libs.plugins.gmsflags.android.serialization)
    alias(libs.plugins.gmsflags.android.koin)
    alias(libs.plugins.gmsflags.android.root)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.detekt)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val requiresSigning = keystorePropertiesFile.exists()

android {
    namespace = "ua.polodarb.gmsflags"

    signingConfigs {
        if (requiresSigning) {
            val keystoreProperties = Properties().apply {
                load(FileInputStream(keystorePropertiesFile))
            }
            create("release") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "ua.polodarb.gmsflags"
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            if (requiresSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".indev"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Splash Screen
    implementation(libs.core.splashscreen)

    // Activity
    implementation(libs.activity.compose)

    // Lifecycle
    implementation(libs.lifecycle.runtime)

    // Navigation
    implementation(libs.navigation.compose)

    // DataStore
    implementation(libs.datastore.preferences)

    // Compose extras
    implementation(libs.work.runtime.ktx)

    // Scrollbar library for Jetpack Compose
    implementation(libs.lazyColumnScrollbar)

    // Material
    implementation(libs.google.material)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.messaging)

    // Ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.core)
    implementation(libs.ktor.android)
    implementation(libs.ktor.negotation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.logging)

    // KtRssRss
    implementation(libs.rssReader)

    // SQLite
    implementation(libs.requery.sqlite)

    // Coil
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    // Kotlin immutable collections
    implementation(libs.kotlin.collections.immutable)

    implementation(projects.core.platform)
    implementation(projects.core.ui)
    implementation(projects.core.common)

    implementation(projects.data.repository)
    implementation(projects.data.repository.impl)

    implementation(projects.data.preferences)
    implementation(projects.data.preferences.impl)

    implementation(projects.data.network)
    implementation(projects.data.network.impl)

    implementation(projects.data.databases.local)
    implementation(projects.data.databases.local.impl)
    implementation(projects.data.databases.gms)
    implementation(projects.data.databases.gms.impl)

    implementation(projects.features.updates)
    implementation(projects.features.saved)
    implementation(projects.features.settings)
    implementation(projects.features.onboarding)
    implementation(projects.features.search)
    implementation(projects.features.suggestions)
    implementation(projects.features.flagsChange)
    implementation(projects.features.flagsFile)

    implementation(projects.domain)

    implementation(projects.xposed)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_reports")
    metricsDestination = layout.buildDirectory.dir("compose_metrics")
}

detekt {
    source.setFrom("src/main/java", "src/main/kotlin")
    config.setFrom("../config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    ignoreFailures = true
}

afterEvaluate {
    tasks.names.filter { Regex("^compile.*Kotlin\$").matches(it) }.forEach { task ->
        tasks.findByName(task)?.dependsOn(tasks.detekt)
    }
}
