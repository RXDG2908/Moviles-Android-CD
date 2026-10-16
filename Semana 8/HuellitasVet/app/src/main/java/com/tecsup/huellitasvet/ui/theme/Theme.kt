package com.tecsup.huellitasvet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.Rol

// VetTheme(rol) entrega un ColorScheme según el perfil.
// En las pantallas usa MaterialTheme.colorScheme.primary, nunca un color fijo.
@Composable
fun VetTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val primario = if (rol == Rol.ADMIN) VetPrimario else DuenoPrimario
    val contenedor = if (rol == Rol.ADMIN) VetContenedor else DuenoContenedor
    val fondo = if (rol == Rol.ADMIN) VetFondo else DuenoFondo

    val esquema = if (oscuro) {
        darkColorScheme(
            primary = primario,
            onPrimary = Color.White,
            primaryContainer = primario.copy(alpha = 0.35f),
            onPrimaryContainer = Color.White,
            secondary = primario,
            secondaryContainer = primario.copy(alpha = 0.35f),
            onSecondaryContainer = Color.White,
            background = FondoOscuro,
            surface = SuperficieOscura,
            surfaceContainer = SuperficieOscura,
            onBackground = Color(0xFFE2E8F0),
            onSurface = Color(0xFFE2E8F0)
        )
    } else {
        lightColorScheme(
            primary = primario,
            onPrimary = Color.White,
            primaryContainer = contenedor,
            onPrimaryContainer = primario,
            secondary = primario,
            secondaryContainer = contenedor,
            onSecondaryContainer = primario,
            background = fondo,
            surface = Color.White,
            surfaceContainer = Color.White,
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF0F172A)
        )
    }

    MaterialTheme(colorScheme = esquema, typography = VetTypography, content = content)
}
