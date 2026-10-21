package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AtenderCitaViewModel(private val repo: VetRepository) : ViewModel() {

    private val citaId = MutableStateFlow(0)

    fun iniciar(id: Int) {
        citaId.value = id
    }

    val cita: StateFlow<CitaConDetalle?> = citaId
        .flatMapLatest { repo.observarCita(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errorDiagnostico = MutableStateFlow<String?>(null)
    val errorDiagnostico: StateFlow<String?> = _errorDiagnostico

    private val _errorPeso = MutableStateFlow<String?>(null)
    val errorPeso: StateFlow<String?> = _errorPeso

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    // Solo se puede atender o cancelar una cita PENDIENTE o CONFIRMADA
    fun sePuedeAtender(c: CitaConDetalle): Boolean =
        c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name

    fun registrar(diagnostico: String, tratamiento: String, peso: Double, alTerminar: () -> Unit) {
        val c = cita.value ?: return
        if (!sePuedeAtender(c)) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }
        _errorDiagnostico.value = if (diagnostico.trim().length < 5) "Mínimo 5 caracteres" else null
        _errorPeso.value = if (peso <= 0) "El peso debe ser mayor que 0" else null
        if (_errorDiagnostico.value != null || _errorPeso.value != null) return

        viewModelScope.launch {
            // Transacción en el Repository: cita ATENDIDA + peso de la mascota a la vez
            repo.registrarAtencion(c.cita.id, diagnostico.trim(), tratamiento.trim(), peso)
            alTerminar()
        }
    }

    fun cancelar(alTerminar: () -> Unit) {
        val c = cita.value ?: return
        if (!sePuedeAtender(c)) return
        viewModelScope.launch {
            repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA)
            alTerminar()
        }
    }
}
