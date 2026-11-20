// Pantalla principal: barra superior y las tres tarjetas de tareas.
package com.leon.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.leon.centrotareas.model.CargaUi
import com.leon.centrotareas.model.PrimosUi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val progreso by vm.progreso.collectAsStateWithLifecycle()
    val corriendo by vm.corriendo.collectAsStateWithLifecycle()
    val primos by vm.primos.collectAsStateWithLifecycle()
    val carga by vm.carga.collectAsStateWithLifecycle()
    val simularError by vm.simularError.collectAsStateWithLifecycle()
    var textoN by rememberSaveable { mutableStateOf("5000000") }
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaTarea("Tarea con progreso") {
                // Prueba de vida: si deja de sumar, la pantalla está congelada
                Button(onClick = { toques++ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Tócame: $toques")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { vm.tareaLarga() },
                        enabled = !corriendo,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Iniciar")
                    }
                    OutlinedButton(
                        onClick = { vm.cancelar() },
                        enabled = corriendo,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${(progreso * 100).toInt()} %")
                Text(mensaje)
            }
            TarjetaTarea("Contar números primos") {
                val calculando = primos is PrimosUi.Calculando
                OutlinedTextField(
                    value = textoN,
                    onValueChange = { textoN = it },
                    label = { Text("Contar primos hasta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            // Ignora textos que no son número o son menores que 2
                            val n = textoN.toIntOrNull()
                            if (n != null && n >= 2) vm.contar(n)
                        },
                        enabled = !calculando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Contar")
                    }
                    OutlinedButton(
                        onClick = { vm.cancelarPrimos() },
                        enabled = calculando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
                when (val estado = primos) {
                    PrimosUi.Inicial -> {}
                    is PrimosUi.Calculando -> {
                        // Barra con el avance real del cálculo
                        LinearProgressIndicator(
                            progress = { estado.porcentaje },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Calculando... ${(estado.porcentaje * 100).toInt()} %")
                    }
                    is PrimosUi.Listo -> Recuadro(
                        "${estado.cantidad} primos en ${estado.milisegundos} ms",
                        Exito
                    )
                    PrimosUi.Cancelado -> Text("Cancelado")
                }
            }
            TarjetaTarea("Dos cargas") {
                val cargando = carga == CargaUi.Cargando
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { vm.cargarUnaPorUna() },
                        enabled = !cargando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Una por una")
                    }
                    Button(
                        onClick = { vm.cargarALaVez() },
                        enabled = !cargando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("A la vez")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = simularError,
                        onCheckedChange = { vm.cambiarSimularError(it) }
                    )
                    Text("Simular error", modifier = Modifier.padding(start = 8.dp))
                }
                when (val estado = carga) {
                    CargaUi.Inicial -> {}
                    CargaUi.Cargando -> CircularProgressIndicator()
                    is CargaUi.Exito -> Recuadro(
                        "Modo: ${estado.modo}\nPerfil: ${estado.perfil}\n" +
                            "Pedidos: ${estado.pedidos}\nTiempo: ${estado.milisegundos} ms",
                        Exito
                    )
                    is CargaUi.Error -> Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                estado.mensaje,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            // Vuelve a intentar la carga A la vez
                            Button(onClick = { vm.cargarALaVez() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Verde para resultados correctos
private val Exito = Color(0xFFC8E6C9)

// Recuadro de color con un texto: resultado o error
@Composable
fun Recuadro(texto: String, fondo: Color) {
    Surface(color = fondo, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth()) {
        Text(texto, modifier = Modifier.padding(12.dp), color = Color.Black)
    }
}

// Card con el título en azul; el contenido lo pone cada tarea
@Composable
fun TarjetaTarea(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}
