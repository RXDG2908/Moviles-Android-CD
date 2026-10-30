package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import com.tecsup.huellitasvet.model.Distritos
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.VetCombo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.Campo
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Mejora: registro de dueños. Las validaciones están en SesionViewModel; la pantalla solo las muestra.
@Composable
fun RegistroScreen(vm: SesionViewModel, oscuro: Boolean, onRegistrado: () -> Unit, onAtras: () -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var distrito by rememberSaveable { mutableStateOf<String?>(null) }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    val errores by vm.errores.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val foco = LocalFocusManager.current
    val siguiente = { foco.moveFocus(FocusDirection.Down); Unit }

    val crear = {
        foco.clearFocus()
        vm.registrar(nombre, correo, telefono, distrito ?: "", clave, confirmar, onRegistrado)
    }
    val volver = { vm.limpiarError(); onAtras() }

    VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
        Scaffold(
            topBar = { AppTopBar("Crear cuenta", onAtras = volver) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text("Únete a HuellitasVet", style = MaterialTheme.typography.headlineSmall)
                    TextoGris("Registra tu cuenta de dueño para pedir citas y ver la ficha de tus mascotas")
                }

                VetTextField(
                    nombre, { nombre = it; vm.limpiarCampo(Campo.NOMBRE) }, "Nombre y apellido",
                    error = errores.nombre,
                    icono = Icons.Outlined.Person,
                    imeAccion = ImeAction.Next, onIme = siguiente
                )
                VetTextField(
                    correo, { correo = it; vm.limpiarCampo(Campo.CORREO) }, "Correo",
                    error = errores.correo,
                    teclado = KeyboardType.Email,
                    icono = Icons.Outlined.Email,
                    imeAccion = ImeAction.Next, onIme = siguiente
                )
                // Mejora: celular (solo dígitos, máximo 9) y distrito en combobox
                VetTextField(
                    telefono, { nuevo -> telefono = nuevo.filter { it.isDigit() }.take(9); vm.limpiarCampo(Campo.TELEFONO) }, "Celular",
                    error = errores.telefono,
                    teclado = KeyboardType.Phone,
                    icono = Icons.Outlined.Phone,
                    imeAccion = ImeAction.Next, onIme = { foco.clearFocus() }
                )
                VetCombo(
                    etiqueta = "Distrito",
                    opciones = Distritos.lima,
                    seleccion = distrito,
                    onSeleccion = { distrito = it; vm.limpiarCampo(Campo.DISTRITO) },
                    texto = { it },
                    error = errores.distrito,
                    icono = Icons.Outlined.LocationOn
                )
                VetTextField(
                    clave, { clave = it; vm.limpiarCampo(Campo.CLAVE) }, "Contraseña",
                    error = errores.clave,
                    esClave = true,
                    icono = Icons.Outlined.Lock,
                    imeAccion = ImeAction.Next, onIme = siguiente
                )
                VetTextField(
                    confirmar, { confirmar = it; vm.limpiarCampo(Campo.CONFIRMAR) }, "Repite la contraseña",
                    error = errores.confirmar,
                    esClave = true,
                    icono = Icons.Outlined.Lock,
                    imeAccion = ImeAction.Done, onIme = crear
                )

                InfoCaja(texto = "La cuenta es de dueño. Las cuentas de veterinario las crea la clínica.")

                VetButton(if (cargando) "Creando cuenta..." else "Crear cuenta", onClick = crear, habilitado = !cargando)

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextoGris("¿Ya tienes cuenta?")
                    TextButton(onClick = volver) { Text("Inicia sesión", fontWeight = FontWeight.SemiBold) }
                }
            }
        }
    }
}
