package com.tecsup.fortnite.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.fortnite.data.auth.AuthRepository
import com.tecsup.fortnite.data.auth.FalloIngreso
import com.tecsup.fortnite.data.auth.MotivoFallo
import com.tecsup.fortnite.domain.model.Sesion
import com.tecsup.fortnite.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Errores de cada campo; null si el campo está bien
data class ErroresCampos(val correo: String? = null, val clave: String? = null)

private val formatoCorreo = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {

    // Sesión activa (null si nadie ingresó)
    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    // true mientras se revisa si ya había una sesión guardada
    private val _revisando = MutableStateFlow(true)
    val revisando: StateFlow<Boolean> = _revisando.asStateFlow()

    // null = formulario normal; Cargando mientras se ingresa; Error si Firebase respondió con un problema
    private val _login = MutableStateFlow<UiState<Sesion>?>(null)
    val login: StateFlow<UiState<Sesion>?> = _login.asStateFlow()

    // Errores que van debajo de cada campo
    private val _errores = MutableStateFlow(ErroresCampos())
    val errores: StateFlow<ErroresCampos> = _errores.asStateFlow()

    init {
        viewModelScope.launch {
            _sesion.value = auth.sesionActual()
            _revisando.value = false
        }
    }

    fun ingresar(correo: String, clave: String) {
        // Primero se revisan los campos; con errores no se llama a Firebase
        val errores = validar(correo, clave)
        _errores.value = errores
        if (errores.correo != null || errores.clave != null) {
            _login.value = null
            return
        }
        _login.value = UiState.Cargando
        viewModelScope.launch {
            auth.iniciarSesion(correo.trim(), clave)
                .onSuccess {
                    _sesion.value = it
                    _login.value = null
                }
                .onFailure { e -> _login.value = UiState.Error(mensajeDe(e)) }
        }
    }

    // Al escribir de nuevo en un campo, su error se borra
    fun alEditarCorreo() {
        if (_errores.value.correo != null) _errores.value = _errores.value.copy(correo = null)
        if (_login.value is UiState.Error) _login.value = null
    }

    fun alEditarClave() {
        if (_errores.value.clave != null) _errores.value = _errores.value.copy(clave = null)
        if (_login.value is UiState.Error) _login.value = null
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _sesion.value = null
        _login.value = null
        _errores.value = ErroresCampos()
    }

    private fun validar(correo: String, clave: String) = ErroresCampos(
        correo = when {
            correo.isBlank() -> "Escribe tu correo"
            !formatoCorreo.matches(correo.trim()) -> "Revisa el correo: debe tener @ y un dominio, como nombre@correo.com"
            else -> null
        },
        clave = if (clave.isBlank()) "Escribe tu contraseña" else null
    )

    // Mensajes que dicen qué pasó y qué hacer
    private fun mensajeDe(e: Throwable): String = when ((e as? FalloIngreso)?.motivo) {
        MotivoFallo.CREDENCIALES -> "El correo o la contraseña no coinciden. Revísalos e intenta otra vez."
        MotivoFallo.SIN_CONEXION -> "No hay conexión a internet. Conéctate e intenta otra vez."
        MotivoFallo.DEMASIADOS_INTENTOS -> "Hiciste muchos intentos seguidos. Espera unos minutos y vuelve a probar."
        else -> "No pudimos ingresar en este momento. Intenta otra vez en unos segundos."
    }
}
