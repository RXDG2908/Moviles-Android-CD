package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresServicio(
    val nombre: String? = null,
    val duracion: String? = null,
    val precio: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || duracion != null || precio != null
}

// EJEMPLO RESUELTO (Flow -> StateFlow con stateIn) + guardar y eliminar
class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    // Para editar: busca el servicio en la lista observada
    fun servicio(id: Int): StateFlow<Servicio?> = repo.observarServicios()
        .map { lista -> lista.firstOrNull { it.id == id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    // Nuevo (id = 0) o editado (id > 0)
    fun guardar(s: Servicio, alGuardar: () -> Unit) {
        val errores = ErroresServicio(
            nombre = if (s.nombre.isBlank()) "El nombre es obligatorio" else null,
            duracion = if (s.duracionMin !in listOf(15, 30, 45, 60)) "Elige 15, 30, 45 o 60 min" else null,
            precio = if (s.precio <= 0) "El precio debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            repo.guardarServicio(s.copy(nombre = s.nombre.trim(), descripcion = s.descripcion.trim()))
            alGuardar()
        }
    }

    // Un servicio con citas no se elimina: solo se puede desactivar
    fun eliminar(s: Servicio, alEliminar: () -> Unit) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(s.id)) {
                _mensaje.value = "Este servicio tiene citas: no se puede eliminar, solo desactivar"
            } else {
                repo.eliminarServicio(s)
                alEliminar()
            }
        }
    }
}
