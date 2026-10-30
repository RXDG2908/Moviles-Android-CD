package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Mejora: combobox (lista desplegable) con el mismo estilo que VetTextField.
// Se usa para el distrito, el servicio, el rol y el dueño.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> VetCombo(
    etiqueta: String,
    opciones: List<T>,
    seleccion: T?,
    onSeleccion: (T) -> Unit,
    texto: (T) -> String,
    modifier: Modifier = Modifier,
    error: String? = null,
    icono: ImageVector? = null,
    detalle: ((T) -> String)? = null,               // segunda línea de cada opción (precio, duración...)
    iconoOpcion: (@Composable (T) -> Unit)? = null, // ícono a la izquierda de cada opción
    vacio: String = "No hay opciones"
) {
    var abierto by remember { mutableStateOf(false) }
    val esquema = MaterialTheme.colorScheme

    ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { abierto = it }, modifier = modifier) {
        OutlinedTextField(
            value = seleccion?.let(texto) ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            leadingIcon = icono?.let { { Icon(it, contentDescription = null, tint = esquema.primary) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = esquema.surface,
                focusedContainerColor = esquema.surface,
                unfocusedBorderColor = esquema.outlineVariant
            ),
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false },
            containerColor = esquema.surface
        ) {
            if (opciones.isEmpty()) {
                DropdownMenuItem(text = { TextoGris(vacio) }, onClick = { abierto = false }, enabled = false)
            }
            opciones.forEach { opcion ->
                val elegida = opcion == seleccion
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                texto(opcion),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (elegida) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (elegida) esquema.primary else esquema.onSurface
                            )
                            detalle?.let { TextoGris(it(opcion)) }
                        }
                    },
                    leadingIcon = iconoOpcion?.let { { it(opcion) } },
                    trailingIcon = if (elegida) { { Icon(Icons.Filled.Check, contentDescription = null, tint = esquema.primary) } } else null,
                    onClick = { onSeleccion(opcion); abierto = false },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
