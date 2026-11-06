package com.tecsup.authfirebaseappleon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme(
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
