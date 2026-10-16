package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Campo blanco con borde fino; el error sale debajo (junto al campo)
@Composable
fun VetTextField(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false,
    lineas: Int = 1,
    icono: ImageVector? = null
) {
    val esquema = MaterialTheme.colorScheme
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        leadingIcon = icono?.let { { Icon(it, contentDescription = null, tint = esquema.primary) } },
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = lineas == 1,
        minLines = lineas,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = esquema.surface,
            focusedContainerColor = esquema.surface,
            unfocusedBorderColor = esquema.outlineVariant
        )
    )
}
