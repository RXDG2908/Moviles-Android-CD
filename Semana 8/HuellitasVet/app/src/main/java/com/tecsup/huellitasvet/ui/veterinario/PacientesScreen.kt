package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.OpcionChip
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel

@Composable
fun PacientesScreen(vm: PacientesViewModel, onPaciente: (Int) -> Unit) {
    val texto by vm.texto.collectAsState()
    val especie by vm.especie.collectAsState()
    val pacientes by vm.pacientes.collectAsState()
    val visitas by vm.ultimasVisitas.collectAsState()
    val hayFiltros by vm.hayFiltros.collectAsState()

    // Chips: Todos / Perros / Gatos / Otros
    val filtros = listOf<Pair<String, Especie?>>(
        "Todos" to null, "Perros" to Especie.PERRO, "Gatos" to Especie.GATO, "Otros" to Especie.OTRO
    )

    Column {
        AppTopBar("Pacientes")
        Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            VetTextField(
                texto, vm::buscar, "Buscar mascota, raza o dueño...", icono = Icons.Filled.Search,
                accion = if (texto.isNotEmpty()) {
                    { IconButton(onClick = { vm.buscar("") }) { Icon(Icons.Filled.Close, contentDescription = "Borrar texto") } }
                } else null
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtros) { (etiqueta, e) ->
                    OpcionChip(etiqueta, seleccionado = especie == e, onClick = { vm.filtrar(e) })
                }
            }
            // Cuántos pacientes coinciden con el texto y la especie a la vez
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextoGris(
                    if (pacientes.size == 1) "1 paciente encontrado" else "${pacientes.size} pacientes encontrados",
                    Modifier.weight(1f)
                )
                if (hayFiltros) {
                    TextButton(onClick = vm::limpiar) { Text("Limpiar filtros", style = MaterialTheme.typography.labelLarge) }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (pacientes.isEmpty()) item { TextoGris("No se encontraron pacientes con esos filtros") }
            items(pacientes, key = { it.mascota.id }) { p ->
                val m = p.mascota
                Tarjeta(onClick = { onPaciente(m.id) }) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(Especie.valueOf(m.especie), tamano = 56.dp)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(m.nombre, style = MaterialTheme.typography.titleSmall)
                            TextoGris("${m.raza} · ${Fechas.textoEdad(m.anioNacimiento)}")
                            TextoGris("Dueño: ${p.dueno.nombre}")
                            TextoGris(visitas[m.id]?.let { "Última visita: ${Fechas.diaMes(it)}" } ?: "Sin visitas")
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
