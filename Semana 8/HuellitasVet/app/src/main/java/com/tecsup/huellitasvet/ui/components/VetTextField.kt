package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Campo de texto con el error debajo (junto al campo)
@Composable
fun VetTextField(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false,
    lineas: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = lineas == 1,
        minLines = lineas,
        shape = RoundedCornerShape(14.dp)
    )
}
