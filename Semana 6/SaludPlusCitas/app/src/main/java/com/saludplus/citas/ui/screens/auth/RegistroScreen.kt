package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 2 - Registro (diseño)
// TODO: estados para nombre completo, teléfono, correo (opcional) y contraseña (OutlinedTextField).
// TODO: validaciones (campos obligatorios llenos, teléfono de 9 dígitos, correo con @ si se escribe,
//  contraseña mínima).
// TODO: al registrar, llamar a Repositorio.registrarUsuario (add a la lista) e ir a LOGIN.
// TODO: enlace "Términos y Condiciones" -> TERMINOS y "¿Ya tienes cuenta? Iniciar sesión" -> LOGIN.
@Composable
fun RegistroScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Registro",
        botones = listOf(
            "Registrarme" to { navController.navigate(Rutas.LOGIN) },
            "Términos y condiciones" to { navController.navigate(Rutas.TERMINOS) }
        )
    )
}
