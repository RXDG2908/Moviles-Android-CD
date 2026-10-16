package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.FilaSwitch
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(sesion: SesionViewModel, onCambiarPerfil: (Rol) -> Unit, onCerrarSesion: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val aceptarCitas by sesion.aceptarCitas.collectAsState()

    Column(Modifier.verticalScroll(rememberScrollState())) {
        AppTopBar("Ajustes")
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(usuario?.nombre ?: "", style = MaterialTheme.typography.headlineSmall)
            Text(usuario?.correo ?: "", style = MaterialTheme.typography.bodyMedium)

            VetButton("Cambiar de perfil", onClick = { sesion.cambiarPerfil(onCambiarPerfil) })

            HorizontalDivider()
            FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro)
            FilaSwitch("Notificaciones", notificaciones, sesion::cambiarNotificaciones)
            FilaSwitch("Aceptar citas", aceptarCitas, sesion::cambiarAceptarCitas)
            HorizontalDivider()

            VetButton("Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) }, secundario = true)
        }
    }
}
