package com.tecsup.fortnite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Fondo = Color(0xFF0B0B1A)
val Superficie = Color(0xFF14142B)
val SuperficieAlta = Color(0xFF1F1F3D)
val Morado = Color(0xFFB69CFF)
val Celeste = Color(0xFF4DD8FF)
val Dorado = Color(0xFFFFD54A)

private val Colores = darkColorScheme(
    primary = Morado,
    onPrimary = Color(0xFF1B0A4A),
    primaryContainer = Color(0xFF3B2A7A),
    onPrimaryContainer = Color(0xFFE6DEFF),
    secondary = Celeste,
    onSecondary = Color(0xFF00313D),
    tertiary = Dorado,
    onTertiary = Color(0xFF3A2D00),
    background = Fondo,
    onBackground = Color(0xFFE8E6F5),
    surface = Superficie,
    onSurface = Color(0xFFE8E6F5),
    surfaceVariant = SuperficieAlta,
    onSurfaceVariant = Color(0xFFB9B6D3),
    error = Color(0xFFFF6B7A),
    outline = Color(0xFF5A5780)
)

@Composable
fun TecsupNiteTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colores, content = content)
}

// Color de la tarjeta según la rareza que manda la API
fun colorRareza(clave: String): Color = when (clave) {
    "uncommon" -> Color(0xFF5CB85C)
    "rare" -> Color(0xFF3FA9F5)
    "epic" -> Color(0xFFB45CFF)
    "legendary" -> Color(0xFFFF9F2E)
    "icon" -> Color(0xFF2DE0D0)
    "marvel" -> Color(0xFFE03A3E)
    "starwars" -> Color(0xFFF2C94C)
    "gaminglegends" -> Color(0xFF7B61FF)
    "shadow" -> Color(0xFF6B6B7B)
    else -> Color(0xFF8A8AA3)
}
