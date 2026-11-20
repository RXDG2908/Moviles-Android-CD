// Mapa de Google con la posición del usuario y los lugares del campus.
package com.leon.campusgps.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.Polyline
import com.leon.campusgps.util.formatearDistancia
import kotlinx.coroutines.delay
import kotlin.math.cos
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.leon.campusgps.model.Lugar
import com.leon.campusgps.model.LugarConDistancia
import com.leon.campusgps.model.RutaUi
import com.leon.campusgps.model.UbicacionUi

private val ColorEnVivo = Color(0xFFE53935)
private val ColorRadio = Color(0xFF1E88E5)
internal val ColorRuta = Color(0xFF1565C0)
private val ColorRutaCercano = Color(0xFF2E7D32)
// Patrón del croquis: guiones cortos separados por huecos (en píxeles)
private val PatronRuta = listOf(Dash(28f), Gap(18f))

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radio: Int?,
    siguiendo: Boolean,
    pantallaCompleta: Boolean,
    onPantallaCompleta: () -> Unit,
    onSeleccionar: (Int?) -> Unit,
    ruta: RutaUi,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }
    // Para saber si la cámara ya se acercó a la posición al menos una vez
    val yaCentrada = remember { mutableStateOf(false) }

    // Cuando llega la ubicación, la cámara viaja hasta ella
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val punto = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            if (siguiendo && yaCentrada.value) {
                // En vivo solo se desplaza y respeta el zoom que haya elegido el usuario
                camara.animate(CameraUpdateFactory.newLatLng(punto), 700)
            } else {
                camara.animate(CameraUpdateFactory.newLatLngZoom(punto, 16f), 900)
                yaCentrada.value = true
            }
        }
    }

    // ---- Ruta a pie hasta el lugar elegido ----
    // Avance del trazo: 0 = sin dibujar, 1 = llega al destino
    val trazo = remember { Animatable(0f) }
    val margenRutaPx = with(LocalDensity.current) { 60.dp.roundToPx() }

    val lugarRuta = lugares.firstOrNull { it.lugar.id == seleccionado }
    val origenRuta = (estado as? UbicacionUi.Lista)?.let { LatLng(it.posicion.latitud, it.posicion.longitud) }
    val destinoRuta = lugarRuta?.let { LatLng(it.lugar.latitud, it.lugar.longitud) }
    // Camino completo a dibujar: tu posición, las calles que da Google y el lugar.
    // Si el servicio falló se une en línea recta; mientras carga no se dibuja nada.
    val camino: List<LatLng> = when {
        origenRuta == null || destinoRuta == null -> emptyList()
        ruta is RutaUi.Lista -> listOf(origenRuta) +
            ruta.ruta.puntos.map { LatLng(it.latitud, it.longitud) } + destinoRuta
        ruta is RutaUi.Recta -> listOf(origenRuta, destinoRuta)
        else -> emptyList()
    }
    val hayRuta = ruta !is RutaUi.Ninguna && origenRuta != null && lugarRuta != null

    // Encuadra en la cámara todos los puntos del camino
    suspend fun encuadrar(puntos: List<LatLng>, ms: Int) {
        if (puntos.size < 2) return
        val limites = LatLngBounds.builder().apply { puntos.forEach { include(it) } }.build()
        runCatching { camara.animate(CameraUpdateFactory.newLatLngBounds(limites, margenRutaPx), ms) }
    }

    // Sin ubicación no hay ruta: al elegir un lugar la cámara solo viaja hasta él
    LaunchedEffect(seleccionado) {
        if (estado is UbicacionUi.Lista) return@LaunchedEffect
        val destino = destinoRuta ?: return@LaunchedEffect
        camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 17f), 800)
    }

    // Cuando llega una ruta nueva, la cámara la encuadra y el punteado se dibuja
    // calle por calle desde ti hasta el lugar. Al recalcular en vivo no se repite.
    val claveTrazo = when (ruta) {
        is RutaUi.Lista -> "calles-${ruta.destinoId}"
        is RutaUi.Recta -> "recta-${ruta.destinoId}"
        else -> null
    }
    LaunchedEffect(claveTrazo) {
        trazo.snapTo(0f)
        if (claveTrazo == null) return@LaunchedEffect
        encuadrar(camino, 800)
        trazo.animateTo(1f, tween(1500, easing = FastOutSlowInEasing))
    }

    // Al entrar o salir de pantalla completa, se vuelve a encuadrar la ruta
    // cuando el mapa termina de cambiar de tamaño
    LaunchedEffect(pantallaCompleta) {
        if (camino.size < 2) return@LaunchedEffect
        delay(450)
        encuadrar(camino, 600)
    }

    // ---- Animación del radio ----
    // El círculo crece desde 0 con un rebote al elegir un radio, pasa con rebote
    // de un tamaño a otro y se encoge hasta desaparecer al quitarlo.
    val radioAnimado = remember { Animatable(0f) }
    var ultimoRadio by remember { mutableStateOf(radio) }
    LaunchedEffect(radio) {
        if (radio != null) {
            ultimoRadio = radio
            radioAnimado.animateTo(
                radio.toFloat(),
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessVeryLow)
            )
        } else {
            radioAnimado.animateTo(0f, tween(350, easing = FastOutSlowInEasing))
        }
    }

    // Al elegir un radio, la cámara se aleja o acerca para que el círculo quepa entero
    val margenPx = with(LocalDensity.current) { 40.dp.roundToPx() }
    LaunchedEffect(radio) {
        val r = radio ?: return@LaunchedEffect
        val lista = estado as? UbicacionUi.Lista ?: return@LaunchedEffect
        val lat = lista.posicion.latitud
        val lng = lista.posicion.longitud
        val dLat = r / 111_320.0
        val dLng = r / (111_320.0 * cos(Math.toRadians(lat)))
        val limites = LatLngBounds(LatLng(lat - dLat, lng - dLng), LatLng(lat + dLat, lng + dLng))
        runCatching { camara.animate(CameraUpdateFactory.newLatLngBounds(limites, margenPx), 900) }
    }

    // Onda tipo radar que sale del centro hasta el borde del radio, una y otra vez
    val ondas = rememberInfiniteTransition(label = "radar")
    val progresoOnda by ondas.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart),
        label = "onda"
    )
    // El relleno "respira" suavemente mientras el radio está activo
    val respiro by ondas.animateFloat(
        initialValue = 0.14f,
        targetValue = 0.26f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "respiro"
    )

    // En pantalla completa el mapa pierde las esquinas redondeadas y la sombra
    val esquinas by animateDpAsState(if (pantallaCompleta) 0.dp else 20.dp, label = "esquinas")
    val sombra by animateDpAsState(if (pantallaCompleta) 0.dp else 6.dp, label = "sombra")

    val forma = RoundedCornerShape(esquinas)
    Box(
        modifier = modifier
            .shadow(sombra, forma)
            .clip(forma)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camara,
            // Sin la barra de Google Maps que aparece al tocar un marcador
            uiSettings = MapUiSettings(mapToolbarEnabled = false, compassEnabled = true),
            // Con ruta, el logo de Google y el zoom suben para dejar sitio a la etiqueta
            contentPadding = PaddingValues(bottom = if (hayRuta) 56.dp else 0.dp),
            // Tocar una zona vacía del mapa borra la ruta
            onMapClick = { onSeleccionar(null) }
        ) {
            // Ruta punteada desde tu posición hasta el lugar elegido, por las calles
            if (camino.size >= 2 && lugarRuta != null && destinoRuta != null && trazo.value > 0f) {
                val destino = destinoRuta
                // Parte del camino ya dibujada (avanza con la animación)
                val visible = recortarCamino(camino, trazo.value)
                val color = if (lugarRuta.lugar.id == masCercano?.id) ColorRutaCercano else ColorRuta
                // Borde blanco debajo para que el punteado resalte sobre cualquier calle
                Polyline(
                    points = visible,
                    color = Color.White,
                    width = 20f,
                    pattern = PatronRuta,
                    startCap = RoundCap(),
                    endCap = RoundCap(),
                    jointType = JointType.ROUND
                )
                Polyline(
                    points = visible,
                    color = color,
                    width = 11f,
                    pattern = PatronRuta,
                    startCap = RoundCap(),
                    endCap = RoundCap(),
                    jointType = JointType.ROUND,
                    zIndex = 1f
                )
                // Al llegar, el destino late con un pulso
                if (trazo.value >= 1f) {
                    Circle(
                        center = destino,
                        radius = 6.0 + 22.0 * progresoOnda,
                        fillColor = color.copy(alpha = 0.25f * (1f - progresoOnda)),
                        strokeColor = color.copy(alpha = 0.9f * (1f - progresoOnda)),
                        strokeWidth = 4f
                    )
                }
            }

            if (estado is UbicacionUi.Lista) {
                val marcador = rememberMarkerState()
                marcador.position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                // Tu posición: persona en un círculo rojo con la etiqueta "Tú", siempre por encima
                MarkerComposable(
                    "tu",
                    state = marcador,
                    title = "Estás aquí",
                    anchor = AnclaTu,
                    zIndex = 3f
                ) {
                    MarcadorTu()
                }
                // Círculo del radio de cercanía alrededor de la posición (animado)
                val r = radioAnimado.value
                if (r > 1f && ultimoRadio != null) {
                    Circle(
                        center = marcador.position,
                        radius = r.toDouble(),
                        fillColor = ColorRadio.copy(alpha = if (radio != null) respiro else 0.14f),
                        strokeColor = ColorRadio,
                        strokeWidth = 5f
                    )
                    // Onda de radar: crece desde el centro y se desvanece al llegar al borde
                    if (radio != null) {
                        Circle(
                            center = marcador.position,
                            radius = (r * progresoOnda).toDouble(),
                            fillColor = ColorRadio.copy(alpha = 0.10f * (1f - progresoOnda)),
                            strokeColor = ColorRadio.copy(alpha = 0.8f * (1f - progresoOnda)),
                            strokeWidth = 3f
                        )
                    }
                }
            }
            // Un marcador por cada lugar: verde el más cercano, azul los demás
            lugares.forEach { item ->
                key(item.lugar.id) {
                    val estadoMarcador = rememberMarkerState(
                        position = LatLng(item.lugar.latitud, item.lugar.longitud)
                    )
                    // Sin ubicación el lugar elegido muestra su nombre en el mapa;
                    // con ruta ya lo dice la etiqueta de abajo y el globo taparía el trazo
                    LaunchedEffect(seleccionado, hayRuta) {
                        if (item.lugar.id == seleccionado && !hayRuta) {
                            estadoMarcador.showInfoWindow()
                        } else {
                            estadoMarcador.hideInfoWindow()
                        }
                    }
                    // Pin verde el más cercano, azul los demás, con el icono de su categoría
                    val colorPin = if (item.lugar.id == masCercano?.id) ColorPinCercano else ColorPin
                    val elegido = item.lugar.id == seleccionado
                    MarkerComposable(
                        item.lugar.id, colorPin, elegido,
                        state = estadoMarcador,
                        title = item.lugar.nombre,
                        snippet = item.metros?.let { "${item.lugar.categoria} · ${formatearDistancia(it)}" }
                            ?: item.lugar.categoria,
                        // El elegido queda encima de los demás
                        zIndex = if (elegido) 2f else 1f,
                        // Tocar el marcador también traza la ruta hasta ese lugar
                        onClick = {
                            onSeleccionar(item.lugar.id)
                            true
                        }
                    ) {
                        PinLugar(item.lugar.categoria, colorPin, elegido)
                    }
                }
            }
        }

        // Etiqueta flotante mientras el seguimiento en vivo está activo
        AnimatedVisibility(
            visible = siguiendo,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(start = 6.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PuntoEnVivo(ColorEnVivo)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "En vivo",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = ColorEnVivo
                    )
                }
            }
        }

        // Panel con el destino, la distancia, los minutos y los pasos giro a giro
        AnimatedVisibility(
            visible = hayRuta,
            enter = fadeIn() + scaleIn(initialScale = 0.85f),
            exit = fadeOut() + scaleOut(targetScale = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
        ) {
            // Se recuerda lo último mostrado para que el panel no quede vacío al salir
            val ultimo = remember { mutableStateOf<Pair<LugarConDistancia, RutaUi>?>(null) }
            if (hayRuta && lugarRuta != null) ultimo.value = lugarRuta to ruta
            ultimo.value?.let { (lugar, r) -> PanelRuta(lugar, r, onCerrar = { onSeleccionar(null) }) }
        }

        // Botón de pantalla completa en la esquina superior derecha
        BotonPantallaCompleta(
            activa = pantallaCompleta,
            onClick = onPantallaCompleta,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        )
    }
}

// Devuelve el tramo del camino que corresponde a una fracción t (0..1) de su largo,
// para que el punteado avance calle por calle durante la animación
private fun recortarCamino(camino: List<LatLng>, t: Float): List<LatLng> {
    if (t >= 1f || camino.size < 2) return camino
    // Largo aproximado de cada tramo (basta para animar distancias cortas)
    val cosLat = cos(Math.toRadians(camino.first().latitude))
    fun largo(a: LatLng, b: LatLng): Double {
        val dx = (b.longitude - a.longitude) * cosLat
        val dy = b.latitude - a.latitude
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
    val tramos = camino.zipWithNext { a, b -> largo(a, b) }
    var restante = tramos.sum() * t
    val resultado = mutableListOf(camino.first())
    for (i in tramos.indices) {
        val a = camino[i]
        val b = camino[i + 1]
        if (restante >= tramos[i]) {
            resultado.add(b)
            restante -= tramos[i]
        } else {
            // La punta queda a mitad de este tramo
            val f = if (tramos[i] == 0.0) 0.0 else restante / tramos[i]
            resultado.add(LatLng(a.latitude + (b.latitude - a.latitude) * f, a.longitude + (b.longitude - a.longitude) * f))
            break
        }
    }
    return resultado
}

@Composable
private fun BotonPantallaCompleta(activa: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // Pequeño "pop" cada vez que cambia el modo
    val escala = remember { Animatable(1f) }
    LaunchedEffect(activa) {
        escala.snapTo(0.8f)
        escala.animateTo(1f, spring(dampingRatio = Spring.DampingRatioHighBouncy))
    }
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        contentColor = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
        modifier = modifier
            .size(44.dp)
            .scale(escala.value)
    ) {
        Box(contentAlignment = Alignment.Center) {
            // El icono cambia girando y con fundido entre expandir y contraer
            AnimatedContent(
                targetState = activa,
                transitionSpec = {
                    (scaleIn(initialScale = 0.5f) + fadeIn()) togetherWith
                        (scaleOut(targetScale = 0.5f) + fadeOut())
                },
                label = "iconoPantalla"
            ) { completa ->
                Icon(
                    if (completa) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
                    contentDescription = if (completa) "Salir de pantalla completa" else "Mapa en pantalla completa",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
