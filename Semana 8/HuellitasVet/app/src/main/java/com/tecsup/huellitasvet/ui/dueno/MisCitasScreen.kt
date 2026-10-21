package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.AvisoCard
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.IconoCaja
import com.tecsup.huellitasvet.ui.components.SelectorPestanas
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.veterinario.iconoServicio
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(vm: MisCitasViewModel, duenoId: Int, onPedirCita: () -> Unit) {
    LaunchedEffect(duenoId) { vm.iniciar(duenoId) }
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<CitaConDetalle?>(null) }

    Scaffold(
        topBar = { AppTopBar("Mis citas") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPedirCita,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            ) { Icon(Icons.Filled.Add, contentDescription = "Pedir cita") }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(Modifier.padding(padding)) {
            SelectorPestanas(
                listOf("Próximas", "Historial"), pestana, { pestana = it },
                Modifier.padding(horizontal = 16.dp)
            )

            val lista = if (pestana == 0) proximas else historial
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                mensaje?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
                if (lista.isEmpty()) {
                    item { TextoGris(if (pestana == 0) "No tienes citas próximas" else "Aún no hay citas en tu historial") }
                }
                items(lista, key = { it.cita.id }) { c ->
                    CitaCard(
                        titulo = c.servicio.nombre,
                        detalle = "${c.mascota.mascota.nombre} · ${Fechas.corta(c.cita.fecha)} · ${c.cita.hora} · S/ %.2f".format(c.cita.precio),
                        estado = EstadoCita.valueOf(c.cita.estado),
                        inicio = { IconoCaja(iconoServicio(c.servicio.icono)) }
                    ) {
                        // El botón solo aparece si la regla de 2 horas lo permite
                        if (vm.puedeCancelar(c)) {
                            TextButton(onClick = { porCancelar = c }) {
                                Text("Cancelar", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge)
                            }
                        } else if (pestana == 0) {
                            TextoGris("Ya no puedes cancelar")
                        }
                    }
                }
                if (pestana == 0) {
                    item { AvisoCard(icono = Icons.Outlined.Schedule, texto = "Puedes cancelar hasta 2 horas antes") }
                }
            }
        }
    }

    // Confirmación antes de cancelar
    porCancelar?.let { c ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("Cancelar cita") },
            text = { Text("¿Cancelar la cita de ${c.mascota.mascota.nombre} del ${Fechas.larga(c.cita.fecha)} a las ${c.cita.hora}?") },
            confirmButton = {
                TextButton(onClick = { vm.cancelar(c); porCancelar = null }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { porCancelar = null }) { Text("No") }
            }
        )
    }
}
