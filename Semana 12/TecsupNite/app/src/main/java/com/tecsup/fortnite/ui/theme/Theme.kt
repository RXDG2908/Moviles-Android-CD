package com.tecsup.fortnite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.fortnite.R

val Cielo = Color(0xFF35AEF6)
val CieloFuerte = Color(0xFF0E7BE0)
val Noche = Color(0xFF0A2A66)
val NocheClara = Color(0xFF123E91)
val Oro = Color(0xFFFFC61A)
val Blanco = Color.White
val CelesteTexto = Color(0xFFD6EEFF)
val Rojo = Color(0xFFD91E2E)

val Burbank = FontFamily(
    Font(R.font.burbank_black, FontWeight.Black),
    Font(R.font.burbank_bold, FontWeight.Bold)
)

private val Colores = darkColorScheme(
    primary = Oro,
    onPrimary = Noche,
    primaryContainer = Oro,
    onPrimaryContainer = Noche,
    secondary = Cielo,
    onSecondary = Noche,
    tertiary = Oro,
    onTertiary = Noche,
    background = Cielo,
    onBackground = Blanco,
    surface = Noche,
    onSurface = Blanco,
    surfaceVariant = NocheClara,
    onSurfaceVariant = CelesteTexto,
    surfaceContainer = Noche,
    error = Rojo,
    onError = Blanco,
    outline = Blanco
)

// Títulos inclinados y con sombra, como los letreros del juego
private val inclinado = TextGeometricTransform(skewX = -0.14f)
private val sombra = Shadow(Noche.copy(alpha = 0.55f), Offset(0f, 5f), 6f)

private fun titulo(tamano: Int) = TextStyle(
    fontFamily = Burbank,
    fontWeight = FontWeight.Black,
    fontSize = tamano.sp,
    lineHeight = (tamano + 4).sp,
    textGeometricTransform = inclinado
)

private fun negra(tamano: Int) = TextStyle(
    fontFamily = Burbank,
    fontWeight = FontWeight.Black,
    fontSize = tamano.sp,
    lineHeight = (tamano + 4).sp,
    letterSpacing = 0.3.sp
)

private fun lectura(tamano: Int) = TextStyle(
    fontFamily = Burbank,
    fontWeight = FontWeight.Bold,
    fontSize = tamano.sp,
    lineHeight = (tamano + 6).sp,
    letterSpacing = 0.4.sp
)

private val Tipos = Typography(
    displayLarge = titulo(76).copy(shadow = sombra),
    displayMedium = titulo(60).copy(shadow = sombra),
    displaySmall = titulo(46).copy(shadow = sombra),
    headlineLarge = titulo(40).copy(shadow = sombra),
    headlineMedium = titulo(36),
    headlineSmall = titulo(30),
    titleLarge = titulo(30),
    titleMedium = negra(24),
    titleSmall = negra(21),
    bodyLarge = lectura(20),
    bodyMedium = lectura(18),
    bodySmall = lectura(16),
    labelLarge = negra(20),
    labelMedium = lectura(16),
    labelSmall = lectura(14)
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(2.dp),
    large = RoundedCornerShape(2.dp),
    extraLarge = RoundedCornerShape(2.dp)
)

@Composable
fun TecsupNiteTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colores, typography = Tipos, shapes = Formas, content = content)
}

// Color de la tarjeta según la rareza que manda la API.
// Épico va en rojo rosado en vez del morado del juego, por decisión de diseño de la app.
fun colorRareza(clave: String): Color = when (clave) {
    "uncommon" -> Color(0xFF3FAE2A)
    "rare" -> Color(0xFF1E8FE8)
    "epic" -> Color(0xFFC4265E)
    "legendary" -> Color(0xFFF58A07)
    "icon" -> Color(0xFF00B5AD)
    "marvel" -> Color(0xFFD91E2E)
    "starwars" -> Color(0xFF1B1F2B)
    "gaminglegends" -> Color(0xFF2456D6)
    "dc" -> Color(0xFF1F6FD1)
    "shadow" -> Color(0xFF2B3140)
    "lava" -> Color(0xFFE2501B)
    "frozen" -> Color(0xFF3DBCE8)
    else -> Color(0xFF5B8FC7)
}
