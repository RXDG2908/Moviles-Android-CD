package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 8 - Iniciar sesión (faltante, obligatoria)
// TODO: diseñarla con el mismo estilo de Registro (teléfono y contraseña).
// TODO: Repositorio.iniciarSesion (búsqueda con find); si es correcto ir a HOME,
//  si no, mostrar un mensaje de error.
@Composable
fun LoginScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Iniciar sesión",
        botones = listOf(
            "Ingresar" to { navController.navigate(Rutas.HOME) }
        )
    )
}
