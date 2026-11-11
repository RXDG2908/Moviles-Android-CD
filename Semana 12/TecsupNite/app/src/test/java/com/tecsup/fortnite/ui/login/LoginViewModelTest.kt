package com.tecsup.fortnite.ui.login

import com.tecsup.fortnite.data.auth.AuthRepository
import com.tecsup.fortnite.data.auth.FalloIngreso
import com.tecsup.fortnite.data.auth.MotivoFallo
import com.tecsup.fortnite.domain.model.Rol
import com.tecsup.fortnite.domain.model.Sesion
import com.tecsup.fortnite.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

// Auth falso: responde lo que la prueba le diga y cuenta las llamadas
private class AuthFalso(var respuesta: Result<Sesion>) : AuthRepository {
    var llamadas = 0
    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> {
        llamadas++
        return respuesta
    }
    override suspend fun sesionActual(): Sesion? = null
    override fun cerrarSesion() {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val despachador = StandardTestDispatcher()
    private val admin = Sesion("u1", "admin@tecsupnite.com", Rol.ADMIN)

    @Before
    fun preparar() = Dispatchers.setMain(despachador)

    @After
    fun limpiar() = Dispatchers.resetMain()

    @Test
    fun camposVaciosMuestranErrorEnCadaCampoYNoLlamanAFirebase() = runTest(despachador) {
        val auth = AuthFalso(Result.success(admin))
        val vm = LoginViewModel(auth)
        advanceUntilIdle()

        vm.ingresar("", "")
        advanceUntilIdle()

        assertEquals("Escribe tu correo", vm.errores.value.correo)
        assertEquals("Escribe tu contraseña", vm.errores.value.clave)
        assertEquals(0, auth.llamadas)
    }

    @Test
    fun correoSinArrobaMuestraComoArreglarlo() = runTest(despachador) {
        val vm = LoginViewModel(AuthFalso(Result.success(admin)))
        advanceUntilIdle()

        vm.ingresar("admin.tecsupnite.com", "clave")

        assertNotNull(vm.errores.value.correo)
        assertNull(vm.errores.value.clave)
    }

    @Test
    fun claveIncorrectaMuestraMensajeClaro() = runTest(despachador) {
        val vm = LoginViewModel(AuthFalso(Result.failure(FalloIngreso(MotivoFallo.CREDENCIALES))))
        advanceUntilIdle()

        vm.ingresar("admin@tecsupnite.com", "mala")
        advanceUntilIdle()

        val estado = vm.login.value as UiState.Error
        assertEquals("El correo o la contraseña no coinciden. Revísalos e intenta otra vez.", estado.mensaje)
        assertNull(vm.sesion.value)
    }

    @Test
    fun ingresoCorrectoGuardaLaSesion() = runTest(despachador) {
        val vm = LoginViewModel(AuthFalso(Result.success(admin)))
        advanceUntilIdle()

        vm.ingresar("  admin@tecsupnite.com ", "buena")
        advanceUntilIdle()

        assertEquals(Rol.ADMIN, vm.sesion.value?.rol)
        assertNull(vm.login.value)
    }

    @Test
    fun alEscribirSeBorraElErrorDelCampo() = runTest(despachador) {
        val vm = LoginViewModel(AuthFalso(Result.success(admin)))
        advanceUntilIdle()
        vm.ingresar("", "")

        vm.alEditarCorreo()

        assertNull(vm.errores.value.correo)
        assertEquals("Escribe tu contraseña", vm.errores.value.clave)
    }
}
