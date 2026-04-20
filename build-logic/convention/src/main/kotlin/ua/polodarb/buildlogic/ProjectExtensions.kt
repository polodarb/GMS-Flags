package ua.polodarb.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.plugins.PluginAware
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the
import org.gradle.plugin.use.PluginDependency

typealias CommonExtension = com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>

fun Project.whenAndroidIsApplied(
    action: (CommonExtension) -> Unit
) {
    pluginManager.withPlugin("com.android.application") {
        action(extensions.getByType<ApplicationExtension>())
    }
    pluginManager.withPlugin("com.android.library") {
        action(extensions.getByType<LibraryExtension>())
    }
}

val Project.libs get() = the<LibrariesForLibs>()

fun DependencyHandlerScope.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandlerScope.debugImplementation(dependency: Any) {
    add("debugImplementation", dependency)
}

fun DependencyHandlerScope.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandlerScope.ksp(dependency: Any) {
    add("ksp", dependency)
}

fun PluginAware.apply(plugin: Provider<PluginDependency>) {
    apply(plugin = plugin.get().pluginId)
}
