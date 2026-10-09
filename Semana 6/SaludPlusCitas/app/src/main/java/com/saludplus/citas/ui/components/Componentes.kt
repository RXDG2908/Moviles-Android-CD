package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Componentes reutilizables por crear en este paquete (sugerencia):
// TODO: BarraSuperior(titulo, onVolver)        -> barra superior con flecha de volver
// TODO: BotonPrincipal(texto, habilitado, onClick) -> botón azul de ancho completo
// TODO: TarjetaEspecialidad(especialidad, onClick)
// TODO: TarjetaMedico(medico, onClick)
// TODO: TarjetaCita(cita, onClick)
// TODO: BarraInferior(navController, rutaActual) -> NavigationBar: Inicio, Citas, Resultados, Perfil

// Contenido temporal de las pantallas que aún no están terminadas.
// Al completar una pantalla, borra la llamada a PantallaEnConstruccion.
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    botones: List<Pair<String, () -> Unit>> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Pantalla en construcción")
        botones.forEach { (texto, accion) ->
            Button(onClick = accion, modifier = Modifier.fillMaxWidth()) {
                Text(texto)
            }
        }
    }
}
