package com.tecsup.huellitasvet.data.local.dao

// [A] TÚ LO HACES — Paso 2
// Contrato:
// Escritura: insertar(c): Long · actualizarEstado(id, estado) · registrarAtencion(id, diagnostico, tratamiento, peso) · obtener(id): Cita?
// Horarios: contarOcupadas(fecha, hora): Int (sin CANCELADA) · observarHorasOcupadas(fecha): Flow<List<String>>
// Lectura (@Transaction, Flow<CitaConDetalle>): observarPorDueno (INNER JOIN mascotas) · observarPorFecha · observarUna · observarHistorial
// Panel: contarPorFecha · contarPorEstado · contarPorServicio(): Flow<List<ConteoServicio>> · ingresosDesde(fechaInicio): Flow<Double> (COALESCE)
