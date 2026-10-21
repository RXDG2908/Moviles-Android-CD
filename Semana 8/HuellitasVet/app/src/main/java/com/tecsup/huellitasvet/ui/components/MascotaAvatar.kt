package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie

// Carita de perro, gato u otro animal (conejo) dibujada con Canvas.
// Cada parte es un trazo SVG en un lienzo de 100 x 100 (como un viewBox) y se escala al tamaño pedido.
// El círculo de fondo usa el color del contenedor del rol (verde o azul claro).
@Composable
fun MascotaAvatar(especie: Especie, modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    val fondo = MaterialTheme.colorScheme.primaryContainer
    val partes = dibujos.getValue(especie)
    Canvas(modifier = modifier.size(tamano)) {
        drawCircle(fondo)
        // La cara se agranda un poco (ZOOM) para que se lea bien en tamaños chicos,
        // y se recorta al círculo para que nada se salga del borde
        val k = size.minDimension / 100f * ZOOM
        val borde = Path().apply { addOval(Rect(Offset.Zero, size)) }
        withTransform({
            clipPath(borde)
            translate(size.width / 2 - 50f * k, size.height / 2 - 52f * k)
            scale(k, k, pivot = Offset.Zero)
        }) {
            partes.forEach { (path, p) ->
                val estilo = if (p.grosor > 0f) {
                    Stroke(width = p.grosor, cap = StrokeCap.Round, join = StrokeJoin.Round)
                } else Fill
                drawPath(path, p.color, alpha = p.alpha, style = estilo)
            }
        }
    }
}

private const val ZOOM = 1.12f

// Una parte del dibujo: trazo SVG, color, transparencia y grosor (0 = relleno)
private class Parte(val d: String, val color: Color, val alpha: Float = 1f, val grosor: Float = 0f)

// Círculo como trazo SVG (dos arcos)
private fun circulo(cx: Float, cy: Float, r: Float) =
    "M${cx - r} $cy A$r $r 0 1 0 ${cx + r} $cy A$r $r 0 1 0 ${cx - r} $cy Z"

// Colores comunes
private val OJO = Color(0xFF2B2B2B)
private val BRILLO = Color.White
private val MEJILLA = Color(0xFFF29C9C)
private val ROSADO = Color(0xFFF07C8D)
private val OREJA_INTERNA = Color(0xFFF5B8C4)

private val perro = listOf(
    // orejas caídas
    Parte("M27 30 C15 31 9 48 12 61 C14 70 23 71 27 63 L33 41 Z", Color(0xFF8A4B22)),
    Parte("M73 30 C85 31 91 48 88 61 C86 70 77 71 73 63 L67 41 Z", Color(0xFF8A4B22)),
    // cabeza, mancha de la frente y hocico
    Parte("M50 26 C66 26 76 37 76 52 C76 68 65 79 50 79 C35 79 24 68 24 52 C24 37 34 26 50 26 Z", Color(0xFFE3A869)),
    Parte("M50 28 C46 28 44 33 45 40 C46 46 48 49 50 49 C52 49 54 46 55 40 C56 33 54 28 50 28 Z", Color(0xFFF6DDBF)),
    Parte("M50 52 C59 52 65 57 65 64 C65 71 58 75 50 75 C42 75 35 71 35 64 C35 57 41 52 50 52 Z", Color(0xFFF6DDBF)),
    Parte(circulo(33f, 61f, 3.6f), MEJILLA, alpha = 0.55f),
    Parte(circulo(67f, 61f, 3.6f), MEJILLA, alpha = 0.55f),
    // ojos con brillo
    Parte(circulo(40f, 49f, 4f), OJO), Parte(circulo(41.4f, 47.6f, 1.4f), BRILLO),
    Parte(circulo(60f, 49f, 4f), OJO), Parte(circulo(61.4f, 47.6f, 1.4f), BRILLO),
    // nariz, lengua y boca
    Parte("M44.5 57.5 C44.5 55 55.5 55 55.5 57.5 C55.5 60.5 52.5 62.5 50 62.5 C47.5 62.5 44.5 60.5 44.5 57.5 Z", OJO),
    Parte("M47.5 67.5 C47.5 72.5 52.5 72.5 52.5 67.5 Z", ROSADO),
    Parte("M50 62.5 L50 66 M50 66 C48 68.5 45 68.5 43.5 66.5 M50 66 C52 68.5 55 68.5 56.5 66.5", OJO, grosor = 1.8f)
)

private val gato = listOf(
    // orejas en punta con el interior rosado
    Parte("M27 44 L28 18 C28 15 31 14 33 16 L51 33 Z", Color(0xFF8F98A4)),
    Parte("M73 44 L72 18 C72 15 69 14 67 16 L49 33 Z", Color(0xFF8F98A4)),
    Parte("M31 37 L32 23 L43 32 Z", OREJA_INTERNA),
    Parte("M69 37 L68 23 L57 32 Z", OREJA_INTERNA),
    // cabeza con rayas en la frente y hocico claro
    Parte("M50 30 C67 30 78 40 78 55 C78 70 66 78 50 78 C34 78 22 70 22 55 C22 40 33 30 50 30 Z", Color(0xFF9AA3AE)),
    Parte("M50 32 L50 40 M43 33.5 L44.5 39 M57 33.5 L55.5 39", Color(0xFF727C88), grosor = 2.8f),
    Parte(circulo(44.5f, 63f, 7f), Color(0xFFE9ECEF)),
    Parte(circulo(55.5f, 63f, 7f), Color(0xFFE9ECEF)),
    Parte(circulo(32f, 60f, 3.4f), MEJILLA, alpha = 0.5f),
    Parte(circulo(68f, 60f, 3.4f), MEJILLA, alpha = 0.5f),
    // ojos almendrados con brillo
    Parte("M36 50 C36 45 45 45 45 50 C45 54 36 54 36 50 Z", OJO), Parte(circulo(42.2f, 48.6f, 1.3f), BRILLO),
    Parte("M55 50 C55 45 64 45 64 50 C64 54 55 54 55 50 Z", OJO), Parte(circulo(61.2f, 48.6f, 1.3f), BRILLO),
    // nariz, boca y bigotes
    Parte("M46.5 57.5 L53.5 57.5 L50 61 Z", ROSADO),
    Parte("M46.5 57.5 L53.5 57.5 L50 61 Z", ROSADO, grosor = 1.5f),
    Parte("M50 61 C49 64.5 45.5 65 44 63 M50 61 C51 64.5 54.5 65 56 63", OJO, grosor = 1.6f),
    Parte("M37 61 L25 59 M37 64.5 L25 66.5 M63 61 L75 59 M63 64.5 L75 66.5", Color(0xFF5F6873), grosor = 1.2f)
)

private val conejo = listOf(
    // orejas largas con el interior rosado
    Parte("M40 40 C33 26 34 11 40 11 C46 11 48 26 46 40 Z", Color(0xFFD9CFC7)),
    Parte("M60 40 C67 26 66 11 60 11 C54 11 52 26 54 40 Z", Color(0xFFD9CFC7)),
    Parte("M40.5 34 C37 24 37.5 16 40.5 16 C43.5 16 44 24 43.5 34 Z", OREJA_INTERNA),
    Parte("M59.5 34 C63 24 62.5 16 59.5 16 C56.5 16 56 24 56.5 34 Z", OREJA_INTERNA),
    // cabeza y hocico blanco
    Parte("M50 35 C65 35 74 45 74 58 C74 71 63 79 50 79 C37 79 26 71 26 58 C26 45 35 35 50 35 Z", Color(0xFFE6DED7)),
    Parte(circulo(45f, 66f, 6.5f), Color.White),
    Parte(circulo(55f, 66f, 6.5f), Color.White),
    Parte(circulo(34f, 64f, 3.4f), MEJILLA, alpha = 0.55f),
    Parte(circulo(66f, 64f, 3.4f), MEJILLA, alpha = 0.55f),
    // ojos con brillo
    Parte(circulo(41f, 55f, 3.8f), OJO), Parte(circulo(42.3f, 53.7f, 1.3f), BRILLO),
    Parte(circulo(59f, 55f, 3.8f), OJO), Parte(circulo(60.3f, 53.7f, 1.3f), BRILLO),
    // nariz, boca y dientes
    Parte("M47 61.5 C47 60 53 60 53 61.5 C53 63 51 64.5 50 64.5 C49 64.5 47 63 47 61.5 Z", ROSADO),
    Parte("M50 64.5 L50 67 M50 67 C48.5 69 46 69 45 67.5 M50 67 C51.5 69 54 69 55 67.5", OJO, grosor = 1.5f),
    Parte("M47.8 69.6 Q47.8 68.6 48.8 68.6 L51.2 68.6 Q52.2 68.6 52.2 69.6 L52.2 73.2 L47.8 73.2 Z", Color.White),
    Parte("M47.8 69.6 Q47.8 68.6 48.8 68.6 L51.2 68.6 Q52.2 68.6 52.2 69.6 L52.2 73.2 L47.8 73.2 Z", Color(0xFFD9CFC7), grosor = 0.8f)
)

// Los trazos se convierten a Path una sola vez y se reutilizan en cada dibujo
private val dibujos: Map<Especie, List<Pair<Path, Parte>>> by lazy {
    mapOf(Especie.PERRO to perro, Especie.GATO to gato, Especie.OTRO to conejo)
        .mapValues { (_, partes) -> partes.map { PathParser().parsePathString(it.d).toPath() to it } }
}
