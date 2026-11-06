// ===== build.gradle (nivel Proyecto) =====
// Aquí solo se declaran los plugins y sus versiones (apply false);
// cada módulo los aplica cuando los necesita.
// google.services = plugin de Firebase (versión 4.4.2 en libs.versions.toml)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
}
