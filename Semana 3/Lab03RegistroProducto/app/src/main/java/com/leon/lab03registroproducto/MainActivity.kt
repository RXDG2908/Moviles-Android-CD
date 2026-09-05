package com.leon.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistro(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Pantalla del registro de producto
@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {
    // Estados de la pantalla
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Completa los datos y presiona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campos de ingreso
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (S/)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Boton que muestra el resumen
        Button(
            onClick = {
                // Se avisa exactamente que dato falta o esta mal escrito
                if (nombre.trim() == "") {
                    mensajeError = "Falta el nombre del producto"
                    mostrarResumen = false
                } else if (precio.trim() == "") {
                    mensajeError = "Falta el precio"
                    mostrarResumen = false
                } else if (cantidad.trim() == "") {
                    mensajeError = "Falta la cantidad"
                    mostrarResumen = false
                } else if (precio.toDoubleOrNull() == null) {
                    mensajeError = "El precio debe ser un numero"
                    mostrarResumen = false
                } else if (cantidad.toIntOrNull() == null) {
                    mensajeError = "La cantidad debe ser un numero entero"
                    mostrarResumen = false
                } else {
                    mensajeError = ""
                    mostrarResumen = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR PRODUCTO")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Boton que limpia el formulario
        Button(
            onClick = {
                nombre = ""
                precio = ""
                cantidad = ""
                mostrarResumen = false
                mensajeError = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LIMPIAR")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Mensaje de error de validacion
        if (mensajeError != "") {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        // Resumen del producto registrado
        if (mostrarResumen) {
            val precioNum = precio.toDoubleOrNull() ?: 0.0
            val cantidadNum = cantidad.toIntOrNull() ?: 0
            val importe = precioNum * cantidadNum

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(nombre, style = MaterialTheme.typography.titleLarge)
                    Text("Precio: S/ " + String.format("%.2f", precioNum))
                    Text("Cantidad: $cantidadNum")
                    Text(
                        text = "Importe: S/ " + String.format("%.2f", importe),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Mensaje de confirmacion
            Text(
                text = "✓ Producto registrado correctamente",
                color = Color(0xFF2E7D32)
            )
        }
    }
}
