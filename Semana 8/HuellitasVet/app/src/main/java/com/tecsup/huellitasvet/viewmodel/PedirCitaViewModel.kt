package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Fechas
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(private val repo: VetRepository) : ViewModel() {

    private val duenoId = MutableStateFlow(0)

    // ---------- Lo que el dueño elige ----------
    private val _mascotaId = MutableStateFlow<Int?>(null)
    val mascotaId: StateFlow<Int?> = _mascotaId

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId

    private val _fecha = MutableStateFlow(LocalDate.now()) // por defecto: hoy
    val fecha: StateFlow<LocalDate> = _fecha

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    // ---------- Datos que se observan ----------
    val mascotas: StateFlow<List<Mascota>> = duenoId
        .flatMapLatest { repo.observarMascotasDeDueno(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Solo servicios ACTIVOS
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cambia sola cuando cambia la fecha
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Chips de fecha: de hoy a 7 días
    val fechas: List<LocalDate> = (0..7L).map { LocalDate.now().plusDays(it) }

    fun iniciar(duenoId: Int, mascotaInicial: Int?) {
        this.duenoId.value = duenoId
        if (mascotaInicial != null && _mascotaId.value == null) _mascotaId.value = mascotaInicial
    }

    fun elegirMascota(id: Int) { _mascotaId.value = id }
    fun elegirServicio(id: Int) { _servicioId.value = id }
    fun elegirHora(h: String) { _hora.value = h }

    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null // si la fecha cambia, se limpia la hora
    }

    // Una hora ya pasó si la fecha es hoy y la hora es anterior a ahora
    fun horaPasada(h: String): Boolean =
        Fechas.fechaHora(_fecha.value.toString(), h).isBefore(LocalDateTime.now())

    fun confirmar(nota: String, alCrear: () -> Unit) {
        val m = _mascotaId.value
        val s = _servicioId.value
        val h = _hora.value
        val f = _fecha.value.toString()
        if (m == null || s == null || h == null) {
            _mensaje.value = "Elige mascota, servicio, fecha y hora"
            return
        }
        viewModelScope.launch {
            // Se vuelve a consultar: el horario pudo ocuparse mientras tanto
            if (repo.horarioOcupado(f, h)) {
                _mensaje.value = "Ese horario acaba de ocuparse. Elige otro."
                _hora.value = null
                return@launch
            }
            if (horaPasada(h)) {
                _mensaje.value = "Esa hora ya pasó. Elige otra."
                _hora.value = null
                return@launch
            }
            repo.crearCita(m, s, f, h, nota.trim())
            _mensaje.value = null
            alCrear()
        }
    }
}
