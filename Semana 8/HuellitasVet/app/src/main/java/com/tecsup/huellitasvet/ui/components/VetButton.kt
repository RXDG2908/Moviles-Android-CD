package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Botón principal del color del rol. secundario = borde del color del rol.
// peligro = borde y texto rojos (Cancelar cita, Cerrar sesión).
@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    secundario: Boolean = false,
    peligro: Boolean = false,
    icono: ImageVector? = null
) {
    val m = modifier.fillMaxWidth().height(52.dp)
    val forma = RoundedCornerShape(14.dp)
    val contenido: @Composable () -> Unit = {
        icono?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
    if (secundario || peligro) {
        val color: Color = if (peligro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        OutlinedButton(
            onClick = onClick, modifier = m, enabled = habilitado, shape = forma,
            border = BorderStroke(1.5.dp, color),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
        ) { contenido() }
    } else {
        Button(onClick = onClick, modifier = m, enabled = habilitado, shape = forma) { contenido() }
    }
}
