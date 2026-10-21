package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PacientesViewModel(private val repo: VetRepository) : ViewModel() {

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto

    private val _especie = MutableStateFlow<Especie?>(null) // null = Todos
    val especie: StateFlow<Especie?> = _especie

    // combine: se recalcula cuando cambia la lista, el texto o la especie
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(), _texto, _especie
    ) { lista, t, e ->
        lista.filter { p ->
            val coincideEspecie = e == null || p.mascota.especie == e.name
            val coincideTexto = t.isBlank() ||
                p.mascota.nombre.contains(t.trim(), ignoreCase = true) ||
                p.dueno.nombre.contains(t.trim(), ignoreCase = true)
            coincideEspecie && coincideTexto
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // mascotaId -> fecha de su última atención
    val ultimasVisitas: StateFlow<Map<Int, String>> = repo.observarUltimasVisitas()
        .map { lista -> lista.associate { it.mascotaId to it.fecha } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun buscar(t: String) { _texto.value = t }
    fun filtrar(e: Especie?) { _especie.value = e }
}
