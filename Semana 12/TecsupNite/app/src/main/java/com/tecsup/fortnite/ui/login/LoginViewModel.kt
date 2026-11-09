package com.tecsup.fortnite.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.fortnite.data.auth.AuthRepository
import com.tecsup.fortnite.domain.model.Sesion
import com.tecsup.fortnite.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {

    // Sesión activa (null si nadie ingresó)
    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    // true mientras se revisa si ya había una sesión guardada
    private val _revisando = MutableStateFlow(true)
    val revisando: StateFlow<Boolean> = _revisando.asStateFlow()

    // null = formulario normal; Cargando / Error mientras se ingresa
    private val _login = MutableStateFlow<UiState<Sesion>?>(null)
    val login: StateFlow<UiState<Sesion>?> = _login.asStateFlow()

    init {
        viewModelScope.launch {
            _sesion.value = auth.sesionActual()
            _revisando.value = false
        }
    }

    fun ingresar(correo: String, clave: String) {
        // Con campos vacíos no se llama a Firebase
        if (correo.isBlank() || clave.isBlank()) {
            _login.value = UiState.Error("Escribe tu correo y tu contraseña")
            return
        }
        _login.value = UiState.Cargando
        viewModelScope.launch {
            auth.iniciarSesion(correo.trim(), clave)
                .onSuccess {
                    _sesion.value = it
                    _login.value = null
                }
                .onFailure { _login.value = UiState.Error("No se pudo ingresar. Revisa tu correo, tu contraseña y tu conexión.") }
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _sesion.value = null
        _login.value = null
    }
}
