import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ua.polodarb.buildlogic.androidTestImplementation
import ua.polodarb.buildlogic.apply
import ua.polodarb.buildlogic.debugImplementation
import ua.polodarb.buildlogic.implementation
import ua.polodarb.buildlogic.libs
import ua.polodarb.buildlogic.whenAndroidIsApplied

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            project.whenAndroidIsApplied { ext ->
                apply(libs.plugins.kotlin.compose)

                ext.apply {
                    buildFeatures {
                        compose = true
                    }
                }

                dependencies {
                    platform(libs.compose.bom).let { bom ->
                        implementation(bom)
                        androidTestImplementation(bom)
                    }

                    implementation(libs.compose.ui)
                    implementation(libs.compose.material3)
                    implementation(libs.compose.animation)
                    implementation(libs.compose.icons)
                    implementation(libs.compose.ui.tooling.preview)
                    debugImplementation(libs.compose.ui.tooling)
                    debugImplementation(libs.compose.test.manifest)
                }
            }
        }
    }
}
