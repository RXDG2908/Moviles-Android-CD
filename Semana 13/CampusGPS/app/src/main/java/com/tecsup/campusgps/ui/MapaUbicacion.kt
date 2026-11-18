// Mapa de Google con la posición del usuario y los lugares del campus.
package com.tecsup.campusgps.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

private val ColorEnVivo = Color(0xFFE53935)

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radio: Int?,
    siguiendo: Boolean,
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

    // Al elegir un lugar de la lista, la cámara viaja hasta él
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            val punto = LatLng(lugar.latitud, lugar.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(punto, 17f), 800)
        }
    }

    val forma = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .shadow(6.dp, forma)
            .clip(forma)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camara,
            // Sin la barra de Google Maps que aparece al tocar un marcador
            uiSettings = MapUiSettings(mapToolbarEnabled = false, compassEnabled = true)
        ) {
            if (estado is UbicacionUi.Lista) {
                val marcador = rememberMarkerState()
                marcador.position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                Marker(state = marcador, title = "Estás aquí")
                // Círculo del radio de cercanía alrededor de la posición
                if (radio != null) {
                    Circle(
                        center = marcador.position,
                        radius = radio.toDouble(),
                        fillColor = Color(0x331E88E5),
                        strokeColor = Color(0xFF1E88E5),
                        strokeWidth = 4f
                    )
                }
            }
            // Un marcador por cada lugar: verde el más cercano, azul los demás
            lugares.forEach { item ->
                key(item.lugar.id) {
                    val estadoMarcador = rememberMarkerState(
                        position = LatLng(item.lugar.latitud, item.lugar.longitud)
                    )
                    // El lugar elegido muestra su nombre en el mapa
                    LaunchedEffect(seleccionado) {
                        if (item.lugar.id == seleccionado) estadoMarcador.showInfoWindow()
                    }
                    Marker(
                        state = estadoMarcador,
                        title = item.lugar.nombre,
                        snippet = item.lugar.categoria,
                        icon = BitmapDescriptorFactory.defaultMarker(
                            if (item.lugar.id == masCercano?.id) {
                                BitmapDescriptorFactory.HUE_GREEN
                            } else {
                                BitmapDescriptorFactory.HUE_AZURE
                            }
                        )
                    )
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
    }
}
