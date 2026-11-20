// Modelos de la ruta a pie: el camino por las calles y sus indicaciones.
package com.leon.campusgps.model

// Una indicación giro a giro, por ejemplo "Gira a la derecha en Jr. Moquegua"
data class PasoRuta(
    val instruccion: String,
    val maniobra: String,
    val metros: Int
)

// Camino completo: puntos por donde pasa, distancia real y tiempo caminando
data class Ruta(
    val puntos: List<Posicion>,
    val metros: Int,
    val segundos: Int,
    val pasos: List<PasoRuta>
)

sealed interface RutaUi {
    // No hay lugar elegido o todavía no se conoce tu ubicación
    data object Ninguna : RutaUi

    // Pidiendo la ruta al servicio
    data class Cargando(val destinoId: Int) : RutaUi

    // Ruta por las calles lista para dibujar
    data class Lista(val ruta: Ruta, val destinoId: Int) : RutaUi

    // Sin internet o el servicio falló: se dibuja una línea recta
    data class Recta(val destinoId: Int) : RutaUi
}
