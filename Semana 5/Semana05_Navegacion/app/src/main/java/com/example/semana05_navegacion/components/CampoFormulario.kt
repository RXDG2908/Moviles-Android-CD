package com.example.semana05_navegacion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Campo de texto con ícono; si es contraseña, oculta el texto y permite verlo
@Composable
fun CampoFormulario(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    esContrasena: Boolean = false
) {
    var mostrar by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = {
            if (esContrasena) {
                Text(
                    text = if (mostrar) "Ocultar" else "Ver",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { mostrar = !mostrar }
                )
            }
        },
        visualTransformation = if (esContrasena && !mostrar) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}
