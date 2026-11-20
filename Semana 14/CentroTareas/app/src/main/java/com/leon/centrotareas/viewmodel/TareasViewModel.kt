// ViewModel de las tres tareas: guarda los estados y lanza el trabajo en segundo plano.
package com.leon.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leon.centrotareas.data.TareasRepository
import com.leon.centrotareas.model.CargaUi
import com.leon.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Avance de 0f a 1f; la pantalla solo lo lee
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    // true mientras la tarea trabaja
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    // Guarda la corrutina para poder cancelarla
    private var trabajo: Job? = null

    // La espera va en otro hilo; el hilo principal sigue dibujando
    fun tareaLarga() {
        // Si ya hay una tarea corriendo no se lanza otra
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            try {
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                // El Repository hace el trabajo en Default y emite el avance;
                // collect recibe cada valor de vuelta en el hilo principal
                repo.tareaConProgreso().collect { avance ->
                    _progreso.value = avance
                }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                // Se ejecuta termine bien o cancelada
                _corriendo.value = false
            }
        }
    }

    // Detiene la tarea; la barra se queda donde estaba
    fun cancelar() {
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    // Cuenta los primos con el Repository y mide cuánto tarda
    fun contar(hasta: Int) {
        if (_primos.value is PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando()
            val inicio = System.currentTimeMillis()
            try {
                repo.contarPrimosConProgreso(hasta).collect { avance ->
                    val cantidad = avance.cantidad
                    _primos.value = if (cantidad == null) {
                        PrimosUi.Calculando(avance.porcentaje)
                    } else {
                        PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
                    }
                }
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                // Se vuelve a lanzar para no romper la cancelación
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // Interruptor: si está activo, los pedidos fallan
    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    // Primero el perfil y después los pedidos: unos 5 s
    fun cargarUnaPorUna() {
        if (_carga.value == CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(_simularError.value)
                _carga.value = CargaUi.Exito(
                    "Una por una", perfil, pedidos.size, System.currentTimeMillis() - inicio
                )
            } catch (e: Exception) {
                // La cancelación no es un error: se deja pasar
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }

    // Las dos cargas al mismo tiempo con async: tarda lo que la más lenta, unos 3 s
    fun cargarALaVez() {
        if (_carga.value == CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = coroutineScope {
                    val a = async { repo.cargarPerfil() }
                    val b = async { repo.cargarPedidos(_simularError.value) }
                    Pair(a.await(), b.await())
                }
                _carga.value = CargaUi.Exito(
                    "A la vez", perfil, pedidos.size, System.currentTimeMillis() - inicio
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }
}
