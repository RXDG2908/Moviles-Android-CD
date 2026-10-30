package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla usuarios: dueños (CLIENTE), veterinarios (ADMIN) y administradores (ADMINISTRADOR)
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String, // Rol.name
    val telefono: String = "", // mejora: 9 dígitos
    val distrito: String = ""  // mejora: distrito de Lima
)
