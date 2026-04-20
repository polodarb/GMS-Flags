import org.gradle.api.Plugin
import org.gradle.api.Project
import ua.polodarb.buildlogic.apply
import ua.polodarb.buildlogic.libs

class AndroidSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(libs.plugins.kotlin.serialization)
        }
    }
}
