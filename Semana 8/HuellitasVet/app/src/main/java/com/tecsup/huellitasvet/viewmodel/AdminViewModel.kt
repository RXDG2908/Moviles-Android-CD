package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Validaciones
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Números del panel del administrador
data class ResumenAdmin(
    val duenos: Int = 0,
    val veterinarios: Int = 0,
    val administradores: Int = 0,
    val mascotas: Int = 0,
    val servicios: Int = 0,
    val citas: Int = 0
) {
    val usuarios get() = duenos + veterinarios + administradores
}

// Mejora: administrador con control total (usuarios de los 3 roles y mascotas de todos)
@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModel(private val repo: VetRepository) : ViewModel() {

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _errores = MutableStateFlow(ErroresFormulario())
    val errores: StateFlow<ErroresFormulario> = _errores

    // ---------- Panel ----------
    val resumen: StateFlow<ResumenAdmin> = combine(
        listOf(
            repo.contarUsuariosPorRol(Rol.CLIENTE),
            repo.contarUsuariosPorRol(Rol.ADMIN),
            repo.contarUsuariosPorRol(Rol.ADMINISTRADOR),
            repo.contarMascotas(),
            repo.observarServicios().map { it.size },
            repo.contarCitas()
        )
    ) { n -> ResumenAdmin(n[0], n[1], n[2], n[3], n[4], n[5]) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResumenAdmin())

    // ---------- Usuarios: pestaña por rol + búsqueda ----------
    private val _rol = MutableStateFlow(Rol.CLIENTE)
    val rol: StateFlow<Rol> = _rol

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    val usuarios: StateFlow<List<Usuario>> = combine(
        _rol.flatMapLatest { repo.observarUsuariosPorRol(it) },
        _busqueda
    ) { lista, texto ->
        val t = texto.trim()
        if (t.isEmpty()) lista
        else lista.filter { u ->
            u.nombre.contains(t, true) || u.correo.contains(t, true) || u.distrito.contains(t, true) || u.telefono.contains(t)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Todos los dueños (combobox del formulario de mascota)
    val duenos: StateFlow<List<Usuario>> = repo.observarUsuariosPorRol(Rol.CLIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---------- Mascotas de todos los dueños + búsqueda ----------
    private val _busquedaMascota = MutableStateFlow("")
    val busquedaMascota: StateFlow<String> = _busquedaMascota

    private val todasLasMascotas = repo.observarTodasLasMascotas()

    val mascotas: StateFlow<List<MascotaConDueno>> = combine(todasLasMascotas, _busquedaMascota) { lista, texto ->
        val t = texto.trim()
        if (t.isEmpty()) lista
        else lista.filter { it.mascota.nombre.contains(t, true) || it.mascota.raza.contains(t, true) || it.dueno.nombre.contains(t, true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cuántas mascotas tiene cada dueño (se muestra en la lista de usuarios)
    val mascotasPorDueno: StateFlow<Map<Int, Int>> = todasLasMascotas
        .map { lista -> lista.groupingBy { it.mascota.duenoId }.eachCount() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun elegirRol(r: Rol) { _rol.value = r }
    fun buscar(t: String) { _busqueda.value = t }
    fun buscarMascota(t: String) { _busquedaMascota.value = t }
    fun limpiarMensaje() { _mensaje.value = null }
    fun limpiarErrores() { _errores.value = ErroresFormulario() }

    fun usuario(id: Int): StateFlow<Usuario?> = flow { emit(repo.obtenerUsuario(id)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // ---------- Guardar usuario (nuevo o editado) ----------
    // Al editar, la contraseña vacía conserva la anterior.
    fun guardarUsuario(
        id: Int, nombre: String, correo: String, telefono: String, distrito: String,
        rol: Rol, clave: String, alGuardar: () -> Unit
    ) {
        val esNuevo = id == 0
        val errores = ErroresFormulario(
            nombre = Validaciones.nombre(nombre),
            correo = Validaciones.correo(correo),
            telefono = Validaciones.telefono(telefono),
            distrito = Validaciones.distrito(distrito),
            clave = if (esNuevo || clave.isNotEmpty()) Validaciones.clave(clave) else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            val limpio = correo.trim().lowercase()
            if (repo.correoDeOtroUsuario(limpio, id)) {
                _errores.value = errores.copy(correo = "Este correo ya tiene una cuenta")
                return@launch
            }
            val claveFinal = if (!esNuevo && clave.isEmpty()) repo.obtenerUsuario(id)?.clave ?: clave else clave
            repo.guardarUsuario(
                Usuario(
                    id = id, nombre = nombre.trim(), correo = limpio, clave = claveFinal, rol = rol.name,
                    telefono = telefono.filter { it.isDigit() }, distrito = distrito
                )
            )
            _mensaje.value = if (esNuevo) "${rol.etiqueta} creado: ${nombre.trim()}" else "Cambios guardados"
            alGuardar()
        }
    }

    // ---------- Eliminar usuario (con sus mascotas y citas) ----------
    fun eliminarUsuario(u: Usuario, idActual: Int) {
        viewModelScope.launch {
            when {
                u.id == idActual -> _mensaje.value = "No puedes eliminar tu propia cuenta"
                u.rol == Rol.ADMINISTRADOR.name && repo.contarUsuariosPorRol(Rol.ADMINISTRADOR).first() <= 1 ->
                    _mensaje.value = "Debe quedar al menos un administrador"
                else -> {
                    repo.eliminarUsuarioConTodo(u)
                    _mensaje.value = "${u.nombre} fue eliminado"
                }
            }
        }
    }

    // ---------- Eliminar mascota (con su historial de citas) ----------
    fun eliminarMascota(m: Mascota) {
        viewModelScope.launch {
            repo.eliminarMascotaConCitas(m)
            _mensaje.value = "${m.nombre} fue eliminada"
        }
    }
}
