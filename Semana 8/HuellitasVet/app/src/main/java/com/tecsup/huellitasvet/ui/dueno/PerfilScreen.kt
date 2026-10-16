package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.FilaSwitch
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.veterinario.TarjetaCambiarPerfil
import com.tecsup.huellitasvet.ui.veterinario.TarjetaUsuario
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Mismo estilo que Ajustes del veterinario (mockup 8)
@Composable
fun PerfilScreen(
    sesion: SesionViewModel,
    inicioVm: InicioViewModel,
    citasVm: MisCitasViewModel,
    duenoId: Int,
    onCambiarPerfil: (Rol) -> Unit,
    onCerrarSesion: () -> Unit
) {
    LaunchedEffect(duenoId) {
        inicioVm.iniciar(duenoId)
        citasVm.iniciar(duenoId)
    }
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val recordatorios by sesion.notificaciones.collectAsState()
    val mascotas by inicioVm.mascotas.collectAsState()
    val proximas by citasVm.proximas.collectAsState()
    val historial by citasVm.historial.collectAsState()
    val atenciones = historial.count { it.cita.estado == EstadoCita.ATENDIDA.name }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        AppTopBar("Perfil")
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TarjetaUsuario(usuario?.nombre ?: "", usuario?.correo ?: "")

            // Contadores
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador("Mascotas", mascotas.size, Modifier.weight(1f))
                Contador("Próximas", proximas.size, Modifier.weight(1f))
                Contador("Atenciones", atenciones, Modifier.weight(1f))
            }

            TarjetaCambiarPerfil("Modo práctica: ir a vista Veterinario") { sesion.cambiarPerfil(onCambiarPerfil) }

            TituloSeccion("Preferencias")
            Tarjeta {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro, "Cambia el tema de la app")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaSwitch("Recordatorios", recordatorios, sesion::cambiarNotificaciones, "Avisos de tus próximas citas")
                }
            }
            InfoCaja(texto = "Tus preferencias se guardan en el dispositivo con DataStore")

            VetButton("Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) }, peligro = true)
        }
    }
}

@Composable
fun Contador(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Tarjeta(modifier, fondo = MaterialTheme.colorScheme.primaryContainer) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            TextoGris(titulo)
        }
    }
}
