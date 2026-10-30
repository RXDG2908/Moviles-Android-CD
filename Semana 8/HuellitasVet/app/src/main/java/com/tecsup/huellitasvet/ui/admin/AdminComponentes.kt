// =====================================================================================
// GUÍA · Paquete ui/admin (mejora: rol Administrador con control total)
//   AdminPanelScreen       -> resumen + mosaico (Dueños, Veterinarios, Mascotas, Servicios) + acciones rápidas
//   AdminUsuariosScreen    -> pestañas por rol, búsqueda, editar y eliminar (con confirmación)
//   AdminFormUsuarioScreen -> crear/editar usuario de cualquier rol (rol y distrito en combobox)
//   AdminMascotasScreen    -> mascotas de todos los dueños; eliminar borra también sus citas
//   AdminComponentes (este archivo) -> piezas compartidas: color de rol, avatar con iniciales, diálogo
// Todas usan un solo AdminViewModel (viewmodel/AdminViewModel.kt), compartido en el grafo "grafo_admin"
// de NavVet. Servicios, Agenda y Atender cita reutilizan las pantallas del veterinario.
// =====================================================================================
package com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.theme.primarioDe
import com.tecsup.huellitasvet.ui.theme.esTemaOscuro

// Color propio de cada rol (verde, azul o terracota), también dentro del tema del administrador
@Composable
fun colorDeRol(rol: Rol): Color = primarioDe(rol, esTemaOscuro())

// Círculo con las iniciales del usuario, del color de su rol
@Composable
fun AvatarIniciales(nombre: String, rol: Rol, tamano: Dp = 46.dp) {
    val color = colorDeRol(rol)
    val iniciales = nombre.split(" ")
        .filter { it.isNotBlank() && it.first().isLetter() && !it.endsWith(".") }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }
    Box(Modifier.size(tamano).background(color.copy(alpha = 0.16f), CircleShape), contentAlignment = Alignment.Center) {
        Text(iniciales, color = color, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
}

// Confirmación antes de borrar (acción que no se puede deshacer)
@Composable
fun DialogoEliminar(titulo: String, texto: String, onConfirmar: () -> Unit, onCancelar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo) },
        text = { Text(texto) },
        confirmButton = {
            TextButton(onClick = onConfirmar) { Text("Eliminar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
