package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoFormulario
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Pantalla 8 - Iniciar sesión (vista faltante, mismo estilo que Registro):
// busca al usuario en la lista (find) y guarda la sesión.
@Composable
fun LoginScreen(navController: NavController) {
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Iniciar sesión") { navController.popBackStack() } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(Modifier.align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Bienvenido de nuevo", color = TextoOscuro, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Ingresa para gestionar tus citas", color = TextoGris, fontSize = 13.sp)
            }
            Spacer(Modifier.height(4.dp))

            CampoFormulario("Teléfono", telefono, { telefono = it }, Icons.Default.Call, KeyboardType.Phone)
            CampoFormulario("Contraseña", contrasena, { contrasena = it }, Icons.Default.Lock, KeyboardType.Password, esContrasena = true)

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            BotonPrincipal("Iniciar sesión") {
                if (telefono.isBlank() || contrasena.isBlank()) {
                    error = "Ingresa tu teléfono y contraseña"
                } else if (Repositorio.iniciarSesion(telefono, contrasena)) {
                    // Entra al Inicio y borra Splash/Registro/Login del historial
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                } else {
                    error = "Teléfono o contraseña incorrectos"
                }
            }

            Row(Modifier.align(Alignment.CenterHorizontally)) {
                Text("¿No tienes cuenta? ", color = TextoGris, fontSize = 13.sp)
                Text(
                    "Regístrate",
                    color = AzulSalud,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.navigate(Rutas.REGISTRO) }
                )
            }
        }
    }
}
