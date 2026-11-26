// Piezas visuales que comparten las pantallas: tarjeta, avatar, carga y error.
package com.leon.murocampus.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Un color fijo por usuario, para reconocer de un vistazo quién publicó
private val ColoresUsuario = listOf(
    Color(0xFF1565C0), Color(0xFF2E7D32), Color(0xFFC62828), Color(0xFF6A1B9A),
    Color(0xFFEF6C00), Color(0xFF00838F), Color(0xFFAD1457), Color(0xFF4E342E),
    Color(0xFF283593), Color(0xFF558B2F)
)

fun colorDeUsuario(usuario: Int): Color = ColoresUsuario[(usuario - 1).mod(ColoresUsuario.size)]

// Círculo de color con el número del usuario
@Composable
fun Avatar(usuario: Int, tamano: Dp = 40.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(colorDeUsuario(usuario)),
        contentAlignment = Alignment.Center
    ) {
        Text("U$usuario", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
    }
}

// Tarjeta de una publicación: avatar, usuario, título en negrita y el texto recortado
@Composable
fun TarjetaPost(
    usuario: Int,
    titulo: String,
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxLineas: Int = 2
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Avatar(usuario)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Usuario $usuario",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorDeUsuario(usuario)
                )
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    texto,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = maxLineas,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Indicador de carga centrado; el texto late suavemente mientras espera
@Composable
fun Cargando(texto: String) {
    val latido = rememberInfiniteTransition(label = "latido")
    val opacidad by latido.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "opacidad"
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text(texto, modifier = Modifier.alpha(opacidad))
    }
}

// Recuadro de error con icono, el mensaje y el botón Reintentar
@Composable
fun ErrorConReintento(mensaje: String, onReintentar: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.onErrorContainer)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onReintentar) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Text("Reintentar", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
