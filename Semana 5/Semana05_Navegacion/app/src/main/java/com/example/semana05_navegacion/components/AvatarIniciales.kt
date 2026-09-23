package com.example.semana05_navegacion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Círculo con las iniciales del nombre (reemplaza a la foto)
@Composable
fun AvatarIniciales(nombre: String, tamano: Dp, conBorde: Boolean = false) {
    val iniciales = nombre.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    var modifier = Modifier
        .size(tamano)
        .clip(CircleShape)
    if (conBorde) {
        modifier = modifier.border(4.dp, Color.White, CircleShape)
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value * 0.36f).sp
        )
    }
}
