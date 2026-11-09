package com.tecsup.fortnite.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.fortnite.data.repository.ItemRepository
import com.tecsup.fortnite.notification.Notificador
import com.tecsup.fortnite.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Totales(val guardados: Int, val favoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Totales>>(UiState.Cargando)
    val estado: StateFlow<UiState<Totales>> = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        actualizarTotales()
    }

    private fun actualizarTotales() {
        viewModelScope.launch {
            _estado.value = UiState.Exito(Totales(repo.cantidadGuardada(), repo.cantidadFavoritos()))
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            repo.refrescar()
                .onSuccess { n ->
                    _mensaje.value = "Se guardaron $n elementos"
                    notificador.mostrar("Tienda actualizada", "Se guardaron $n elementos")
                }
                .onFailure { _mensaje.value = "No se pudo sincronizar. Revisa tu conexión." }
            actualizarTotales()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repo.limpiarCache()
            _mensaje.value = "Caché limpiada"
            actualizarTotales()
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }
}
