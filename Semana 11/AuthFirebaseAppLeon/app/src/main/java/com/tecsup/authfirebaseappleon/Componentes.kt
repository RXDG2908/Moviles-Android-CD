package com.tecsup.authfirebaseappleon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/** Ancho máximo del contenido para que todo quede centrado y simétrico. */
val AnchoContenido = 420.dp

/** Barra superior del profesor: "Programación Móvil - Tecsup" (con el icono de Tecsup). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTecsup() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.tecsup_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Programación Móvil - Tecsup", style = MaterialTheme.typography.titleMedium)
            }
        }
    )
}

@Composable
fun LogoTecsup(modifier: Modifier = Modifier, ancho: Int = 180) {
    Image(
        painter = painterResource(R.drawable.logo_tecsup),
        contentDescription = "Logo Tecsup",
        contentScale = ContentScale.Fit,
        modifier = modifier.width(ancho.dp)
    )
}

@Composable
fun PantallaAuth(contenido: @Composable ColumnScope.() -> Unit) {
    Scaffold(topBar = { BarraTecsup() }) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = AnchoContenido)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoTecsup(ancho = 200)
                Spacer(Modifier.height(32.dp))
                contenido()
            }
        }
    }
}

@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    esPassword: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esPassword) KeyboardType.Password else KeyboardType.Email
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BotonPrincipal(texto: String, habilitado: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier.fillMaxWidth().height(52.dp)
    ) { Text(texto, style = MaterialTheme.typography.titleMedium) }
}

@Composable
fun MensajeError(mensaje: String?) {
    if (mensaje != null) {
        Spacer(Modifier.height(12.dp))
        Text(
            mensaje,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
