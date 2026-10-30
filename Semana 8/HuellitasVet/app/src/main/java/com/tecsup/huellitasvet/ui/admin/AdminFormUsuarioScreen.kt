package com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Distritos
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetCombo
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

// Mejora: crear o editar un usuario de cualquier rol
@Composable
fun AdminFormUsuarioScreen(vm: AdminViewModel, usuarioId: Int?, rolInicial: Rol, onListo: () -> Unit, onAtras: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var distrito by remember { mutableStateOf<String?>(null) }
    var rol by remember { mutableStateOf(rolInicial) }
    var clave by remember { mutableStateOf("") }
    val errores by vm.errores.collectAsState()

    // Modo editar: carga los datos una sola vez
    val existente by remember(usuarioId) { vm.usuario(usuarioId ?: 0) }.collectAsState()
    var cargado by remember { mutableStateOf(false) }
    LaunchedEffect(existente) {
        val u = existente
        if (u != null && !cargado) {
            nombre = u.nombre; correo = u.correo; telefono = u.telefono
            distrito = u.distrito.ifBlank { null }; rol = Rol.valueOf(u.rol)
            cargado = true
        }
    }
    DisposableEffect(Unit) { onDispose { vm.limpiarErrores() } }

    Scaffold(
        topBar = { AppTopBar(if (usuarioId == null) "Nuevo usuario" else "Editar usuario", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).imePadding().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(Modifier.align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally) {
                AvatarIniciales(nombre.ifBlank { "?" }, rol, tamano = 72.dp)
                Text(rol.etiqueta, style = MaterialTheme.typography.labelLarge, color = colorDeRol(rol))
            }

            VetCombo(
                etiqueta = "Rol",
                opciones = Rol.entries,
                seleccion = rol,
                onSeleccion = { rol = it },
                texto = { it.etiqueta },
                detalle = {
                    when (it) {
                        Rol.CLIENTE -> "Pide citas para sus mascotas"
                        Rol.ADMIN -> "Atiende la agenda y los pacientes"
                        Rol.ADMINISTRADOR -> "Control total de la app"
                    }
                },
                icono = Icons.Outlined.Badge,
                iconoOpcion = { AvatarIniciales(it.etiqueta, it, tamano = 28.dp) }
            )
            VetTextField(nombre, { nombre = it }, "Nombre y apellido", error = errores.nombre, icono = Icons.Outlined.Person)
            VetTextField(correo, { correo = it }, "Correo", error = errores.correo, teclado = KeyboardType.Email, icono = Icons.Outlined.Email)
            VetTextField(
                telefono, { nuevo -> telefono = nuevo.filter { it.isDigit() }.take(9) }, "Celular",
                error = errores.telefono, teclado = KeyboardType.Phone, icono = Icons.Outlined.Phone
            )
            VetCombo(
                etiqueta = "Distrito",
                opciones = Distritos.lima,
                seleccion = distrito,
                onSeleccion = { distrito = it },
                texto = { it },
                error = errores.distrito,
                icono = Icons.Outlined.LocationOn
            )
            VetTextField(
                clave, { clave = it }, if (usuarioId == null) "Contraseña" else "Nueva contraseña (opcional)",
                error = errores.clave, esClave = true, icono = Icons.Outlined.Lock
            )
            if (usuarioId != null) InfoCaja(texto = "Deja la contraseña vacía para mantener la actual.")

            VetButton(if (usuarioId == null) "Crear usuario" else "Guardar cambios", onClick = {
                vm.guardarUsuario(usuarioId ?: 0, nombre, correo, telefono, distrito ?: "", rol, clave, onListo)
            })
            VetButton("Cancelar", onClick = onAtras, secundario = true)
        }
    }
}
