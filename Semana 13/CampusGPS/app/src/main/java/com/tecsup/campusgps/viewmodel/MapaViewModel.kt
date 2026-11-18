// ViewModel de la pantalla: busca la ubicación y publica el estado.
package com.tecsup.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionNoDisponibleException
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    // Todos los lugares con su distancia; la pantalla recibe solo los que pasan el filtro
    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )

    // Categorías para los chips, sacadas de los mismos lugares (sin repetir)
    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    // null = sin filtro (se ven todas las categorías)
    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    val lugares: StateFlow<List<LugarConDistancia>> = combine(_lugares, _categoria) { lista, cat ->
        if (cat == null) lista else lista.filter { it.lugar.categoria == cat }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, _lugares.value)

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    fun buscar() {
        // Sin Play Services no hay ubicación posible: se avisa sin intentar
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    // Calcula la distancia a cada lugar y ordena del más cercano al más lejano
    private fun actualizarDistancias(pos: Posicion) {
        val conDistancia = lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, distanciaMetros(pos, it))
        }
        _lugares.value = conDistancia.sortedBy { it.metros }
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    // Radio de cercanía en metros; null = sin radio
    val radios: List<Int> = listOf(100, 200, 500)
    private val _radio = MutableStateFlow<Int?>(null)
    val radio: StateFlow<Int?> = _radio.asStateFlow()

    fun elegirRadio(metros: Int?) {
        _radio.value = metros
    }

    // Ids de los lugares visibles que están a la distancia del radio o menos
    val dentroDelRadio: StateFlow<Set<Int>> = combine(lugares, _radio) { lista, r ->
        if (r == null) {
            emptySet()
        } else {
            lista.filter { it.metros != null && it.metros <= r }.map { it.lugar.id }.toSet()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    fun filtrar(cat: String?) {
        _categoria.value = cat
        // Si el lugar elegido queda oculto por el filtro, se quita la selección
        val sigueVisible = _lugares.value.any {
            it.lugar.id == _seleccionado.value && (cat == null || it.lugar.categoria == cat)
        }
        if (!sigueVisible) _seleccionado.value = null
    }

    // El más cercano es el primero de la lista visible que ya tiene distancia; null sin ubicación
    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error("Falta el permiso de ubicación", TipoError.SIN_PERMISO)
        is UbicacionNoDisponibleException ->
            UbicacionUi.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
        else -> UbicacionUi.Error(e.message ?: "No se pudo obtener la ubicación", TipoError.OTRO)
    }
}
