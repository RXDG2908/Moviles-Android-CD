package com.leon.tecsupfit.screens

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// Chip reutilizable: relleno si está elegido, solo borde si no.
// No decide nada: solo avisa con onClick y la pantalla decide qué hacer.
@Composable
fun ChipOpcion(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    if (seleccionado) {
        Button(onClick = onClick) {
            Text(texto)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(texto)
        }
    }
}
