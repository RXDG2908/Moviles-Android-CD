package com.tecsup.fortnite.data.auth

// Motivo por el que no se pudo ingresar, sin depender de las clases de Firebase.
// Así el ViewModel elige el mensaje sin importar nada de Firebase.
enum class MotivoFallo { CREDENCIALES, SIN_CONEXION, DEMASIADOS_INTENTOS, OTRO }

class FalloIngreso(val motivo: MotivoFallo, causa: Throwable? = null) : Exception(motivo.name, causa)
