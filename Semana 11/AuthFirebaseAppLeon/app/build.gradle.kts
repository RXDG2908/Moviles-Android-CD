// ===== build.gradle (nivel Módulo) =====
// Plugins que se aplican a este módulo:
//  - android.application : es una app Android
//  - kotlin.compose      : habilita Jetpack Compose
//  - google.services     : lee google-services.json y conecta con Firebase
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

// Configuración general de Android
android {
    namespace = "com.tecsup.authfirebaseappleon"
    compileSdk {
        version = release(37)
    }

    // Datos de la app: el applicationId debe coincidir con el package name registrado en Firebase
    defaultConfig {
        applicationId = "com.tecsup.authfirebaseappleon"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Tipos de compilación (release sin minificar, como en la guía)
    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    // Versión de Java usada para compilar
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // Activa Jetpack Compose
    buildFeatures {
        compose = true
    }
}

// Librerías que usa la app
dependencies {
    // Firebase: BoM + Authentication + Firestore
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Navigation Compose (NavHost, NavController, composable, etc.)
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Íconos de Material (ícono "+" del botón Nuevo curso)
    implementation("androidx.compose.material:material-icons-core")

    // Jetpack Compose y AndroidX (versiones tomadas del catálogo libs.versions.toml)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
