package com.tecsup.huellitasvet.data.repository

import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// [G] GUIADO — Paso 4: Servicio listo; el resto TODO
class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    private val servicioDao = db.servicioDao()
    // TODO (Paso 4): usuarioDao, mascotaDao y citaDao

    // ---------- Servicios ----------
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()
    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()
    override suspend fun guardarServicio(s: Servicio) { servicioDao.insertar(s) }
    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)
    override suspend fun cambiarActivo(id: Int, activo: Boolean) = servicioDao.cambiarActivo(id, activo)

    // TODO (Paso 4): true si citaDao cuenta citas de ese servicio
    override suspend fun servicioTieneCitas(id: Int): Boolean = false

    // TODO (Paso 4): login, mascotas, citas y panel.
    // registrarAtencion va dentro de db.withTransaction { ... } (import androidx.room.withTransaction)
}
