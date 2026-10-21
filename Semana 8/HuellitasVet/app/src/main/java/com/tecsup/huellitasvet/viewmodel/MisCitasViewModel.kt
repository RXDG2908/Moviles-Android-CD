package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MisCitasViewModel(private val repo: VetRepository) : ViewModel() {

    private val duenoId = MutableStateFlow(0)

    fun iniciar(id: Int) {
        duenoId.value = id
    }

    // Flow de Room: si el veterinario confirma o atiende, la lista cambia sola
    private val citas = duenoId.flatMapLatest { repo.observarCitasDeDueno(it) }

    // Próximas = PENDIENTE o CONFIRMADA desde ahora
    val proximas: StateFlow<List<CitaConDetalle>> = citas
        .map { lista -> lista.filter { esProxima(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Historial = ATENDIDA, CANCELADA y las pasadas (la más reciente primero)
    val historial: StateFlow<List<CitaConDetalle>> = citas
        .map { lista -> lista.filterNot { esProxima(it) }.reversed() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private fun activa(c: CitaConDetalle) =
        c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name

    private fun esProxima(c: CitaConDetalle): Boolean =
        activa(c) && !Fechas.fechaHora(c.cita.fecha, c.cita.hora).isBefore(LocalDateTime.now())

    // Regla: solo PENDIENTE o CONFIRMADA y si faltan MÁS de 2 horas
    fun puedeCancelar(c: CitaConDetalle): Boolean {
        if (!activa(c)) return false
        val falta = Duration.between(LocalDateTime.now(), Fechas.fechaHora(c.cita.fecha, c.cita.hora))
        return falta.toMinutes() > 120
    }

    fun cancelar(c: CitaConDetalle) {
        if (!puedeCancelar(c)) {
            _mensaje.value = "Ya no puedes cancelar esta cita"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA)
            _mensaje.value = null
        }
    }
}
