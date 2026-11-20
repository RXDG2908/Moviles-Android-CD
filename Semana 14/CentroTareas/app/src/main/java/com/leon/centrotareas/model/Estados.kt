// Estados de pantalla del contador de primos y de las dos cargas.
package com.leon.centrotareas.model

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    // porcentaje de 0f a 1f mientras avanza el cálculo
    data class Calculando(val porcentaje: Float = 0f) : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data object Cancelado : PrimosUi
}

// Avance del conteo de primos: cantidad es null hasta que termina
data class AvancePrimos(val porcentaje: Float, val cantidad: Int? = null)

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}
