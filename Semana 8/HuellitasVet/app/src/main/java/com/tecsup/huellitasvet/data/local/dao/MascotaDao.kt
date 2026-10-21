package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {
    // ---------- Escritura (suspend) ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(m: Mascota): Long

    @Update
    suspend fun actualizar(m: Mascota)

    @Delete
    suspend fun eliminar(m: Mascota)

    @Query("UPDATE mascotas SET peso = :peso WHERE id = :id")
    suspend fun actualizarPeso(id: Int, peso: Double)

    // ---------- Lectura (Flow) ----------
    @Query("SELECT * FROM mascotas WHERE duenoId = :duenoId ORDER BY nombre")
    fun observarPorDueno(duenoId: Int): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    fun observarUna(id: Int): Flow<Mascota?>

    @Transaction
    @Query("SELECT * FROM mascotas ORDER BY nombre")
    fun observarTodas(): Flow<List<MascotaConDueno>>

    // Búsqueda de pacientes: texto en nombre de la mascota, raza o nombre del dueño
    // y, al mismo tiempo, filtro por especie (especie = null -> todas)
    @Transaction
    @Query(
        "SELECT m.* FROM mascotas m INNER JOIN usuarios u ON m.duenoId = u.id " +
            "WHERE (:especie IS NULL OR m.especie = :especie) " +
            "AND (m.nombre LIKE '%' || :texto || '%' OR m.raza LIKE '%' || :texto || '%' " +
            "OR u.nombre LIKE '%' || :texto || '%') ORDER BY m.nombre"
    )
    fun buscar(texto: String, especie: String?): Flow<List<MascotaConDueno>>

    @Query("SELECT COUNT(*) FROM citas WHERE mascotaId = :mascotaId")
    suspend fun contarCitas(mascotaId: Int): Int
}
