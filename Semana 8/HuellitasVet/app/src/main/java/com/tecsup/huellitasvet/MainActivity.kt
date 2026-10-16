package com.tecsup.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.navigation.NavVet
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as VetApp
        val factory = VetViewModelFactory(app.repository, app.preferencias)
        setContent {
            // La sesión se comparte con toda la app
            val sesion: SesionViewModel = viewModel(factory = factory)
            val usuario by sesion.usuarioActual.collectAsState()
            val oscuro by sesion.modoOscuro.collectAsState()
            val rol = usuario?.let { Rol.valueOf(it.rol) } ?: Rol.CLIENTE

            VetTheme(rol = rol, oscuro = oscuro) {
                NavVet(factory, sesion)
            }
        }
    }
}
