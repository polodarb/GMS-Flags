import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ua.polodarb.buildlogic.apply
import ua.polodarb.buildlogic.configureAndroid
import ua.polodarb.buildlogic.libs

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(libs.plugins.android.application)

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)

                defaultConfig.targetSdk = 35

                lint {
                    checkReleaseBuilds = false
                    abortOnError = false
                }
            }
        }
    }
}
