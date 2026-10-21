package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.PuntoPeso
import com.tecsup.huellitasvet.model.Sexo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// Un error por campo (null = sin error)
data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val sexo: String? = null,
    val anio: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, especie, sexo, anio, peso).any { it != null }
}

class MascotaViewModel(private val repo: VetRepository) : ViewModel() {

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun mascota(id: Int): StateFlow<Mascota?> = repo.observarMascota(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Historial clínico: citas ATENDIDAS, la más reciente primero (el orden lo da el DAO)
    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> = repo.observarHistorial(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Puntos del gráfico de peso: uno por cada atención con peso registrado
    fun pesos(mascotaId: Int): StateFlow<List<PuntoPeso>> = repo.observarPesos(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardar(mascota: Mascota, alGuardar: () -> Unit) {
        val anioActual = LocalDate.now().year
        val errores = ErroresMascota(
            nombre = if (mascota.nombre.trim().length < 2) "Mínimo 2 caracteres" else null,
            especie = if (Especie.entries.none { it.name == mascota.especie }) "Elige la especie" else null,
            sexo = if (Sexo.entries.none { it.name == mascota.sexo }) "Elige el sexo" else null,
            anio = if (mascota.anioNacimiento !in 1990..anioActual) "Entre 1990 y $anioActual" else null,
            peso = if (mascota.peso <= 0) "El peso debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            repo.guardarMascota(mascota.copy(nombre = mascota.nombre.trim(), raza = mascota.raza.trim()))
            alGuardar()
        }
    }

    // Solo se elimina si NO tiene citas
    fun eliminar(mascota: Mascota, alEliminar: () -> Unit) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(mascota.id)) {
                _mensaje.value = "No se puede eliminar: ${mascota.nombre} tiene citas registradas"
            } else {
                repo.eliminarMascota(mascota)
                alEliminar()
            }
        }
    }
}
