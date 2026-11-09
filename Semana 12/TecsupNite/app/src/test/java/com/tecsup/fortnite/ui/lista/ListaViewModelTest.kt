package com.tecsup.fortnite.ui.lista

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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    // viewModelScope usa Dispatchers.Main; en las pruebas se cambia por uno de prueba
    private val despachador = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(despachador)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun alSincronizarBienMuestraLaListaYNotifica() = runTest(despachador) {
        val repo = RepositorioFalso()
        repo.resultadoRefrescar = Result.success(listOf(itemDePrueba("1"), itemDePrueba("2")))
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)
        advanceUntilIdle()

        val estado = vm.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(2, (estado as UiState.Exito).datos.size)
        assertEquals(listOf("Se guardaron 2 elementos"), notificador.avisos)
        assertNull(vm.aviso.value)
    }

    @Test
    fun sinInternetYSinDatosGuardadosMuestraError() = runTest(despachador) {
        val repo = RepositorioFalso()
        repo.resultadoRefrescar = Result.failure(IOException("sin red"))
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)
        advanceUntilIdle()

        val estado = vm.estado.value
        assertTrue(estado is UiState.Error)
        assertEquals("No hay conexión a internet", (estado as UiState.Error).mensaje)
        assertTrue(notificador.avisos.isEmpty())
    }

    @Test
    fun sinInternetConDatosGuardadosMuestraLoGuardadoConAviso() = runTest(despachador) {
        val repo = RepositorioFalso(iniciales = listOf(itemDePrueba("1")))
        repo.resultadoRefrescar = Result.failure(IOException("sin red"))

        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()

        val estado = vm.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(1, (estado as UiState.Exito).datos.size)
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
    }

    @Test
    fun alternarFavoritoCambiaElItemEnLaLista() = runTest(despachador) {
        val repo = RepositorioFalso()
        repo.resultadoRefrescar = Result.success(listOf(itemDePrueba("1")))
        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()

        vm.alternarFavorito("1")
        advanceUntilIdle()

        val estado = vm.estado.value as UiState.Exito
        assertTrue(estado.datos.first().esFavorito)
    }

    @Test
    fun reintentarVuelveASincronizar() = runTest(despachador) {
        val repo = RepositorioFalso()
        repo.resultadoRefrescar = Result.failure(IOException("sin red"))
        val vm = ListaViewModel(repo, NotificadorFalso())
        advanceUntilIdle()

        repo.resultadoRefrescar = Result.success(listOf(itemDePrueba("1")))
        vm.reintentar()
        advanceUntilIdle()

        assertEquals(2, repo.vecesRefrescar)
        assertTrue(vm.estado.value is UiState.Exito)
    }
}
