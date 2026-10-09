package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresSalud = lightColorScheme(
    primary = AzulSalud,
    onPrimary = Blanco,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulSalud,
    background = Blanco,
    onBackground = TextoOscuro,
    surface = Blanco,
    onSurface = TextoOscuro,
    surfaceVariant = GrisSuave,
    onSurfaceVariant = TextoGris
)

@Composable
fun SaludPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresSalud,
        typography = Typography,
        content = content
    )
}
