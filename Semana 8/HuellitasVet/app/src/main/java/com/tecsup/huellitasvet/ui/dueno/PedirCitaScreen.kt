package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.veterinario.iconoServicio
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel

@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    duenoId: Int,
    mascotaInicial: Int?,
    aceptaCitas: Boolean,
    onCitaCreada: () -> Unit,
    onAtras: () -> Unit
) {
    LaunchedEffect(duenoId) { vm.iniciar(duenoId, mascotaInicial) }

    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var nota by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar("Pedir cita", onAtras = onAtras) },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        // Ajuste del veterinario: la clínica puede dejar de recibir citas
        if (!aceptaCitas) {
            Text(
                "La clínica no está recibiendo citas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(padding).padding(16.dp)
            )
            return@Scaffold
        }

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------- Mascota ----------
            Text("Mascota", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(mascotas, key = { it.id }) { m ->
                    Seleccionable(seleccionado = mascotaId == m.id, onClick = { vm.elegirMascota(m.id) }) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            MascotaAvatar(Especie.valueOf(m.especie), tamano = 48.dp)
                            Text(m.nombre)
                        }
                    }
                }
            }

            // ---------- Servicio (solo activos) ----------
            Text("Servicio", style = MaterialTheme.typography.titleMedium)
            servicios.forEach { s ->
                Seleccionable(seleccionado = servicioId == s.id, onClick = { vm.elegirServicio(s.id) }) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(iconoServicio(s.icono), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Text(s.nombre, modifier = Modifier.weight(1f))
                        Text("${s.duracionMin} min · S/ %.2f".format(s.precio), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // ---------- Fecha ----------
            Text("Fecha", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(fecha = f, seleccionada = f == fecha, onClick = { vm.elegirFecha(f) })
                }
            }

            // ---------- Hora: 18 horarios en filas de 4 ----------
            Text("Hora", style = MaterialTheme.typography.titleMedium)
            Horarios.delDia.chunked(4).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fila.forEach { h ->
                        HoraChip(
                            hora = h,
                            seleccionada = hora == h,
                            ocupado = h in ocupadas || vm.horaPasada(h), // ocupado o ya pasó
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Relleno para que la última fila no se estire
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            VetTextField(nota, { nota = it }, "Nota (opcional)")

            // ---------- Resumen ----------
            val mascota = mascotas.firstOrNull { it.id == mascotaId }
            val servicio = servicios.firstOrNull { it.id == servicioId }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Resumen", style = MaterialTheme.typography.titleMedium)
                    Text("Mascota: ${mascota?.nombre ?: "-"}")
                    Text("Servicio: ${servicio?.nombre ?: "-"}")
                    Text("Fecha: $fecha")
                    Text("Hora: ${hora ?: "-"}")
                    Text("Precio: ${servicio?.let { "S/ %.2f".format(it.precio) } ?: "-"}")
                }
            }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton(
                "Confirmar cita",
                onClick = { vm.confirmar(nota, onCitaCreada) },
                habilitado = mascota != null && servicio != null && hora != null
            )
        }
    }
}

// Tarjeta con borde del color del rol cuando está elegida
@Composable
private fun Seleccionable(seleccionado: Boolean, onClick: () -> Unit, contenido: @Composable () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (seleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) { contenido() }
}
