// ROOT CAUSE FIX
// `plugins {}` inside `pluginManagement {}` does not support `apply false`.
// That modifier is project-level only. Having it here caused Gradle to
// silently discard the entire `repositories {}` block, so only the Gradle
// Plugin Portal was searched — google() was never reached, KSP never found.
//
// Correct structure: pluginManagement owns only repositories.
// All `id + version + apply false` declarations belong in build.gradle.kts.

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "StellarPath"
include(":app")
