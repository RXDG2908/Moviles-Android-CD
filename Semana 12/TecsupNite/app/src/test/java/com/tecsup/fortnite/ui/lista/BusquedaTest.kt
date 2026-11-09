package com.tecsup.fortnite.ui.lista

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusquedaTest {

    private val cuchara = itemDePrueba("1", titulo = "Cucharína Cappuccina").copy(seccion = "Brainrot")

    @Test
    fun normalizarQuitaTildesYMayusculas() {
        assertEquals("cucharina", normalizar("  Cucharína "))
    }

    @Test
    fun buscaSinTildesNiMayusculas() {
        assertTrue(coincide(cuchara, "CUCHARINA"))
    }

    @Test
    fun buscaVariasPalabrasEnNombreYSeccion() {
        assertTrue(coincide(cuchara, "brainrot cappuccina"))
        assertFalse(coincide(cuchara, "brainrot vader"))
    }

    @Test
    fun busquedaVaciaMuestraTodo() {
        assertTrue(coincide(cuchara, "   "))
    }
}
