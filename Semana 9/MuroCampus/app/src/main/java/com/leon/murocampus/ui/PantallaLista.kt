// Pantalla de la lista: dibuja el estado que le da el ViewModel.
package com.leon.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.viewmodel.PostsViewModel
import com.leon.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Cargando("Cargando publicaciones…")
        is UiState.Error -> ErrorConReintento(e.mensaje) { vm.cargar() }
        is UiState.Exito -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(e.datos, key = { it.id }) { post ->
                TarjetaPost(
                    usuario = post.userId,
                    titulo = post.title,
                    texto = post.body,
                    onClick = { onAbrir(post.id) }
                )
            }
        }
    }
}
