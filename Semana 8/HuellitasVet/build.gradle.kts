// Archivo de build de nivel superior
plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    // La versión de KSP debe coincidir con la de Kotlin
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
}
