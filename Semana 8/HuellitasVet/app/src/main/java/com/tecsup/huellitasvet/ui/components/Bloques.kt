package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente

// Piezas pequeñas que se repiten en varias pantallas del mockup

// Título de sección ("Mis mascotas", "Servicios", "Preferencias")
@Composable
fun TituloSeccion(texto: String, modifier: Modifier = Modifier) {
    Text(texto, style = MaterialTheme.typography.titleSmall, modifier = modifier)
}

// Ícono dentro de un cuadrado redondeado del color del contenedor
@Composable
fun IconoCaja(icono: ImageVector, tamano: Dp = 48.dp, color: Color = MaterialTheme.colorScheme.primary) {
    Box(
        Modifier.size(tamano).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(tamano / 2))
    }
}

// Aviso ámbar (recordatorios, "puedes cancelar hasta 2 horas antes", citas por confirmar)
@Composable
fun AvisoCard(
    icono: ImageVector,
    texto: String,
    modifier: Modifier = Modifier,
    titulo: String? = null,
    accion: (@Composable () -> Unit)? = null
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(EstadoPendiente.copy(alpha = 0.14f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = EstadoPendiente)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            titulo?.let { Text(it, style = MaterialTheme.typography.titleSmall, color = EstadoPendiente) }
            Text(texto, style = MaterialTheme.typography.bodySmall)
        }
        accion?.invoke()
    }
}

// Caja informativa del color del contenedor ("Modo práctica", "Se guardan con DataStore")
@Composable
fun InfoCaja(texto: String, modifier: Modifier = Modifier, titulo: String? = null, icono: ImageVector? = null) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icono?.let {
            Icon(it, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
        }
        Column {
            titulo?.let { Text(it, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary) }
            Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// Fila con título, subtítulo opcional y switch (Perfil y Ajustes)
@Composable
fun FilaSwitch(titulo: String, activado: Boolean, onCambio: (Boolean) -> Unit, subtitulo: String? = null) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            subtitulo?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(checked = activado, onCheckedChange = onCambio)
    }
}

// Texto gris secundario
@Composable
fun TextoGris(texto: String, modifier: Modifier = Modifier) {
    Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = modifier)
}
