package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()

    Column {
        AppTopBar("Agenda")

        // Chips de los próximos días
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vm.fechas) { f -> FechaChip(f, seleccionada = f == fecha, onClick = { vm.elegirFecha(f) }) }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (citas.isEmpty()) item { Text("No hay citas este día") }

            // Línea de tiempo: la hora a la izquierda y la tarjeta a la derecha
            items(citas, key = { it.cita.id }) { c ->
                val estado = EstadoCita.valueOf(c.cita.estado)
                Row {
                    Text(
                        c.cita.hora,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(56.dp).padding(top = 14.dp)
                    )
                    CitaCard(
                        mascota = c.mascota.mascota.nombre,
                        especie = Especie.valueOf(c.mascota.mascota.especie),
                        servicio = c.servicio.nombre,
                        fecha = c.cita.fecha,
                        hora = c.cita.hora,
                        estado = estado,
                        precio = c.cita.precio,
                        subtitulo = "Dueño: ${c.mascota.dueno.nombre}"
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                            when (estado) {
                                EstadoCita.PENDIENTE -> Button(onClick = { vm.confirmar(c) }) { Text("Confirmar") }
                                EstadoCita.CONFIRMADA -> Button(onClick = { onAtender(c.cita.id) }) { Text("Atender") }
                                else -> {}
                            }
                            if (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) {
                                TextButton(onClick = { vm.cancelar(c) }) {
                                    Text("Cancelar", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
