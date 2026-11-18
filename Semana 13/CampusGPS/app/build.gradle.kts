// Configuracion de Gradle del modulo app: plugins, SDK y dependencias.
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// TODO: lee local.properties (si existe) y úsalo en defaultConfig
val localProps = Properties().apply {
    // TODO: cargar rootProject.file("local.properties")
}

android {
    namespace = "com.tecsup.campusgps"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tecsup.campusgps"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // TODO: manifestPlaceholders["MAPS_API_KEY"] = ...
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Ubicación y mapas de Google
    // maps-compose 8.4.0 (la de la guía) exige AGP 9.1 y compileSdk 37; 6.4.1 funciona con este proyecto
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    implementation("com.google.maps.android:maps-compose:6.4.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
