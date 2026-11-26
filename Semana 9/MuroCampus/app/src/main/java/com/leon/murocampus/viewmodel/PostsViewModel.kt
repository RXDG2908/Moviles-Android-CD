// ViewModel del muro: pide los datos al Repository y los publica como UiState.
package com.leon.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.model.Comentario
import com.leon.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Lo que muestra el detalle: la publicación y sus comentarios
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ----- Lista de publicaciones
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // Se carga una sola vez al crearse; al rotar, el ViewModel conserva la lista
    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // ----- Detalle de una publicación
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    // Pide el post y sus comentarios a la vez; si una falla, todo el detalle es Error
    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val (post, comentarios) = coroutineScope {
                val a = async { repo.detalle(id) }
                val b = async { repo.comentarios(id) }
                Pair(a.await(), b.await())
            }
            val error = post.exceptionOrNull() ?: comentarios.exceptionOrNull()
            _detalle.value = if (error != null) {
                UiState.Error(mensajeDe(error))
            } else {
                UiState.Exito(DetalleUi(post.getOrThrow(), comentarios.getOrThrow()))
            }
        }
    }
}
