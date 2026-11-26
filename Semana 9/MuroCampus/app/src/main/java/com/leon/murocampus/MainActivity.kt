// Actividad principal: crea el ViewModel y decide qué pantalla se ve.
package com.leon.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.viewmodel.UiState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.leon.murocampus.ui.PantallaCrear
import com.leon.murocampus.ui.PantallaDetalle
import com.leon.murocampus.ui.PantallaLista
import com.leon.murocampus.ui.theme.MuroCampusTheme
import com.leon.murocampus.viewmodel.PostsViewModel
import com.leon.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels { PostsViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuroCampusTheme {
                AppMuro(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMuro(vm: PostsViewModel) {
    // null = se ve la lista; un número = se ve el detalle de esa publicación
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    // true = se ve el formulario para crear
    var creando by rememberSaveable { mutableStateOf(false) }

    // El botón Atrás del sistema vuelve a la lista sin recargarla
    BackHandler(enabled = idSeleccionado != null || creando) {
        if (creando) vm.reiniciarCreacion()
        idSeleccionado = null
        creando = false
    }

    // Resultado del borrado: vuelve a la lista y avisa con un Snackbar
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    val avisos = remember { SnackbarHostState() }
    // El aviso va en su propio alcance: así no se corta al limpiar el estado
    val alcance = rememberCoroutineScope()
    LaunchedEffect(borrado) {
        when (val b = borrado) {
            is UiState.Exito -> {
                idSeleccionado = null
                alcance.launch { avisos.showSnackbar("Publicación ${b.datos} eliminada") }
                vm.borradoMostrado()
            }
            is UiState.Error -> {
                alcance.launch { avisos.showSnackbar("No se pudo eliminar: ${b.mensaje}") }
                vm.borradoMostrado()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(avisos) },
        floatingActionButton = {
            // El botón + solo aparece en la lista
            if (idSeleccionado == null && !creando) {
                FloatingActionButton(onClick = { creando = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Nueva publicación")
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("MuroCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { relleno ->
        Box(modifier = Modifier.fillMaxSize().padding(relleno)) {
            val id = idSeleccionado
            when {
                creando -> PantallaCrear(vm)
                id == null -> PantallaLista(vm, onAbrir = {
                    idSeleccionado = it
                    vm.abrir(it)
                })
                else -> PantallaDetalle(vm, id)
            }
        }
    }
}
