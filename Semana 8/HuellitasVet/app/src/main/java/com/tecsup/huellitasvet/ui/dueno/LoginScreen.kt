package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Un solo login para los dos roles
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: SesionViewModel, oscuro: Boolean, onEntrar: (Rol) -> Unit) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val error by vm.error.collectAsState()

    // El color de la pantalla cambia según la pestaña elegida
    VetTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MascotaAvatar(if (rol == Rol.CLIENTE) Especie.PERRO else Especie.GATO, tamano = 96.dp)
                Text("HuellitasVet", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Text("Ingresa a tu cuenta", style = MaterialTheme.typography.bodyMedium)

                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    Rol.entries.forEachIndexed { i, r ->
                        SegmentedButton(
                            selected = rol == r,
                            onClick = { rol = r; vm.limpiarError() },
                            shape = SegmentedButtonDefaults.itemShape(i, Rol.entries.size)
                        ) { Text(r.etiqueta) }
                    }
                }

                VetTextField(correo, { correo = it }, "Correo", teclado = KeyboardType.Email)
                VetTextField(clave, { clave = it }, "Contraseña", esClave = true)

                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                VetButton("Ingresar", onClick = { vm.login(correo, clave, rol, onEntrar) })
            }
        }
    }
}
