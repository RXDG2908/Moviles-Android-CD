package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita

// Tarjeta de cita. 'acciones' permite poner botones (Cancelar, Confirmar, Atender...)
@Composable
fun CitaCard(
    mascota: String,
    especie: Especie,
    servicio: String,
    fecha: String,
    hora: String,
    estado: EstadoCita,
    precio: Double,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    onClick: (() -> Unit)? = null,
    acciones: @Composable () -> Unit = {}
) {
    val contenido: @Composable () -> Unit = {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MascotaAvatar(especie, tamano = 44.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(mascota, style = MaterialTheme.typography.titleMedium)
                    Text(servicio, style = MaterialTheme.typography.bodyMedium)
                    subtitulo?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
                }
                EstadoCitaChip(estado)
            }
            Row(
                Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$fecha · $hora", style = MaterialTheme.typography.bodyMedium)
                Text("S/ %.2f".format(precio), style = MaterialTheme.typography.titleMedium)
            }
            acciones()
        }
    }
    val forma = RoundedCornerShape(16.dp)
    val colores = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = forma, colors = colores) { contenido() }
    } else {
        Card(modifier = modifier.fillMaxWidth(), shape = forma, colors = colores) { contenido() }
    }
}
