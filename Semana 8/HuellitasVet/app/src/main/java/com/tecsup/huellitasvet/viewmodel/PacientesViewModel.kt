package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PacientesViewModel(private val repo: VetRepository) : ViewModel() {

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto

    private val _especie = MutableStateFlow<Especie?>(null) // null = Todos
    val especie: StateFlow<Especie?> = _especie

    // La búsqueda la hace Room (SQL con LIKE). Cuando cambia el texto o la especie,
    // flatMapLatest cancela la consulta anterior y observa la nueva
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(_texto, _especie) { t, e -> t to e }
        .flatMapLatest { (t, e) -> repo.buscarPacientes(t, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Hay filtros puestos: se muestra el botón para limpiarlos
    val hayFiltros: StateFlow<Boolean> = combine(_texto, _especie) { t, e -> t.isNotBlank() || e != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // mascotaId -> fecha de su última atención
    val ultimasVisitas: StateFlow<Map<Int, String>> = repo.observarUltimasVisitas()
        .map { lista -> lista.associate { it.mascotaId to it.fecha } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun buscar(t: String) { _texto.value = t }
    fun filtrar(e: Especie?) { _especie.value = e }

    fun limpiar() {
        _texto.value = ""
        _especie.value = null
    }
}
