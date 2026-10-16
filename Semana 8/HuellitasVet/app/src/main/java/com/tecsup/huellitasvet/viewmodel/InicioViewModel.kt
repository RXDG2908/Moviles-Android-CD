package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Fechas
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class InicioViewModel(private val repo: VetRepository) : ViewModel() {

    // Dueño actual (se recibe desde la pantalla con iniciar)
    private val duenoId = MutableStateFlow(0)

    fun iniciar(id: Int) {
        duenoId.value = id
    }

    val mascotas: StateFlow<List<Mascota>> = duenoId
        .flatMapLatest { repo.observarMascotasDeDueno(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Primera cita PENDIENTE o CONFIRMADA desde ahora (ordenada por fecha y hora)
    val proximaCita: StateFlow<CitaConDetalle?> = duenoId
        .flatMapLatest { repo.observarCitasDeDueno(it) }
        .map { citas ->
            val ahora = LocalDateTime.now()
            citas
                .filter { it.cita.estado == EstadoCita.PENDIENTE.name || it.cita.estado == EstadoCita.CONFIRMADA.name }
                .filter { !Fechas.fechaHora(it.cita.fecha, it.cita.hora).isBefore(ahora) }
                .minByOrNull { Fechas.fechaHora(it.cita.fecha, it.cita.hora) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
