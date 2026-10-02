package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

// Barra superior morada que usan todas las pantallas.
// "subtitulo" es opcional; "navigationIcon" es el ícono de la izquierda (volver o menú).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    subtitulo: String? = null,
    navigationIcon: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(titulo, fontWeight = FontWeight.Bold)
                if (subtitulo != null) {
                    Text(subtitulo, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
