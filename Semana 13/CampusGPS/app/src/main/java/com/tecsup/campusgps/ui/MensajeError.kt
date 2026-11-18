// Recuadro de error con el mensaje y el botón que corresponde a cada caso.
package com.tecsup.campusgps.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.PhonelinkErase
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sacudida corta cuando aparece un error nuevo, junto con una vibración
    val desplazamiento = remember { Animatable(0f) }
    val vibracion = LocalHapticFeedback.current
    LaunchedEffect(error) {
        vibracion.performHapticFeedback(HapticFeedbackType.LongPress)
        desplazamiento.animateTo(
            targetValue = 0f,
            animationSpec = keyframes {
                durationMillis = 400
                -14f at 60
                12f at 130
                -8f at 200
                5f at 270
                0f at 400
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { translationX = desplazamiento.value },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconoDeError(error.tipo),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(error.mensaje, fontWeight = FontWeight.Medium)

                // Sin Play Services no hay botón: reintentar no lo arregla
                when (error.tipo) {
                    TipoError.SIN_PERMISO -> BotonError("Abrir ajustes", Icons.Filled.Settings, onAbrirAjustes)
                    TipoError.GPS_APAGADO, TipoError.OTRO ->
                        BotonError("Reintentar", Icons.Filled.Refresh, onReintentar)
                    TipoError.SIN_PLAY_SERVICES -> Unit
                }
            }
        }
    }
}

@Composable
private fun BotonError(texto: String, icono: ImageVector, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        Text(texto)
    }
}

private fun iconoDeError(tipo: TipoError): ImageVector = when (tipo) {
    TipoError.SIN_PERMISO -> Icons.Filled.LocationOff
    TipoError.GPS_APAGADO -> Icons.Filled.GpsOff
    TipoError.SIN_PLAY_SERVICES -> Icons.Filled.PhonelinkErase
    TipoError.OTRO -> Icons.Filled.ErrorOutline
}
