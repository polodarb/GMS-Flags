import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "ua.polodarb.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gmsflags.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "gmsflags.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "gmsflags.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidRoom") {
            id = "gmsflags.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidSerialization") {
            id = "gmsflags.android.serialization"
            implementationClass = "AndroidSerializationConventionPlugin"
        }
        register("androidKoin") {
            id = "gmsflags.android.koin"
            implementationClass = "AndroidKoinConventionPlugin"
        }
        register("androidRoot") {
            id = "gmsflags.android.root"
            implementationClass = "AndroidRootConventionPlugin"
        }
    }
}
