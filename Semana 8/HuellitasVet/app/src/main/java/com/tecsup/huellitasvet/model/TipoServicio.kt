package com.tecsup.huellitasvet.model

// Mejora: tipo de servicio. Se guarda en Room como TipoServicio.name (columna servicios.tipo)
// Dónde se usa:
//   - PedirCitaScreen / PedirCitaViewModel: el dueño elige el tipo y el combobox muestra sus servicios
//   - InicioScreen: las 3 tarjetas de "Servicios" abren Pedir cita con el tipo ya elegido
//   - ServiciosScreen / FormularioServicioScreen: filtro por tipo y tipo de cada servicio
//   - PanelScreen: cada barra de "Citas por servicio" toma el color de su tipo (colorDeTipo en Theme.kt)
// Son 3 tipos porque la skill dataviz solo valida 3 colores distinguibles todos contra todos.
enum class TipoServicio(val etiqueta: String, val descripcion: String) {
    SALUD("Salud", "Consultas y chequeos"),
    ESTETICA("Estética", "Baños y cortes"),
    PREVENCION("Prevención", "Vacunas y cuidados");

    companion object {
        fun de(nombre: String): TipoServicio = entries.firstOrNull { it.name == nombre } ?: SALUD
    }
}
