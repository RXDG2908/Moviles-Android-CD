package com.tecsup.fortnite.ui.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.domain.model.Sesion
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.Borde
import com.tecsup.fortnite.ui.theme.CelesteTexto
import com.tecsup.fortnite.ui.theme.FondoCielo
import com.tecsup.fortnite.ui.theme.FormaInclinada
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.Rojo
import com.tecsup.fortnite.ui.theme.SombraPanel
import com.tecsup.fortnite.ui.theme.TintaAm

// Rosa claro para el texto de error: se lee bien sobre el panel azul
private val TextoError = Color(0xFFFFB8D2)

@Composable
fun LoginScreen(
    estado: UiState<Sesion>?,
    errores: ErroresCampos,
    onIngresar: (String, String) -> Unit,
    onEditarCorreo: () -> Unit,
    onEditarClave: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var verClave by rememberSaveable { mutableStateOf(false) }
    val cargando = estado is UiState.Cargando
    val foco = LocalFocusManager.current

    FondoCielo {
        Column(Modifier.fillMaxSize().safeDrawingPadding()) {
            Cinta()
            // El Box centra el formulario; el Column se desplaza si no entra (pantalla girada)
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    Modifier
                        .widthIn(max = 460.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    // Marca
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("TECSUP", style = MaterialTheme.typography.displayMedium, color = Blanco)
                        Text("NITE", style = MaterialTheme.typography.displayMedium, color = Oro)
                    }
                    Text(
                        "La tienda de Fortnite de hoy, con tus favoritos guardados en el celular.",
                        modifier = Modifier.padding(top = 2.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Blanco
                    )
                    Spacer(Modifier.height(22.dp))

                    // Panel del formulario, como los paneles de la web
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(SombraPanel)
                            .border(1.dp, Borde.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(18.dp)
                    ) {
                        Text("INGRESA", style = MaterialTheme.typography.headlineLarge, color = Blanco)
                        Text(
                            "Usa la cuenta que te dio el administrador.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CelesteTexto
                        )
                        Spacer(Modifier.height(16.dp))

                        Campo(
                            etiqueta = "Correo",
                            valor = correo,
                            onCambio = { correo = it; onEditarCorreo() },
                            icono = Icons.Outlined.Mail,
                            error = errores.correo,
                            activo = !cargando,
                            teclado = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            acciones = KeyboardActions(onNext = { foco.moveFocus(FocusDirection.Down) })
                        )
                        Spacer(Modifier.height(12.dp))
                        Campo(
                            etiqueta = "Contraseña",
                            valor = clave,
                            onCambio = { clave = it; onEditarClave() },
                            icono = Icons.Outlined.Lock,
                            error = errores.clave,
                            activo = !cargando,
                            teclado = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            acciones = KeyboardActions(onDone = { foco.clearFocus(); onIngresar(correo, clave) }),
                            oculto = !verClave,
                            final = {
                                IconButton(onClick = { verClave = !verClave }) {
                                    Icon(
                                        if (verClave) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                        contentDescription = if (verClave) "Ocultar contraseña" else "Mostrar contraseña",
                                        tint = CelesteTexto
                                    )
                                }
                            }
                        )

                        // Error que manda Firebase (no es de un campo en particular)
                        if (estado is UiState.Error) {
                            Row(
                                Modifier
                                    .padding(top = 14.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Rojo)
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    if (estado.mensaje.contains("conexión")) Icons.Outlined.WifiOff else Icons.Outlined.ErrorOutline,
                                    contentDescription = null,
                                    tint = Blanco
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(estado.mensaje, style = MaterialTheme.typography.bodyMedium, color = Blanco)
                            }
                        }

                        Spacer(Modifier.height(18.dp))
                        Button(
                            onClick = { foco.clearFocus(); onIngresar(correo, clave) },
                            enabled = !cargando,
                            shape = FormaInclinada(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Oro,
                                contentColor = TintaAm,
                                disabledContainerColor = Oro.copy(alpha = 0.75f),
                                disabledContentColor = TintaAm
                            ),
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            if (cargando) {
                                CircularProgressIndicator(Modifier.size(22.dp), color = TintaAm, strokeWidth = 3.dp)
                                Spacer(Modifier.width(10.dp))
                                Text("INGRESANDO…", style = MaterialTheme.typography.titleMedium)
                            } else {
                                Text("INGRESAR", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }

                    Text(
                        "¿No tienes cuenta? Pídela al administrador de la tienda.",
                        modifier = Modifier.padding(top = 14.dp).fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Blanco
                    )
                }
            }
        }
    }
}

// Campo con la etiqueta arriba (nunca solo como texto de ejemplo) y su error debajo
@Composable
private fun Campo(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    icono: ImageVector,
    error: String?,
    activo: Boolean,
    teclado: KeyboardOptions,
    acciones: KeyboardActions,
    oculto: Boolean = false,
    final: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            etiqueta,
            modifier = Modifier.padding(bottom = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Blanco
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onCambio,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { if (error != null) error(error) },
            singleLine = true,
            enabled = activo,
            isError = error != null,
            leadingIcon = { Icon(icono, contentDescription = null, tint = CelesteTexto) },
            trailingIcon = final,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = teclado,
            keyboardActions = acciones,
            visualTransformation = if (oculto) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SombraPanel,
                unfocusedContainerColor = SombraPanel,
                disabledContainerColor = SombraPanel,
                errorContainerColor = SombraPanel,
                focusedTextColor = Blanco,
                unfocusedTextColor = Blanco,
                disabledTextColor = Blanco.copy(alpha = 0.7f),
                errorTextColor = Blanco,
                cursorColor = Oro,
                errorCursorColor = Oro,
                focusedBorderColor = Oro,
                unfocusedBorderColor = Borde.copy(alpha = 0.5f),
                disabledBorderColor = Borde.copy(alpha = 0.3f),
                errorBorderColor = TextoError
            )
        )
        if (error != null) {
            Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ErrorOutline, contentDescription = null, tint = TextoError, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(error, style = MaterialTheme.typography.bodySmall, color = TextoError)
            }
        }
    }
}

// Cinta amarilla en movimiento, como la de la web (la única de la app)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Cinta() {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Oro)
            .padding(vertical = 8.dp)
    ) {
        // La frase va una sola vez; basicMarquee la repite con su propio espacio
        Text(
            "TIENDA DE HOY  •  FAVORITOS  •  FUNCIONA SIN INTERNET  •",
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE,
                spacing = MarqueeSpacing(28.dp)
            ),
            style = MaterialTheme.typography.labelLarge,
            color = TintaAm,
            maxLines = 1
        )
    }
}
