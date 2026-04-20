import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ua.polodarb.buildlogic.apply
import ua.polodarb.buildlogic.configureAndroid
import ua.polodarb.buildlogic.libs

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(libs.plugins.android.library)

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
            }
        }
    }
}
