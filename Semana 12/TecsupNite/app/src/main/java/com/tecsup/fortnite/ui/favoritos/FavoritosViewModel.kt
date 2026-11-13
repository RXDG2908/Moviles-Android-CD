// Logica de la pantalla de favoritos.
package com.tecsup.fortnite.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.fortnite.data.repository.ItemRepository
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repo: ItemRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repo.favoritos.collect { _estado.value = UiState.Exito(it) }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }
}
