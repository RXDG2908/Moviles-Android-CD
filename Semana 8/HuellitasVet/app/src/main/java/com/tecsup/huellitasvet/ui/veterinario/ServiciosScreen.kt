package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.IconoCaja
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

// EJEMPLO RESUELTO: la View solo observa y avisa
@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit, onNuevo: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    Scaffold(
        topBar = { AppTopBar("Servicios") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevo,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            ) { Icon(Icons.Filled.Add, contentDescription = "Nuevo servicio") }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) })
            }
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
    Tarjeta(onClick = onEditar) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconoCaja(iconoServicio(s.icono))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s.nombre, style = MaterialTheme.typography.titleSmall)
                TextoGris("${s.duracionMin} min · S/ %.2f".format(s.precio))
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
