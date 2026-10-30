package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Pets
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
import com.tecsup.huellitasvet.model.TipoServicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.IconoCaja
import com.tecsup.huellitasvet.ui.components.OpcionChip
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TipoServicioEtiqueta
import com.tecsup.huellitasvet.ui.theme.colorDeTipo
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

// EJEMPLO RESUELTO: la View solo observa y avisa
// Mejora: filtro por tipo de servicio y etiqueta de color en cada servicio
@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit, onNuevo: () -> Unit) {
    val todos by vm.servicios.collectAsState()
    val servicios by vm.serviciosFiltrados.collectAsState()
    val filtro by vm.filtroTipo.collectAsState()
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
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { OpcionChip("Todos (${todos.size})", filtro == null, { vm.filtrar(null) }) }
                    items(TipoServicio.entries) { t ->
                        OpcionChip(
                            "${t.etiqueta} (${todos.count { it.tipo == t.name }})",
                            filtro == t,
                            { vm.filtrar(t) },
                            inicio = { Icon(com.tecsup.huellitasvet.ui.components.iconoTipo(t), null, tint = if (filtro == t) MaterialTheme.colorScheme.onPrimary else colorDeTipo(t), modifier = Modifier.size(18.dp)) }
                        )
                    }
                }
            }
            items(servicios, key = { it.id }) { s ->
                ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) })
            }
        }
    }
}

// Íconos disponibles para un servicio (el valor se guarda en Room)
// Mejora (tipo de servicio): de 3 íconos a 7 (baño, huella, diente y balanza) para los servicios nuevos
val iconosServicio = listOf("heart", "syringe", "scissors", "bath", "paw", "tooth", "weight")

fun iconoServicio(icono: String): ImageVector = when (icono) {
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    "bath" -> Icons.Filled.Bathtub
    "paw" -> Icons.Filled.Pets
    "tooth" -> Icons.Filled.MedicalServices
    "weight" -> Icons.Filled.MonitorWeight
    else -> Icons.Filled.Favorite
}

@Composable
fun ServicioItem(s: Servicio, onToggle: () -> Unit, onEditar: () -> Unit) {
    val tipo = TipoServicio.de(s.tipo)
    Tarjeta(onClick = onEditar) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconoCaja(iconoServicio(s.icono), color = colorDeTipo(tipo))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s.nombre, style = MaterialTheme.typography.titleSmall)
                TextoGris("${s.duracionMin} min · S/ %.2f".format(s.precio))
                Spacer(Modifier.height(4.dp))
                TipoServicioEtiqueta(tipo)
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
