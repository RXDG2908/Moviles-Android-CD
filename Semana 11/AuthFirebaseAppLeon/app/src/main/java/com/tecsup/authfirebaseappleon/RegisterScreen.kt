package com.tecsup.authfirebaseappleon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(auth: FirebaseAuth, onRegistroOk: () -> Unit, onVolver: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    PantallaAuth {
        Text("Crear cuenta ✨", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        CampoTexto(email, { email = it }, "Correo electrónico")
        Spacer(Modifier.height(16.dp))
        CampoTexto(password, { password = it }, "Contraseña (mín. 6)", esPassword = true)
        Spacer(Modifier.height(16.dp))
        CampoTexto(confirmar, { confirmar = it }, "Confirmar contraseña", esPassword = true)
        MensajeError(error)
        Spacer(Modifier.height(24.dp))
        BotonPrincipal(if (cargando) "Registrando..." else "Registrarme", !cargando) {
            when {
                email.isBlank() || password.isBlank() -> error = "Completa todos los campos"
                password.length < 6 -> error = "La contraseña debe tener al menos 6 caracteres"
                password != confirmar -> error = "Las contraseñas no coinciden"
                else -> {
                    error = null
                    cargando = true
                    auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnCompleteListener { t ->
                            cargando = false
                            if (t.isSuccessful) onRegistroOk()
                            else error = t.exception?.localizedMessage ?: "Error al registrar"
                        }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onVolver) { Text("← Volver al login") }
    }
}
