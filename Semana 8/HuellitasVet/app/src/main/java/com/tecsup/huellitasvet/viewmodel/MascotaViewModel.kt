package com.tecsup.huellitasvet.viewmodel

// [A] TÚ LO HACES — Paso 8
// Contrato:
// - guardar(mascota) con validaciones por campo: nombre >= 2, especie y sexo, año 1990..actual, peso > 0
// - eliminar(mascota): si mascotaTieneCitas, no elimina y muestra mensaje
// - historial(mascotaId): StateFlow<List<CitaConDetalle>>
