package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.SesionIniciada
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris

// Pantalla 5 - Médicos: usa el parámetro especialidadId para mostrar
// los médicos de esa especialidad, de mejor a menor calificación.
@Composable
fun MedicosScreen(navController: NavController, especialidadId: Int) {
    // Sin sesión no se puede agendar: se manda a Login
    if (!SesionIniciada(navController)) return

    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    var mostrarBusqueda by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }

    // Solo los médicos de la sede elegida (filter + sortedByDescending)
    // y, si se escribió algo, también por nombre
    val sedeId = Repositorio.sedeActual?.id ?: 0
    val medicos = Repositorio.medicosPorSede(sedeId, especialidadId)
        .filter { it.nombre.contains(busqueda.trim(), ignoreCase = true) }

    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperior(
                titulo = "Médicos de ${especialidad?.nombre ?: ""}",
                acciones = {
                    IconButton(onClick = { mostrarBusqueda = !mostrarBusqueda }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar médico")
                    }
                }
            ) { navController.popBackStack() }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Buscador por nombre (se muestra al tocar la lupa)
            if (mostrarBusqueda) {
                TextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar médico...", color = TextoGris) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextoGris) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = GrisSuave,
                        unfocusedContainerColor = GrisSuave,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(medicos) { medico ->
                    TarjetaMedico(medico) {
                        navController.navigate(Rutas.fechaHora(medico.id))
                    }
                }
            }
        }
    }
}
