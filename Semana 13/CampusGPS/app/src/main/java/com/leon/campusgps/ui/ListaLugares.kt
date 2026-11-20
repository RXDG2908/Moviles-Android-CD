// Lista de lugares con su distancia; la tarjeta seleccionada se resalta.
package com.leon.campusgps.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.campusgps.model.LugarConDistancia
import com.leon.campusgps.util.formatearDistancia

// Mismos colores que los marcadores del mapa (HUE_AZURE y HUE_GREEN)
private val AzulMarcador = Color(0xFF1E88E5)
private val VerdeMarcador = Color(0xFF2E7D32)

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    dentroDelRadio: Set<Int>,
    masCercanoId: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estadoLista = rememberLazyListState()

    // Si cambia el más cercano, la lista vuelve arriba para que se vea primero
    LaunchedEffect(lugares.firstOrNull()?.lugar?.id) {
        estadoLista.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = estadoLista,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            TarjetaLugar(
                item = item,
                elegida = item.lugar.id == seleccionado,
                dentro = item.lugar.id in dentroDelRadio,
                esMasCercano = item.lugar.id == masCercanoId,
                onClick = { onClick(item.lugar.id) },
                // Cuando cambia el orden, cada tarjeta se desliza a su nuevo lugar
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun TarjetaLugar(
    item: LugarConDistancia,
    elegida: Boolean,
    dentro: Boolean,
    esMasCercano: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vibracion = LocalHapticFeedback.current
    val interaccion = remember { MutableInteractionSource() }
    val escala = escalaAlPresionar(interaccion)

    // Los colores y el borde cambian con una transición corta en vez de saltar
    val fondo by animateColorAsState(
        targetValue = if (elegida) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        animationSpec = tween(250),
        label = "fondo"
    )
    val grosorBorde by animateDpAsState(if (elegida) 2.dp else 0.dp, tween(250), label = "borde")
    val colorAcento = if (esMasCercano) VerdeMarcador else AzulMarcador

    Card(
        onClick = {
            vibracion.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        interactionSource = interaccion,
        modifier = modifier
            .fillMaxWidth()
            .scale(escala),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = fondo),
        border = if (grosorBorde > 0.dp) {
            BorderStroke(grosorBorde, MaterialTheme.colorScheme.primary)
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de la categoría en un círculo del color de su marcador
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colorAcento.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoDeCategoria(item.lugar.categoria),
                    contentDescription = item.lugar.categoria,
                    tint = colorAcento,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.lugar.nombre, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.lugar.categoria,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    // La etiqueta del radio aparece y desaparece con una animación
                    AnimatedVisibility(
                        visible = dentro,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        EtiquetaRadio()
                    }
                }
            }
            // La distancia cambia deslizando el número viejo hacia abajo
            AnimatedContent(
                targetState = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                transitionSpec = {
                    (slideInVertically { -it } + fadeIn()) togetherWith
                        (slideOutVertically { it } + fadeOut())
                },
                label = "distancia"
            ) { texto ->
                Text(
                    text = texto,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (esMasCercano) VerdeMarcador else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (esMasCercano) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun EtiquetaRadio() {
    Row(
        modifier = Modifier
            .padding(start = 6.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 6.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Radar,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(3.dp))
        Text(
            "Dentro del radio",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
