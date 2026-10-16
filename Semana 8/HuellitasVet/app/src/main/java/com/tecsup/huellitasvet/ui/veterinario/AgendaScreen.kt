package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.OpcionChip
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()

    Column {
        AppTopBar("Agenda")

        // Chips de los próximos días: "Hoy 21", "Jue 22", ...
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vm.fechas) { f ->
                val texto = if (f == LocalDate.now()) "Hoy ${f.dayOfMonth}" else Fechas.corta(f.toString())
                OpcionChip(texto, seleccionado = f == fecha, onClick = { vm.elegirFecha(f) })
            }
        }

        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            if (citas.isEmpty()) item { TextoGris("No hay citas este día") }

            // Línea de tiempo: hora a la izquierda, línea vertical y tarjeta a la derecha
            items(citas, key = { it.cita.id }) { c ->
                val estado = EstadoCita.valueOf(c.cita.estado)
                Row(Modifier.height(IntrinsicSize.Min)) {
                    Text(
                        c.cita.hora,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(52.dp).padding(top = 14.dp)
                    )
                    Box(Modifier.width(2.dp).fillMaxHeight().background(MaterialTheme.colorScheme.primaryContainer))
                    Spacer(Modifier.width(10.dp))
                    CitaCard(
                        titulo = "${c.mascota.mascota.nombre} · ${c.servicio.nombre}",
                        detalle = "Dueño: ${c.mascota.dueno.nombre}",
                        estado = estado,
                        modifier = Modifier.weight(1f).padding(bottom = 12.dp),
                        accionesEnLinea = false,
                        inicio = { MascotaAvatar(Especie.valueOf(c.mascota.mascota.especie), tamano = 46.dp) }
                    ) {
                        if (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) {
                            TextButton(onClick = { vm.cancelar(c) }) {
                                Text("Cancelar", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                        when (estado) {
                            EstadoCita.PENDIENTE -> BotonChico("Confirmar") { vm.confirmar(c) }
                            EstadoCita.CONFIRMADA -> BotonChico("Atender") { onAtender(c.cita.id) }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

// Botón compacto del color del rol (Confirmar / Atender)
@Composable
private fun BotonChico(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
        modifier = Modifier.height(38.dp)
    ) { Text(texto, style = MaterialTheme.typography.labelLarge) }
}
