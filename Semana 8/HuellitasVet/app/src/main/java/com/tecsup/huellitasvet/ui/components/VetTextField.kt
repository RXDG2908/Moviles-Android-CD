package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Campo blanco con borde fino; el error sale debajo (junto al campo)
// esClave = true agrega el ojo para mostrar u ocultar la contraseña.
// imeAccion / onIme: botón del teclado (Siguiente o Listo) y qué hace al tocarlo.
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
    icono: ImageVector? = null,
    accion: (@Composable () -> Unit)? = null, // botón al final del campo (por ejemplo, limpiar)
    imeAccion: ImeAction = ImeAction.Default,
    onIme: (() -> Unit)? = null
) {
    val esquema = MaterialTheme.colorScheme
    var verClave by remember { mutableStateOf(false) } // vuelve a ocultarse al regresar a la pantalla
    val ojo: (@Composable () -> Unit)? = if (esClave) {
        {
            IconButton(onClick = { verClave = !verClave }) {
                Icon(
                    if (verClave) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = if (verClave) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = esquema.onSurfaceVariant
                )
            }
        }
    } else null

    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        leadingIcon = icono?.let { { Icon(it, contentDescription = null, tint = esquema.primary) } },
        trailingIcon = accion ?: ojo,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esClave) KeyboardType.Password else teclado,
            imeAction = imeAccion
        ),
        keyboardActions = KeyboardActions(onAny = { onIme?.invoke() ?: defaultKeyboardAction(imeAccion) }),
        visualTransformation = if (esClave && !verClave) PasswordVisualTransformation() else VisualTransformation.None,
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
