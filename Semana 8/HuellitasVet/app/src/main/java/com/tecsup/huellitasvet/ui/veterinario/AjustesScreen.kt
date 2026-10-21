package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.FilaSwitch
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(sesion: SesionViewModel, onCambiarPerfil: (Rol) -> Unit, onCerrarSesion: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val aceptarCitas by sesion.aceptarCitas.collectAsState()

    Column(Modifier.verticalScroll(rememberScrollState())) {
        AppTopBar("Ajustes")
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TarjetaUsuario(usuario?.nombre ?: "", usuario?.correo ?: "")

            TarjetaCambiarPerfil("Modo práctica: ir a vista Dueño") { sesion.cambiarPerfil(onCambiarPerfil) }

            TituloSeccion("Preferencias")
            Tarjeta {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro, "Cambia el tema de la app")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaSwitch("Notificaciones", notificaciones, sesion::cambiarNotificaciones, "Avisar citas nuevas")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaSwitch("Aceptar citas", aceptarCitas, sesion::cambiarAceptarCitas, "Abrir o cerrar la agenda")
                }
            }
            InfoCaja(texto = "Se guardan en el dispositivo con DataStore")

            VetButton("Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) }, peligro = true)
        }
    }
}

// Tarjeta con el usuario y la píldora "Perfil activo" (también la usa Perfil del dueño)
@Composable
fun TarjetaUsuario(nombre: String, correo: String) {
    Tarjeta {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(56.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(nombre, style = MaterialTheme.typography.titleMedium)
                TextoGris(correo)
                Text(
                    "Perfil activo",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }
        }
    }
}

// Tarjeta del color del rol para cambiar de perfil
@Composable
fun TarjetaCambiarPerfil(subtitulo: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.SwapHoriz, contentDescription = null)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("Cambiar de perfil", style = MaterialTheme.typography.titleSmall)
                Text(subtitulo, style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}
