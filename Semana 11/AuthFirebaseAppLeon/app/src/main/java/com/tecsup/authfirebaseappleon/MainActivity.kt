package com.tecsup.authfirebaseappleon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.google.firebase.FirebaseApp

/**
 * MainActivity: punto de entrada de la app, la primera pantalla.
 * Inicializa Firebase y carga la navegación (AuthApp) dentro del tema Material 3.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme(
                // Paleta de colores: morado (botones) y celeste de Tecsup
                colorScheme = lightColorScheme(
                    primary = Color(0xFF6650A4),
                    secondary = Color(0xFF00B3E6),
                    background = Color.White,
                    surface = Color.White,
                    surfaceVariant = Color(0xFFF1EDF8)
                )
            ) {
                AuthApp() // Llamamos a nuestra app con navegación
            }
        }
    }
}
