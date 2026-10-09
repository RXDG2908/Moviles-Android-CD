plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.saludplus.citas"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.saludplus.citas"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // java.time (LocalDate) existe desde Android 8 (API 26); con desugaring
        // también funciona en los celulares con minSdk 24 y 25
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Permite usar java.time (LocalDate) en Android 7 (minSdk 24)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    // Navegación entre pantallas
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Íconos (flecha de volver, pestañas, menú)
    implementation("androidx.compose.material:material-icons-core")
    // Íconos extra (canasta del producto y flecha de compartir)
    implementation("androidx.compose.material:material-icons-extended")

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}