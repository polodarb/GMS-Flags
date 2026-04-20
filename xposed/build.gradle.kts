plugins {
    alias(libs.plugins.gmsflags.android.library)
}

android {
    namespace = "ua.polodarb.xposed"

    buildFeatures.buildConfig = true

    defaultConfig {
        ndk {
            abiFilters += setOf("armeabi-v7a", "arm64-v8a")
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "MAIN_APPLICATION_ID", "\"ua.polodarb.gmsflags.indev\"")
        }
        release {
            buildConfigField("String", "MAIN_APPLICATION_ID", "\"ua.polodarb.gmsflags\"")
        }
    }

    externalNativeBuild {
        ndkBuild {
            path("src/main/jni/Android.mk")
        }
    }
}

dependencies {
    compileOnly(libs.xposed.api)
    implementation(libs.dexkit)
    implementation(projects.core.xposedInfo)
}
