package com.tecsup.fortnite.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.domain.model.Sesion
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.FormaInclinada
import com.tecsup.fortnite.ui.theme.TintaAm
import com.tecsup.fortnite.ui.theme.FondoCielo
import androidx.compose.foundation.shape.RoundedCornerShape
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.Rojo

@Composable
fun LoginScreen(
    estado: UiState<Sesion>?,
    onIngresar: (String, String) -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val cargando = estado is UiState.Cargando

    FondoCielo {
        // El Box centra el formulario; el Column se desplaza si no entra (pantalla girada)
        Box(Modifier.fillMaxSize().safeDrawingPadding(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .widthIn(max = 460.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    "TECSUP",
                    style = MaterialTheme.typography.displayMedium,
                    color = Blanco
                )
                Text(
                    "NITE",
                    style = MaterialTheme.typography.displayLarge,
                    color = Oro
                )
                Text(
                    "Mira la tienda de hoy y guarda tus favoritos",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Blanco
                )
                Spacer(Modifier.height(28.dp))

                Campo("Correo", correo, !cargando, KeyboardType.Email, false) { correo = it }
                Spacer(Modifier.height(10.dp))
                Campo("Contraseña", clave, !cargando, KeyboardType.Password, true) { clave = it }

                if (estado is UiState.Error) {
                    Text(
                        estado.mensaje,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Rojo)
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        color = Blanco,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onIngresar(correo, clave) },
                    enabled = !cargando,
                    shape = FormaInclinada(10.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    if (cargando) {
                        CircularProgressIndicator(Modifier.size(26.dp), color = TintaAm, strokeWidth = 3.dp)
                    } else {
                        Text("INGRESAR", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun Campo(
    etiqueta: String,
    valor: String,
    activo: Boolean,
    teclado: KeyboardType,
    oculto: Boolean,
    onCambio: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        singleLine = true,
        enabled = activo,
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        visualTransformation = if (oculto) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Noche,
            unfocusedContainerColor = Noche,
            disabledContainerColor = Noche,
            focusedTextColor = Blanco,
            unfocusedTextColor = Blanco,
            disabledTextColor = Blanco,
            cursorColor = Oro,
            focusedBorderColor = Oro,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            focusedLabelColor = Oro,
            unfocusedLabelColor = Blanco,
            disabledLabelColor = Blanco
        )
    )
}
