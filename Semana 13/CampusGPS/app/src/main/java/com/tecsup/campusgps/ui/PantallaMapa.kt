// Pantalla principal: botón para buscar la ubicación y el resultado.
package com.tecsup.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel

private const val FINE = Manifest.permission.ACCESS_FINE_LOCATION
private const val COARSE = Manifest.permission.ACCESS_COARSE_LOCATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugares.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercano.collectAsStateWithLifecycle()
    // Solo se busca la distancia ya calculada para mostrarla, no se calcula aquí
    val metrosCercano = lugares.firstOrNull { it.lugar.id == masCercano?.id }?.metros

    val contexto = LocalContext.current
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        // Basta con que acepte uno de los dos (precisa o aproximada)
        if (resultado.values.any { it }) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        val tienePermiso = listOf(FINE, COARSE).any {
            ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED
        }
        if (tienePermiso) {
            vm.buscar()
        } else {
            lanzador.launch(arrayOf(FINE, COARSE))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { alPulsar() },
                enabled = estado !is UbicacionUi.Buscando
            ) {
                Text("Obtener mi ubicación")
            }

            when (val e = estado) {
                UbicacionUi.Inicial -> Text("Pulsa el botón para ver dónde estás")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> Text(
                    "Lat: ${e.posicion.latitud}  ·  Lon: ${e.posicion.longitud}"
                )
                is UbicacionUi.Error -> Text(
                    text = e.mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Lleva la cámara al lugar más cercano; deshabilitado sin ubicación
            OutlinedButton(
                onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                enabled = masCercano != null
            ) {
                Text("Ir al más cercano")
            }
            val cercano = masCercano
            if (cercano != null && metrosCercano != null) {
                Text("Más cercano: ${cercano.nombre} (${formatearDistancia(metrosCercano)})")
            }

            // El mapa ocupa todo el espacio que queda libre
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // La lista va debajo del mapa con altura fija
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = { vm.seleccionar(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
