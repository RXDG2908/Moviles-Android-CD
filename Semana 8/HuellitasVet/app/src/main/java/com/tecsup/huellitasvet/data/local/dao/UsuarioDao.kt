package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.huellitasvet.data.local.entity.Usuario

@Dao
interface UsuarioDao {
    // El correo se compara sin importar mayúsculas (Ana@Correo.test = ana@correo.test)
    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    // Mejora: registro de dueños
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT COUNT(*) > 0 FROM usuarios WHERE LOWER(correo) = LOWER(:correo)")
    suspend fun existeCorreo(correo: String): Boolean

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    // Extra: recuperar al usuario guardado en DataStore al abrir la app
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?

    // Extra: "Cambiar de perfil" busca un usuario del otro rol
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?
}
