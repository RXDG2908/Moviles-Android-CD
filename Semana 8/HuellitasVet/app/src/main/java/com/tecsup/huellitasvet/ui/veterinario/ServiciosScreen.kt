package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

// [E] EJEMPLO RESUELTO: la View solo observa y avisa
@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit, modifier: Modifier = Modifier) {
    val servicios by vm.servicios.collectAsState()
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(servicios, key = { it.id }) { s ->
            ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) })
        }
    }
}

fun iconoServicio(icono: String): ImageVector = when (icono) {
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.Favorite
}

@Composable
fun ServicioItem(s: Servicio, onToggle: () -> Unit, onEditar: () -> Unit) {
    Card(
        onClick = onEditar,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                iconoServicio(s.icono), contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s.nombre, style = MaterialTheme.typography.titleMedium)
                Text("${s.duracionMin} min · S/ %.2f".format(s.precio), style = MaterialTheme.typography.bodyMedium)
                if (!s.activo) Text("Inactivo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
        }
    }
}
