package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita

// Tarjeta de cita del mockup:
// [inicio: ícono del servicio o avatar]  Título
//                                         detalle gris
//                                         [Estado]            [acciones]
@Composable
fun CitaCard(
    titulo: String,
    detalle: String,
    estado: EstadoCita,
    modifier: Modifier = Modifier,
    detalle2: String? = null,
    onClick: (() -> Unit)? = null,
    accionesEnLinea: Boolean = true, // false: las acciones van en su propia fila, a la derecha
    inicio: @Composable () -> Unit = {},
    acciones: @Composable RowScope.() -> Unit = {}
) {
    Tarjeta(modifier, onClick = onClick) {
        Row(Modifier.padding(14.dp)) {
            inicio()
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(titulo, style = MaterialTheme.typography.titleSmall)
                TextoGris(detalle)
                detalle2?.let { TextoGris(it) }
                Spacer(Modifier.height(6.dp))
                if (accionesEnLinea) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        EstadoCitaChip(estado)
                        Spacer(Modifier.weight(1f))
                        acciones()
                    }
                } else {
                    EstadoCitaChip(estado)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        content = acciones
                    )
                }
            }
        }
    }
}
