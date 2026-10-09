package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoFormulario
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Pantalla 2 - Registro: formulario con validaciones; agrega el usuario a la lista.
@Composable
fun RegistroScreen(navController: NavController) {
    // Estados de cada campo
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val teclado = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Título
        Column(Modifier.align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Crear cuenta", color = TextoOscuro, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Regístrate para agendar tus citas", color = TextoGris, fontSize = 13.sp)
        }
        Spacer(Modifier.height(4.dp))

        CampoFormulario("Nombre completo", nombre, { nombre = it }, Icons.Default.Person)
        CampoFormulario("Teléfono", telefono, { telefono = it }, Icons.Default.Call, KeyboardType.Phone)
        CampoFormulario("Correo (opcional)", correo, { correo = it }, Icons.Default.Email, KeyboardType.Email)
        CampoFormulario("Contraseña", contrasena, { contrasena = it }, Icons.Default.Lock, KeyboardType.Password, esContrasena = true)

        // Mensaje de error de las validaciones
        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        BotonPrincipal("Registrarme") {
            teclado?.hide()
            // Validaciones
            error = when {
                nombre.isBlank() || telefono.isBlank() || contrasena.isBlank() ->
                    "Completa nombre, teléfono y contraseña"
                telefono.length != 9 || !telefono.all { it.isDigit() } ->
                    "El teléfono debe tener 9 dígitos"
                correo.isNotBlank() && !correo.contains("@") -> "Ingresa un correo válido"
                contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                else -> ""
            }
            if (error.isEmpty()) {
                // add a la lista de usuarios (any evita teléfonos repetidos)
                val nuevo = Usuario(nombre.trim(), telefono, correo.trim(), contrasena)
                if (Repositorio.registrarUsuario(nuevo)) {
                    navController.navigate(Rutas.LOGIN)
                } else {
                    error = "Ya existe una cuenta con ese teléfono"
                }
            }
        }

        // Términos y condiciones
        Column(Modifier.align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Al registrarte aceptas nuestros", color = TextoGris, fontSize = 12.sp)
            Text(
                "Términos y Condiciones",
                color = AzulSalud,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.navigate(Rutas.TERMINOS) }
            )
        }

        // Ir a iniciar sesión
        Row(Modifier.align(Alignment.CenterHorizontally)) {
            Text("¿Ya tienes cuenta? ", color = TextoGris, fontSize = 13.sp)
            Text(
                "Iniciar sesión",
                color = AzulSalud,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.navigate(Rutas.LOGIN) }
            )
        }
    }
}
