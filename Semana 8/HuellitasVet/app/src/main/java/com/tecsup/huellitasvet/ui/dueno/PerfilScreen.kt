package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    sesion: SesionViewModel,
    inicioVm: InicioViewModel,
    citasVm: MisCitasViewModel,
    duenoId: Int,
    onCerrarSesion: () -> Unit
) {
    LaunchedEffect(duenoId) {
        inicioVm.iniciar(duenoId)
        citasVm.iniciar(duenoId)
    }
    val usuario by sesion.usuarioActual.collectAsState()
    val mascotas by inicioVm.mascotas.collectAsState()
    val proximas by citasVm.proximas.collectAsState()
    val historial by citasVm.historial.collectAsState()
    val atenciones = historial.count { it.cita.estado == EstadoCita.ATENDIDA.name }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        AppTopBar("Perfil")
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(usuario?.nombre ?: "", style = MaterialTheme.typography.headlineSmall)
            Text(usuario?.correo ?: "", style = MaterialTheme.typography.bodyMedium)

            // Contadores
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador("Mascotas", mascotas.size, Modifier.weight(1f))
                Contador("Próximas", proximas.size, Modifier.weight(1f))
                Contador("Atenciones", atenciones, Modifier.weight(1f))
            }

            VetButton("Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) }, secundario = true,
                modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun Contador(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.labelMedium)
        }
    }
}
