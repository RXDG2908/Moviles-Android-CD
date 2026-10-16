package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    duenoId: Int,
    nombre: String,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    onPedirCita: () -> Unit
) {
    LaunchedEffect(duenoId) { vm.iniciar(duenoId) }
    val mascotas by vm.mascotas.collectAsState()
    val proxima by vm.proximaCita.collectAsState()

    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Hola, ${nombre.substringBefore(" ")}", style = MaterialTheme.typography.headlineSmall)
        Text("¿Cómo están tus engreídos hoy?", style = MaterialTheme.typography.bodyMedium)

        TarjetaProximaCita(proxima)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mis mascotas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            TextButton(onClick = onAgregar) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Text("Agregar")
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mascotas, key = { it.id }) { m -> MascotaMini(m, onClick = { onMascota(m.id) }) }
        }

        VetButton("Pedir una cita", onClick = onPedirCita)
    }
}

// Tarjeta del color del rol con la próxima cita
@Composable
private fun TarjetaProximaCita(proxima: CitaConDetalle?) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp)) {
            Text("Próxima cita", style = MaterialTheme.typography.labelMedium)
            if (proxima == null) {
                Text("Aún no tienes citas", style = MaterialTheme.typography.titleMedium)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(Especie.valueOf(proxima.mascota.mascota.especie), tamano = 48.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(proxima.mascota.mascota.nombre, style = MaterialTheme.typography.titleLarge)
                        Text(proxima.servicio.nombre)
                        Text("${proxima.cita.fecha} · ${proxima.cita.hora}")
                    }
                    EstadoCitaChip(EstadoCita.valueOf(proxima.cita.estado))
                }
            }
        }
    }
}

@Composable
private fun MascotaMini(m: Mascota, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MascotaAvatar(Especie.valueOf(m.especie), tamano = 56.dp)
            Text(m.nombre, style = MaterialTheme.typography.titleMedium)
        }
    }
}
