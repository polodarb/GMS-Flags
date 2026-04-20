@file:Suppress("UnstableApiUsage")

package ua.polodarb.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

val jvmVersion = JavaVersion.VERSION_17
const val jvmVersionInt = 17

private fun Project.configureKotlinAndroid() {
    apply(libs.plugins.kotlin.android)

    configure<KotlinAndroidProjectExtension> {
        jvmToolchain(jvmVersionInt)
    }

    dependencies {
        implementation(libs.core.ktx)
    }
}

fun Project.configureAndroid(extension: CommonExtension) {
    configureKotlinAndroid()

    extension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 29
        }

        compileOptions {
            sourceCompatibility = jvmVersion
            targetCompatibility = jvmVersion
        }
    }
}
