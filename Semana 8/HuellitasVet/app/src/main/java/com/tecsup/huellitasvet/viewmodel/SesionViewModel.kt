package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Sesión del usuario y preferencias. Vive mientras la Activity existe (compartido por todas las pantallas).
class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    // Error general del login (credenciales o rol equivocado)
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Mejora: errores debajo de cada campo (login y registro)
    private val _errores = MutableStateFlow(ErroresFormulario())
    val errores: StateFlow<ErroresFormulario> = _errores

    // Mejora: evita tocar "Ingresar" dos veces mientras consulta Room
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // Mejora: aviso y correo para el login después de registrarse
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _correoSugerido = MutableStateFlow("")
    val correoSugerido: StateFlow<String> = _correoSugerido

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // ---------- Splash: si ya hay perfil guardado, salta el login ----------
    fun restaurarSesion(alTerminar: (Rol?) -> Unit) {
        viewModelScope.launch {
            val perfil = prefs.perfilActivo.first()
            val id = prefs.usuarioId.first()
            val usuario = if (perfil.isNotEmpty()) repo.obtenerUsuario(id) else null
            _usuarioActual.value = usuario
            alTerminar(usuario?.let { Rol.valueOf(it.rol) })
        }
    }

    // ---------- Login con selector de rol ----------
    fun login(correo: String, clave: String, rolSeleccionado: Rol, alEntrar: (Rol) -> Unit) {
        val errores = ErroresFormulario(
            correo = validarCorreo(correo),
            clave = if (clave.isBlank()) "Ingresa tu contraseña" else null
        )
        _errores.value = errores
        _error.value = null
        if (errores.hayErrores || _cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Esta cuenta no es de ${rolSeleccionado.etiqueta}"
                else -> {
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _usuarioActual.value = usuario
                    _mensaje.value = null
                    alEntrar(rolSeleccionado)
                }
            }
            _cargando.value = false
        }
    }

    // ---------- Mejora: registro de dueños ----------
    // Solo se registran dueños (CLIENTE); los veterinarios los crea la clínica.
    // Al terminar vuelve al login con el correo escrito, para que el dueño ingrese.
    // Mejora: también pide celular y distrito
    fun registrar(
        nombre: String, correo: String, telefono: String, distrito: String,
        clave: String, confirmar: String, alRegistrar: () -> Unit
    ) {
        val errores = ErroresFormulario(
            nombre = Validaciones.nombre(nombre),
            correo = Validaciones.correo(correo),
            telefono = Validaciones.telefono(telefono),
            distrito = Validaciones.distrito(distrito),
            clave = Validaciones.clave(clave),
            confirmar = if (confirmar != clave) "Las contraseñas no coinciden" else null
        )
        _errores.value = errores
        _error.value = null
        if (errores.hayErrores || _cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            val limpio = correo.trim().lowercase()
            if (repo.correoRegistrado(limpio)) {
                _errores.value = errores.copy(correo = "Este correo ya tiene una cuenta")
            } else {
                repo.registrarUsuario(
                    Usuario(
                        nombre = nombre.trim(), correo = limpio, clave = clave, rol = Rol.CLIENTE.name,
                        telefono = telefono.filter { it.isDigit() }, distrito = distrito
                    )
                )
                _correoSugerido.value = limpio
                _mensaje.value = "Cuenta creada. Ingresa con tu correo y contraseña."
                alRegistrar()
            }
            _cargando.value = false
        }
    }

    private fun validarCorreo(correo: String): String? = Validaciones.correo(correo)

    // ---------- Cambiar de perfil: guarda el otro rol y su usuario ----------
    // Modo práctica entre Dueño y Veterinario (el administrador no lo usa)
    fun cambiarPerfil(alCambiar: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val otro = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
        viewModelScope.launch {
            val usuario = repo.obtenerUsuarioPorRol(otro) ?: return@launch
            prefs.guardarSesion(otro, usuario.id)
            _usuarioActual.value = usuario
            alCambiar(otro)
        }
    }

    // ---------- Preferencias (se guardan en DataStore) ----------
    fun cambiarModoOscuro(b: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(b) }
    }

    fun cambiarNotificaciones(b: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(b) }
    }

    fun cambiarAceptarCitas(b: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(b) }
    }

    fun cerrarSesion(alCerrar: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            alCerrar()
        }
    }

    fun limpiarError() {
        _error.value = null
        _errores.value = ErroresFormulario()
    }

    // Borra el error de un campo apenas el usuario lo corrige
    fun limpiarCampo(campo: Campo) {
        _errores.value = when (campo) {
            Campo.NOMBRE -> _errores.value.copy(nombre = null)
            Campo.CORREO -> _errores.value.copy(correo = null)
            Campo.CLAVE -> _errores.value.copy(clave = null)
            Campo.CONFIRMAR -> _errores.value.copy(confirmar = null)
            Campo.TELEFONO -> _errores.value.copy(telefono = null)
            Campo.DISTRITO -> _errores.value.copy(distrito = null)
        }
        _error.value = null
    }
}

enum class Campo { NOMBRE, CORREO, CLAVE, CONFIRMAR, TELEFONO, DISTRITO }

data class ErroresFormulario(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmar: String? = null,
    val telefono: String? = null,
    val distrito: String? = null
) {
    val hayErrores get() = listOf(nombre, correo, clave, confirmar, telefono, distrito).any { it != null }
}
