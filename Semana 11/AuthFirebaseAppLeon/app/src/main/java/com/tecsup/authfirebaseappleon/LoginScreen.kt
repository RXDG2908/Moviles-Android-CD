package com.tecsup.authfirebaseappleon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(auth: FirebaseAuth, onLoginOk: () -> Unit, onIrRegistro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    PantallaAuth {
        Text("Iniciar Sesión 🔐", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        CampoTexto(email, { email = it }, "Correo electrónico")
        Spacer(Modifier.height(16.dp))
        CampoTexto(password, { password = it }, "Contraseña", esPassword = true)
        MensajeError(error)
        Spacer(Modifier.height(24.dp))
        BotonPrincipal(if (cargando) "Ingresando..." else "Ingresar", !cargando) {
            if (email.isBlank() || password.isBlank()) {
                error = "Completa todos los campos"
                return@BotonPrincipal
            }
            error = null
            cargando = true
            auth.signInWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener { t ->
                    cargando = false
                    if (t.isSuccessful) onLoginOk()
                    else error = t.exception?.localizedMessage ?: "Error al iniciar sesión"
                }
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onIrRegistro) { Text("¿No tienes cuenta? Regístrate aquí") }
    }
}
