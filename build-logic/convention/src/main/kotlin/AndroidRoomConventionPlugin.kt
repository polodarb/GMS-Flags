import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ua.polodarb.buildlogic.apply
import ua.polodarb.buildlogic.implementation
import ua.polodarb.buildlogic.ksp
import ua.polodarb.buildlogic.libs

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(libs.plugins.ksp)

            dependencies {
                implementation(libs.room.runtime)
                implementation(libs.room.ktx)
                ksp(libs.room.compiler)
            }
        }
    }
}
