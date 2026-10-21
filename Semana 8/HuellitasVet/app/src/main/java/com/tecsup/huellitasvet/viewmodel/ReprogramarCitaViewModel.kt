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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ReprogramarCitaViewModel(private val repo: VetRepository) : ViewModel() {

    private val citaId = MutableStateFlow(0)

    val cita: StateFlow<CitaConDetalle?> = citaId
        .flatMapLatest { repo.observarCita(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    // Chips de fecha: de hoy a 7 días (igual que al pedir una cita)
    val fechas: List<LocalDate> = (0..7L).map { LocalDate.now().plusDays(it) }

    // Horas ocupadas del día elegido, sin contar el horario actual de esta misma cita
    val horasOcupadas: StateFlow<List<String>> = combine(
        _fecha.flatMapLatest { repo.observarHorasOcupadas(it.toString()) },
        cita
    ) { ocupadas, c ->
        if (c != null && c.cita.fecha == _fecha.value.toString()) ocupadas - c.cita.hora else ocupadas
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun iniciar(id: Int) {
        if (citaId.value == id) return
        citaId.value = id
        // Arranca en el día de la cita si está dentro de los chips
        viewModelScope.launch {
            val c = repo.observarCita(id).first() ?: return@launch
            val dia = LocalDate.parse(c.cita.fecha)
            if (dia in fechas) _fecha.value = dia
        }
    }

    // Al elegir otra fecha u hora se borra el mensaje de error anterior
    fun elegirHora(h: String) {
        _hora.value = h
        _mensaje.value = null
    }

    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null // si la fecha cambia, se limpia la hora
        _mensaje.value = null
    }

    // Regla de 2 horas: solo se mueve una cita (o se elige un horario) si faltan MÁS de 2 horas
    private fun faltanMasDeDosHoras(fecha: String, hora: String): Boolean =
        Duration.between(LocalDateTime.now(), Fechas.fechaHora(fecha, hora)).toMinutes() > 120

    // Un horario no se puede elegir si ya pasó o si faltan 2 horas o menos
    fun horaNoDisponible(h: String): Boolean = !faltanMasDeDosHoras(_fecha.value.toString(), h)

    // Solo PENDIENTE o CONFIRMADA y si faltan más de 2 horas para la cita actual
    fun puedeReprogramar(c: CitaConDetalle): Boolean {
        val activa = c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name
        return activa && faltanMasDeDosHoras(c.cita.fecha, c.cita.hora)
    }

    fun confirmar(alReprogramar: () -> Unit) {
        val c = cita.value ?: return
        val h = _hora.value
        val f = _fecha.value.toString()
        if (!puedeReprogramar(c)) {
            _mensaje.value = "Esta cita ya no se puede reprogramar"
            return
        }
        if (h == null) {
            _mensaje.value = "Elige la nueva fecha y hora"
            return
        }
        if (f == c.cita.fecha && h == c.cita.hora) {
            _mensaje.value = "Elige un horario distinto al actual"
            return
        }
        if (horaNoDisponible(h)) {
            _mensaje.value = "El nuevo horario debe ser con más de 2 horas de anticipación"
            _hora.value = null
            return
        }
        viewModelScope.launch {
            // Se vuelve a consultar: el horario pudo ocuparse mientras tanto
            if (repo.horarioOcupado(f, h)) {
                _mensaje.value = "Ese horario acaba de ocuparse. Elige otro."
                _hora.value = null
                return@launch
            }
            repo.reprogramarCita(c.cita.id, f, h)
            _mensaje.value = null
            alReprogramar()
        }
    }
}
