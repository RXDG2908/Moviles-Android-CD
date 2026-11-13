// Colores, tipografia y tema de la app.
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.fortnite.R

// Tokens de regalitos.shop (WEB.md): azul tienda claro, nada de azul marino
val CieloClaro = Color(0xFF1A7FF0)   // --azul-2
val Cielo = Color(0xFF0A63D8)        // --azul
val CieloFuerte = Color(0xFF0752BE)
val Noche = Color(0xFF0839AD)        // --panel rgba(5,22,138,.55) sobre el azul
val NocheClara = Color(0xFF0084E6)   // --chip
val Borde = Color(0xFF00C3FE)        // --chip-borde
val Oro = Color(0xFFFDFF00)          // --am, acento de acción único
val TintaAm = Color(0xFF06156B)      // texto sobre amarillo
val Blanco = Color.White
val CelesteTexto = Color(0xFFDCF3FF) // --soft
val Lima = Color(0xFFB6F400)         // --ok, placa "Termina hoy"
val Rojo = Color(0xFFF5327D)         // --rosa, solo avisos
val SombraPanel = Color(0x8C05168A)  // rgba(5,22,138,.55)
// Inter para todo el texto que no es título
@OptIn(ExperimentalTextApi::class)
private fun inter(peso: Int) = Font(
    R.font.inter,
    FontWeight(peso),
    variationSettings = FontVariation.Settings(FontVariation.weight(peso))
)

val Inter = FontFamily(inter(500), inter(600), inter(700), inter(800), inter(900))

val Burbank = FontFamily(
    Font(R.font.burbank_black, FontWeight.Black),
    Font(R.font.burbank_bold, FontWeight.Bold)
)

private val Colores = darkColorScheme(
    primary = Oro,
    onPrimary = TintaAm,
    primaryContainer = Oro,
    onPrimaryContainer = TintaAm,
    secondary = Cielo,
    onSecondary = Noche,
    tertiary = Oro,
    onTertiary = TintaAm,
    background = Cielo,
    outlineVariant = Borde,
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

// Títulos en Burbank con skew(-6deg), como .bb.sk de la web
private val inclinado = TextGeometricTransform(skewX = -0.105f)
private val sombra = Shadow(SombraPanel, Offset(0f, 4f), 0f)

private fun titulo(tamano: Int) = TextStyle(
    fontFamily = Burbank,
    fontWeight = FontWeight.Black,
    fontSize = tamano.sp,
    lineHeight = (tamano + 4).sp,
    textGeometricTransform = inclinado
)

// Texto normal en Inter
private fun texto(tamano: Int, peso: Int) = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight(peso),
    fontSize = tamano.sp,
    lineHeight = (tamano + 6).sp
)

// Precios en negrita itálica con números del mismo ancho, como .pr de la web
val EstiloPrecio = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight(900),
    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
    fontSize = 20.sp,
    lineHeight = 22.sp,
    fontFeatureSettings = "tnum"
)

private val Tipos = Typography(
    displayLarge = titulo(76).copy(shadow = sombra),
    displayMedium = titulo(60).copy(shadow = sombra),
    displaySmall = titulo(46).copy(shadow = sombra),
    headlineLarge = titulo(40).copy(shadow = sombra),
    headlineMedium = titulo(32),
    headlineSmall = titulo(26),
    titleLarge = titulo(26),
    titleMedium = texto(17, 800),
    titleSmall = texto(15, 700),
    bodyLarge = texto(16, 600),
    bodyMedium = texto(14, 500),
    bodySmall = texto(13, 500),
    labelLarge = texto(14, 800),
    labelMedium = texto(13, 600),
    labelSmall = texto(12, 700)
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),   // inputs
    medium = RoundedCornerShape(16.dp),  // tarjetas
    large = RoundedCornerShape(20.dp),   // paneles
    extraLarge = RoundedCornerShape(20.dp)
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
