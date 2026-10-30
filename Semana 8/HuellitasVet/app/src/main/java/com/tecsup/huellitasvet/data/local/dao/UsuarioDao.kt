package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tecsup.huellitasvet.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

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

    // ---------- Mejora: administrador ----------
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY nombre")
    fun observarPorRol(rol: String): Flow<List<Usuario>>

    @Query("SELECT COUNT(*) FROM usuarios WHERE rol = :rol")
    fun contarPorRol(rol: String): Flow<Int>

    // Correo repetido sin contar al mismo usuario (al editar)
    @Query("SELECT COUNT(*) > 0 FROM usuarios WHERE LOWER(correo) = LOWER(:correo) AND id != :id")
    suspend fun existeCorreoDeOtro(correo: String, id: Int): Boolean

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    // Extra: recuperar al usuario guardado en DataStore al abrir la app
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?

    // Extra: "Cambiar de perfil" busca un usuario del otro rol
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?
}
