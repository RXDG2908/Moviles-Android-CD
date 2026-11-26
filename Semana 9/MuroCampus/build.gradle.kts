// Gradle raiz: versiones de los plugins del proyecto.
plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    // Convierte JSON en data class; misma version que Kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
}
