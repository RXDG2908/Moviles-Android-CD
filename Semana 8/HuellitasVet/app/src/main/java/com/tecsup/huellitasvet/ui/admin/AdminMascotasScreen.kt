package com.tecsup.huellitasvet.ui.admin

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

// Mejora: mascotas de todos los dueños. Ver ficha, editar, agregar y eliminar (con sus citas).
@Composable
fun AdminMascotasScreen(vm: AdminViewModel, onNueva: () -> Unit, onEditar: (Int) -> Unit, onFicha: (Int) -> Unit) {
    val mascotas by vm.mascotas.collectAsState()
    val busqueda by vm.busquedaMascota.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var porEliminar by remember { mutableStateOf<MascotaConDueno?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }

    Scaffold(
        topBar = { AppTopBar("Mascotas") },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNueva,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nueva mascota") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(18.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                VetTextField(
                    busqueda, vm::buscarMascota, "Buscar mascota, raza o dueño",
                    icono = Icons.Outlined.Search,
                    accion = if (busqueda.isNotEmpty()) { { IconButton(onClick = { vm.buscarMascota("") }) { Icon(Icons.Filled.Clear, "Limpiar") } } } else null
                )
            }
            item { TextoGris("${mascotas.size} mascotas") }
            if (mascotas.isEmpty()) item { InfoCaja(texto = "No hay mascotas que coincidan con la búsqueda") }

            items(mascotas, key = { it.mascota.id }) { m ->
                Tarjeta(onClick = { onFicha(m.mascota.id) }) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(Especie.valueOf(m.mascota.especie), tamano = 52.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(m.mascota.nombre, style = MaterialTheme.typography.titleSmall)
                            TextoGris("${m.mascota.raza.ifBlank { Especie.valueOf(m.mascota.especie).etiqueta }} · ${m.mascota.peso} kg")
                            TextoGris("Dueño: ${m.dueno.nombre}")
                        }
                        IconButton(onClick = { onEditar(m.mascota.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { porEliminar = m }) {
                            Icon(Icons.Outlined.DeleteOutline, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    porEliminar?.let { m ->
        DialogoEliminar(
            titulo = "¿Eliminar a ${m.mascota.nombre}?",
            texto = "También se borrará su historial de citas. No se puede deshacer.",
            onConfirmar = { vm.eliminarMascota(m.mascota); porEliminar = null },
            onCancelar = { porEliminar = null }
        )
    }
}
