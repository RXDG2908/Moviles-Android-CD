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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.PersonAdd
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
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.SelectorPestanas
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

private val pestanas = listOf(Rol.CLIENTE to "Dueños", Rol.ADMIN to "Veterinarios", Rol.ADMINISTRADOR to "Admins")

// Mejora: usuarios de los 3 roles. Agregar, editar y eliminar.
@Composable
fun AdminUsuariosScreen(vm: AdminViewModel, idActual: Int, onNuevo: (Rol) -> Unit, onEditar: (Int) -> Unit) {
    val rol by vm.rol.collectAsState()
    val usuarios by vm.usuarios.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val mascotasPorDueno by vm.mascotasPorDueno.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var porEliminar by remember { mutableStateOf<Usuario?>(null) }
    val snackbar = remember { SnackbarHostState() }

    // Avisos del ViewModel (eliminado, no se puede eliminar...) en un Snackbar
    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }

    Scaffold(
        topBar = { AppTopBar("Usuarios") },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNuevo(rol) },
                icon = { Icon(Icons.Outlined.PersonAdd, contentDescription = null) },
                text = { Text("Nuevo ${rol.etiqueta.lowercase()}") },
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
                SelectorPestanas(
                    opciones = pestanas.map { it.second },
                    seleccionada = pestanas.indexOfFirst { it.first == rol },
                    onSeleccion = { vm.elegirRol(pestanas[it].first) }
                )
            }
            item {
                VetTextField(
                    busqueda, vm::buscar, "Buscar por nombre, correo o distrito",
                    icono = Icons.Outlined.Search,
                    accion = if (busqueda.isNotEmpty()) { { IconButton(onClick = { vm.buscar("") }) { Icon(Icons.Filled.Clear, "Limpiar") } } } else null
                )
            }
            item { TextoGris("${usuarios.size} ${pestanas.first { it.first == rol }.second.lowercase()}") }

            if (usuarios.isEmpty()) {
                item { InfoCaja(texto = "No hay usuarios que coincidan con la búsqueda") }
            }
            items(usuarios, key = { it.id }) { u ->
                UsuarioItem(
                    u = u,
                    esYo = u.id == idActual,
                    mascotas = mascotasPorDueno[u.id],
                    onEditar = { onEditar(u.id) },
                    onEliminar = { porEliminar = u }
                )
            }
        }
    }

    porEliminar?.let { u ->
        val n = mascotasPorDueno[u.id] ?: 0
        DialogoEliminar(
            titulo = "¿Eliminar a ${u.nombre}?",
            texto = if (u.rol == Rol.CLIENTE.name && n > 0)
                "También se eliminarán sus $n mascotas y todas sus citas. No se puede deshacer."
            else "Se borrará su cuenta. No se puede deshacer.",
            onConfirmar = { vm.eliminarUsuario(u, idActual); porEliminar = null },
            onCancelar = { porEliminar = null }
        )
    }
}

@Composable
private fun UsuarioItem(u: Usuario, esYo: Boolean, mascotas: Int?, onEditar: () -> Unit, onEliminar: () -> Unit) {
    val rol = Rol.valueOf(u.rol)
    Tarjeta(onClick = onEditar) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            AvatarIniciales(u.nombre, rol)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(if (esYo) "${u.nombre} (tú)" else u.nombre, style = MaterialTheme.typography.titleSmall)
                TextoGris(u.correo)
                val extra = listOfNotNull(
                    u.telefono.ifBlank { null },
                    u.distrito.ifBlank { null },
                    if (rol == Rol.CLIENTE) "${mascotas ?: 0} mascotas" else null
                )
                if (extra.isNotEmpty()) TextoGris(extra.joinToString(" · "))
            }
            if (!esYo) {
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
