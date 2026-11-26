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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    // El botón Atrás del sistema vuelve a la lista sin recargarla
    BackHandler(enabled = idSeleccionado != null) { idSeleccionado = null }

    Scaffold(
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
            if (id == null) {
                PantallaLista(vm, onAbrir = {
                    idSeleccionado = it
                    vm.abrir(it)
                })
            } else {
                PantallaDetalle(vm, id)
            }
        }
    }
}
