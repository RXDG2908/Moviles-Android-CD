package com.leon.lab03registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.lab03registronotas.ui.theme.Lab03RegistroNotasTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroNotasTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Pantalla del registro de notas
@Composable
fun PantallaNotas(modifier: Modifier = Modifier) {
    // Una nota por curso, el Slider trabaja con Float
    var notaFundamentos by remember { mutableStateOf(0f) }
    var notaPoo by remember { mutableStateOf(0f) }
    var notaMoviles by remember { mutableStateOf(0f) }
    var notaBaseDatos by remember { mutableStateOf(0f) }

    // Opciones y control del calculo
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Registro de Notas",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Asigna las notas de tus cursos y calcula tu promedio",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Fundamentos de Programacion
        Text(
            text = "Fundamentos de Programacion (20%)",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = notaFundamentos,
                onValueChange = { notaFundamentos = it },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = notaFundamentos.toInt().toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Programacion Orientada a Objetos
        Text(
            text = "Programacion Orientada a Objetos (25%)",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = notaPoo,
                onValueChange = { notaPoo = it },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = notaPoo.toInt().toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Programacion en Moviles
        Text(
            text = "Programacion en Moviles (30%)",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = notaMoviles,
                onValueChange = { notaMoviles = it },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = notaMoviles.toInt().toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Base de Datos
        Text(
            text = "Base de Datos (25%)",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = notaBaseDatos,
                onValueChange = { notaBaseDatos = it },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = notaBaseDatos.toInt().toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Opcion de redondeo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Redondear promedio final",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Confirmacion de las notas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Confirmo que las notas son correctas",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // El boton solo funciona si el checkbox esta marcado
        Button(
            onClick = { mostrarResultado = true },
            enabled = confirmado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CALCULAR PROMEDIO")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Mensaje mientras no se calcula
        if (!mostrarResultado) {
            Text(
                text = "Asigna las notas y confirma para calcular",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Tarjeta con el resultado
        if (mostrarResultado) {
            // Promedio ponderado con los pesos de cada curso
            val ponderado = notaFundamentos * 0.20 + notaPoo * 0.25 +
                    notaMoviles * 0.30 + notaBaseDatos * 0.25

            // Si el Switch esta activo se redondea al entero mas cercano
            val promedioFinal = if (redondear) {
                ponderado.roundToInt().toDouble()
            } else {
                ponderado
            }

            // Observacion segun el promedio final
            val observacion = when {
                promedioFinal >= 17 -> "EXCELENTE"
                promedioFinal >= 13 -> "APROBADO"
                promedioFinal >= 10 -> "EN RECUPERACION"
                else -> "DESAPROBADO"
            }
            val colorObservacion = when {
                promedioFinal >= 17 -> Color(0xFF1B5E20)
                promedioFinal >= 13 -> Color(0xFF2E7D32)
                promedioFinal >= 10 -> Color(0xFFF9A825)
                else -> Color(0xFFC62828)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Resultado",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("Promedio ponderado: " + String.format("%.2f", ponderado))
                    if (redondear) {
                        Text(
                            text = "Promedio final: " + promedioFinal.toInt() +
                                    " (redondeado)",
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Promedio final: " +
                                    String.format("%.2f", promedioFinal),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = observacion,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorObservacion
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Mensaje de confirmacion
            Text(
                text = "✓ Promedio calculado correctamente",
                color = Color(0xFF2E7D32)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Pie de la pantalla
        Text(
            text = "Desarrollado por: Renzo Raul Leon Fernandez",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
