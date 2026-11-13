// Logica de la pantalla de detalle.
package com.tecsup.fortnite.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.fortnite.data.repository.ItemRepository
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repo: ItemRepository,
    private val id: String
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repo.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error("No se pudo cargar el cosmético") }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            repo.alternarFavorito(id)
            // Se vuelve a leer para que la estrella cambie
            val actual = _estado.value
            if (actual is UiState.Exito) {
                _estado.value = UiState.Exito(actual.datos.copy(esFavorito = !actual.datos.esFavorito))
            }
        }
    }
}
