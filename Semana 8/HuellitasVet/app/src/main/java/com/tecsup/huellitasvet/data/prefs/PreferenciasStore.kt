package com.tecsup.huellitasvet.data.prefs

// [A] TÚ LO HACES — Paso 5
// Contrato:
// - val Context.dataStore by preferencesDataStore("ajustes") (una sola vez, a nivel de archivo)
// - Claves: perfilActivo (String), usuarioId (Int), modoOscuro (Boolean), notificaciones (true), aceptarCitas (true)
// - Un Flow por preferencia
// - suspend guardarSesion(rol, usuarioId), guardarModoOscuro(b), guardarNotificaciones(b), guardarAceptarCitas(b), cerrarSesion()
