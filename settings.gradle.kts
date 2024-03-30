pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
rootProject.name = "GMS Flags"
include(":app")
include(":core:platform")
include(":core:navigation")
include(":core:ui")
include(":data:repository")
include(":data:network")
include(":data:database")
include(":data:preferences")
include(":data:model")
include(":domain")
include(":features:onboarding")
include(":features:suggestions")
include(":features:search")
include(":features:saved")
include(":features:updates")
include(":features:flagsChange")
include(":features:settings")
