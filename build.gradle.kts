plugins {
    id("com.android.application") version "9.1.1" apply false
    // org.jetbrains.kotlin.android removed: built into AGP 9.0+, applying it is now a fatal error
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.21" apply false
    id("com.google.devtools.ksp") version "2.3.7" apply false
}
