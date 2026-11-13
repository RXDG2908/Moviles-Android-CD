// Modelo de la sesion del usuario (correo y rol).
package com.tecsup.fortnite.domain.model

enum class Rol { ADMIN, USUARIO }

data class Sesion(val uid: String, val correo: String, val rol: Rol)
