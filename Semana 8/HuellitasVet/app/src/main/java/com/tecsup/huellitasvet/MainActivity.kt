package com.tecsup.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.navigation.NavVet
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

// [E]
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as VetApp
        val factory = VetViewModelFactory(app.repository)
        setContent {
            // TODO (Paso 13): rol y modo oscuro leídos de PreferenciasStore
            VetTheme(rol = Rol.ADMIN, oscuro = false) {
                NavVet(factory)
            }
        }
    }
}
