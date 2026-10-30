package com.tecsup.huellitasvet.ui.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

// Mejora: panel del administrador (control total)
@Composable
fun AdminPanelScreen(
    vm: AdminViewModel,
    nombre: String,
    onUsuarios: (Rol) -> Unit,
    onNuevoUsuario: (Rol) -> Unit,
    onMascotas: () -> Unit,
    onNuevaMascota: () -> Unit,
    onServicios: () -> Unit,
    onAgenda: () -> Unit,
    onAjustes: () -> Unit
) {
    val r by vm.resumen.collectAsState()
    val esquema = MaterialTheme.colorScheme

    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ---------- Saludo ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Hola, ${nombre.substringBefore(" ")}", style = MaterialTheme.typography.headlineSmall)
                TextoGris("Administrador · control total de la clínica")
            }
            IconButton(onClick = onAjustes) { Icon(Icons.Outlined.Settings, contentDescription = "Ajustes") }
        }

        // ---------- Tarjeta principal ----------
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = esquema.primary, contentColor = esquema.onPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Usuarios registrados", style = MaterialTheme.typography.bodySmall)
                    Text("${r.usuarios}", style = MaterialTheme.typography.headlineMedium)
                    Text("${r.citas} citas · ${r.mascotas} mascotas", style = MaterialTheme.typography.labelLarge)
                }
                Box(
                    Modifier.size(56.dp).background(esquema.onPrimary.copy(alpha = 0.16f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(30.dp)) }
            }
        }

        // ---------- Mosaico de secciones (cada una con su color) ----------
        TituloSeccion("Gestionar")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Mosaico("Dueños", r.duenos, Icons.Filled.Groups, colorDeRol(Rol.CLIENTE), Modifier.weight(1f)) { onUsuarios(Rol.CLIENTE) }
            Mosaico("Veterinarios", r.veterinarios, Icons.Filled.MedicalServices, colorDeRol(Rol.ADMIN), Modifier.weight(1f)) { onUsuarios(Rol.ADMIN) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Mosaico("Mascotas", r.mascotas, Icons.Filled.Pets, colorDeRol(Rol.ADMINISTRADOR), Modifier.weight(1f), onMascotas)
            Mosaico("Servicios", r.servicios, Icons.Filled.LocalOffer, colorDeRol(Rol.ADMINISTRADOR), Modifier.weight(1f), onServicios)
        }

        // ---------- Acciones rápidas ----------
        TituloSeccion("Acciones rápidas")
        Tarjeta {
            Accion(Icons.Outlined.PersonAdd, "Nuevo dueño", "Registrar un cliente con su celular y distrito") { onNuevoUsuario(Rol.CLIENTE) }
            Accion(Icons.Outlined.PersonAdd, "Nuevo veterinario", "Dar acceso al panel de la clínica") { onNuevoUsuario(Rol.ADMIN) }
            Accion(Icons.Filled.Pets, "Nueva mascota", "Asignarla a cualquier dueño", onNuevaMascota)
            Accion(Icons.Filled.EventNote, "Agenda de citas", "Confirmar, atender o cancelar", onAgenda)
            Accion(Icons.Filled.AdminPanelSettings, "Administradores", "${r.administradores} con control total") { onUsuarios(Rol.ADMINISTRADOR) }
        }
    }
}

@Composable
private fun Mosaico(titulo: String, valor: Int, icono: ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Tarjeta(modifier, onClick = onClick, fondo = color.copy(alpha = 0.12f)) {
        Column(Modifier.padding(14.dp)) {
            Icon(icono, contentDescription = null, tint = color)
            Spacer(Modifier.size(8.dp))
            Text("$valor", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            Text(titulo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Accion(icono: ImageVector, titulo: String, subtitulo: String, onClick: () -> Unit) {
    val esquema = MaterialTheme.colorScheme
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(38.dp).background(esquema.primaryContainer, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(icono, contentDescription = null, tint = esquema.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleSmall)
                TextoGris(subtitulo)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = esquema.onSurfaceVariant)
        }
    }
}
