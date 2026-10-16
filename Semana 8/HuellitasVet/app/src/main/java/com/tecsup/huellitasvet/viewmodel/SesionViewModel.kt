package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Esta cuenta no es de ${rolSeleccionado.etiqueta}"
                else -> {
                    _error.value = null
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _usuarioActual.value = usuario
                    alEntrar(rolSeleccionado)
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
