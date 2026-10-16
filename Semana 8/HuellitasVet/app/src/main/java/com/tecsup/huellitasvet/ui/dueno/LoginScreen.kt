package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.SelectorPestanas
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Un solo login para los dos roles
@Composable
fun LoginScreen(vm: SesionViewModel, oscuro: Boolean, onEntrar: (Rol) -> Unit) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val error by vm.error.collectAsState()

    // El color de la pantalla cambia según la pestaña elegida
    VetTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Spacer(Modifier.height(32.dp))
                Column {
                    Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
                    TextoGris("Ingresa para continuar")
                }

                SelectorPestanas(
                    opciones = Rol.entries.map { it.etiqueta },
                    seleccionada = rol.ordinal,
                    onSeleccion = { rol = Rol.entries[it]; vm.limpiarError() },
                    relleno = true
                )

                VetTextField(correo, { correo = it }, "Correo", teclado = KeyboardType.Email, icono = Icons.Outlined.Email)
                VetTextField(clave, { clave = it }, "Contraseña", esClave = true, icono = Icons.Outlined.Lock)

                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                VetButton("Ingresar", onClick = { vm.login(correo, clave, rol, onEntrar) })

                if (rol == Rol.ADMIN) {
                    InfoCaja(
                        titulo = "Modo práctica",
                        texto = "Puedes cambiar de perfil desde Ajustes para probar ambos roles."
                    )
                }
            }
        }
    }
}
