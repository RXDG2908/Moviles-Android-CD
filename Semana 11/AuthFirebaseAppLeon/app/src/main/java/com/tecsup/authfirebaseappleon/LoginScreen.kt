package com.tecsup.authfirebaseappleon

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

// Pantalla de Inicio de Sesión con Firebase Authentication (Email/Password)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // Instancia de Firebase Auth y contexto (necesario para mostrar Toasts)
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    // Estados de los campos y del botón (se recomponen al cambiar)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ---------- TÍTULO SUPERIOR ----------
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Programación Móvil - Tecsup",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- IMAGEN TECSUP ----------
            Image(
                painter = painterResource(id = R.drawable.tecsup),
                contentDescription = "Logo Tecsup",
                modifier = Modifier
                    .size(120.dp) // Ajusta el tamaño si quieres más grande o más pequeño
                    .align(Alignment.CenterHorizontally)
            )
        }

        // ---------- CONTENIDO PRINCIPAL ----------
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título y campos del formulario
            Text("Iniciar Sesión 🔐", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botón Ingresar: valida los campos y llama a Firebase para iniciar sesión
            Button(
                onClick = {
                    // Validación: no permite campos vacíos
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Firebase valida correo y contraseña; el resultado llega en addOnCompleteListener
                    isLoading = true
                    auth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Inicio exitoso ✅", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoading) "Cargando..." else "Ingresar")
            }

            // Enlace para ir a la pantalla de registro
            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }

        // ---------- PIE DE PÁGINA ----------
        Text(
            text = "Renzo León - Tecsup",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}
