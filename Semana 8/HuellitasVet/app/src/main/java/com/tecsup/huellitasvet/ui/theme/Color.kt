package com.tecsup.huellitasvet.ui.theme

import androidx.compose.ui.graphics.Color

// Dueño (esmeralda)
val DuenoPrimario = Color(0xFF059669)
val DuenoContenedor = Color(0xFFD1FAE5)
val DuenoFondo = Color(0xFFF6FDF9)

// Veterinario (azul)
val VetPrimario = Color(0xFF1D4ED8)
val VetContenedor = Color(0xFFDBEAFE)
val VetFondo = Color(0xFFF7F9FF)

// Mejora: Administrador (naranja terracota). Sigue el mismo ritmo que los otros roles
// (primario fuerte + contenedor claro + fondo teñido) y sus neutros son cálidos, sin grises.
// Contraste validado con la skill dataviz: blanco sobre el primario 5.2:1, texto secundario 6.7:1.
val AdminPrimario = Color(0xFFC2410C)
val AdminContenedor = Color(0xFFFFEDD5)
val AdminFondo = Color(0xFFFFFAF5)
val AdminTexto = Color(0xFF2A160D)
val AdminTextoSuave = Color(0xFF8A4B2A)
val AdminBorde = Color(0xFFFED7AA)
val AdminBordeFino = Color(0xFFFCE7D6)
val AdminSuperficieSuave = Color(0xFFFFF1E6)
// Administrador en modo oscuro (fondos marrón cálido, no pizarra)
val AdminPrimarioOscuro = Color(0xFFFB923C)
val AdminSobrePrimarioOscuro = Color(0xFF431407)
val AdminFondoOscuro = Color(0xFF1C120D)
val AdminSuperficieOscura = Color(0xFF2A1C15)
val AdminSuperficieSuaveOscura = Color(0xFF3A271D)
val AdminTextoOscuro = Color(0xFFFCEFE6)
val AdminTextoSuaveOscuro = Color(0xFFE8B999)
val AdminBordeOscuro = Color(0xFF5A3A2B)
val AtendidaAdmin = Color(0xFF0E7490) // "Atendida" sin gris en las pantallas del administrador (5.4:1)

// Estados de la cita
val EstadoPendiente = Color(0xFFD97706)
val EstadoConfirmada = Color(0xFF16A34A)
val EstadoAtendida = Color(0xFF475569)
val EstadoCancelada = Color(0xFFDC2626)

// Modo oscuro
val FondoOscuro = Color(0xFF0F172A)
val SuperficieOscura = Color(0xFF1E293B)

// Mejora: tipos de servicio. 3 tonos de la paleta categórica de la skill dataviz,
// validados todos contra todos (daltonismo ΔE >= 9, visión normal ΔE >= 20) en claro y oscuro.
val TipoSalud = Color(0xFF2A78D6)
val TipoEstetica = Color(0xFFEB6834)
val TipoPrevencion = Color(0xFF1BAF7A)
val TipoSaludOscuro = Color(0xFF3987E5)
val TipoEsteticaOscuro = Color(0xFFD95926)
val TipoPrevencionOscuro = Color(0xFF199E70)
