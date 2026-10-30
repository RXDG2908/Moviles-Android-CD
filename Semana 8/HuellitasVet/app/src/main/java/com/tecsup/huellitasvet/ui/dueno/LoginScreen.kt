package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.SelectorPestanas
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.Campo
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Un solo login para los dos roles
// Mejora: logo, errores por campo, ojo en la contraseña, teclado con Siguiente/Listo,
// botón bloqueado mientras consulta y enlace al registro (solo dueños).
@Composable
fun LoginScreen(vm: SesionViewModel, oscuro: Boolean, onEntrar: (Rol) -> Unit, onRegistrarse: () -> Unit) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    // Si viene del registro, el correo ya aparece escrito
    var correo by remember { mutableStateOf(vm.correoSugerido.value) }
    var clave by remember { mutableStateOf("") }
    val error by vm.error.collectAsState()
    val errores by vm.errores.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val foco = LocalFocusManager.current

    val ingresar = {
        foco.clearFocus()
        vm.login(correo, clave, rol, onEntrar)
    }

    // El color de la pantalla cambia según la pestaña elegida
    VetTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(
                        when (rol) {
                            Rol.CLIENTE -> Especie.PERRO
                            Rol.ADMIN -> Especie.GATO
                            Rol.ADMINISTRADOR -> Especie.OTRO
                        },
                        tamano = 56.dp
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "HuellitasVet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
                    TextoGris(
                        when (rol) {
                            Rol.CLIENTE -> "Ingresa para cuidar a tus mascotas"
                            Rol.ADMIN -> "Ingresa al panel de la clínica"
                            Rol.ADMINISTRADOR -> "Control total de usuarios, mascotas y servicios"
                        }
                    )
                }

                SelectorPestanas(
                    opciones = Rol.entries.map { it.etiqueta },
                    seleccionada = rol.ordinal,
                    onSeleccion = { rol = Rol.entries[it]; vm.limpiarError() },
                    relleno = true
                )

                mensaje?.let { InfoCaja(texto = it, icono = Icons.Outlined.CheckCircle) }

                VetTextField(
                    correo, { correo = it; vm.limpiarCampo(Campo.CORREO) }, "Correo",
                    error = errores.correo,
                    teclado = KeyboardType.Email,
                    icono = Icons.Outlined.Email,
                    imeAccion = ImeAction.Next,
                    onIme = { foco.moveFocus(FocusDirection.Down) }
                )
                VetTextField(
                    clave, { clave = it; vm.limpiarCampo(Campo.CLAVE) }, "Contraseña",
                    error = errores.clave,
                    esClave = true,
                    icono = Icons.Outlined.Lock,
                    imeAccion = ImeAction.Done,
                    onIme = ingresar
                )

                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                VetButton(if (cargando) "Ingresando..." else "Ingresar", onClick = ingresar, habilitado = !cargando)

                if (rol == Rol.CLIENTE) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextoGris("¿No tienes cuenta?")
                        TextButton(onClick = { vm.limpiarError(); onRegistrarse() }) {
                            Text("Regístrate", fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else if (rol == Rol.ADMIN) {
                    InfoCaja(
                        titulo = "Modo práctica",
                        texto = "Puedes cambiar de perfil desde Ajustes para probar ambos roles."
                    )
                } else {
                    InfoCaja(
                        titulo = "Administrador",
                        texto = "Crea y elimina dueños, veterinarios, mascotas y servicios. Las cuentas de administrador solo las crea otro administrador."
                    )
                }
            }
        }
    }
}
