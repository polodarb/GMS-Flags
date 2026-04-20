import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ua.polodarb.buildlogic.implementation
import ua.polodarb.buildlogic.libs

class AndroidRootConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.libsu.core)
                implementation(libs.libsu.service)
                implementation(libs.libsu.nio)
            }
        }
    }
}
