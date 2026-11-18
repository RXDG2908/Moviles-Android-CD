// Colores y tema Material 3 de la app.
package com.tecsup.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Azul = Color(0xFF1565C0)
private val AzulClaro = Color(0xFF90CAF9)
private val Verde = Color(0xFF2E7D32)

private val ColoresClaros = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    secondary = Verde,
    onSecondary = Color.White
)

private val ColoresOscuros = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Color(0xFF0D2A4F),
    secondary = Color(0xFFA5D6A7),
    onSecondary = Color(0xFF0B3A10)
)

@Composable
fun CampusGPSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) ColoresOscuros else ColoresClaros,
        content = content
    )
}
