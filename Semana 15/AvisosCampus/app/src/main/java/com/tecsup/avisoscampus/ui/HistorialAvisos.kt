package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tecsup.avisoscampus.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía")
        } else {
            Column {
                avisos.take(20).forEach { aviso ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            aviso.origen.texto,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(aviso.hora, style = MaterialTheme.typography.labelMedium)
                    }
                    Text(aviso.titulo, style = MaterialTheme.typography.titleSmall)
                    Text(aviso.texto, style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider()
                }
            }
        }
    }
}
