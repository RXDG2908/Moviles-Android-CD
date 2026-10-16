package com.tecsup.huellitasvet.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.graficos.DatoGrafico
import com.tecsup.huellitasvet.ui.theme.EstadoAtendida
import com.tecsup.huellitasvet.ui.theme.EstadoCancelada
import com.tecsup.huellitasvet.ui.theme.EstadoConfirmada
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(private val repo: VetRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString() // día 1 del mes

    // Citas de hoy sin contar canceladas
    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Suma del precio de las ATENDIDAS desde el día 1
    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(inicioMes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // ---------- Datos de Room -> formato de los gráficos ----------
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        repo.observarCantidadPorEstado(EstadoCita.CANCELADA)
    ) { p, c, a, x ->
        listOf(
            DatoGrafico(EstadoCita.PENDIENTE.etiqueta, p.toFloat(), EstadoPendiente),
            DatoGrafico(EstadoCita.CONFIRMADA.etiqueta, c.toFloat(), EstadoConfirmada),
            DatoGrafico(EstadoCita.ATENDIDA.etiqueta, a.toFloat(), EstadoAtendida),
            DatoGrafico(EstadoCita.CANCELADA.etiqueta, x.toFloat(), EstadoCancelada)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val paleta = listOf(Color(0xFF1D4ED8), Color(0xFF0EA5E9), Color(0xFF8B5CF6), Color(0xFF14B8A6))

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { lista ->
            lista.mapIndexed { i, c -> DatoGrafico(c.nombre, c.cantidad.toFloat(), paleta[i % paleta.size]) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
