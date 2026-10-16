package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.ui.graphics.Color

// Formato que reciben GraficoDona y GraficoBarras
data class DatoGrafico(
    val etiqueta: String,
    val valor: Float,
    val color: Color
)
