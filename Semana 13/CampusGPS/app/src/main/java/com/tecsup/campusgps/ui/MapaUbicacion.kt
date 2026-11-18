// Mapa de Google con la posición del usuario y los lugares del campus.
package com.tecsup.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Cuando llega la ubicación, la cámara viaja hasta ella
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val punto = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(punto, 16f))
        }
    }

    // Al elegir un lugar de la lista, la cámara viaja hasta él
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            val punto = LatLng(lugar.latitud, lugar.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(punto, 17f))
        }
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        if (estado is UbicacionUi.Lista) {
            val marcador = rememberMarkerState()
            marcador.position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Marker(state = marcador, title = "Estás aquí")
        }
        // Un marcador azul por cada lugar; key(id) identifica a cada uno
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
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
    }
}
