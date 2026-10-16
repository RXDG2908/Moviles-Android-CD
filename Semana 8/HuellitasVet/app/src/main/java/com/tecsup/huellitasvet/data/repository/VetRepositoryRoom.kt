package com.tecsup.huellitasvet.data.repository

import androidx.room.withTransaction
import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.model.ConteoServicio
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.UltimaVisita
import kotlinx.coroutines.flow.Flow

// Implementación del contrato con Room
class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    private val servicioDao = db.servicioDao()
    private val usuarioDao = db.usuarioDao()
    private val mascotaDao = db.mascotaDao()
    private val citaDao = db.citaDao()

    // ---------- Usuarios ----------
    override suspend fun login(correo: String, clave: String): Usuario? = usuarioDao.login(correo, clave)
    override suspend fun obtenerUsuario(id: Int): Usuario? = usuarioDao.obtener(id)
    override suspend fun obtenerUsuarioPorRol(rol: Rol): Usuario? = usuarioDao.obtenerPorRol(rol.name)

    // ---------- Servicios ----------
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()
    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()
    override suspend fun guardarServicio(s: Servicio) { servicioDao.insertar(s) }
    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)
    override suspend fun cambiarActivo(id: Int, activo: Boolean) = servicioDao.cambiarActivo(id, activo)
    override suspend fun servicioTieneCitas(id: Int): Boolean = citaDao.contarDeServicio(id) > 0

    // ---------- Mascotas ----------
    override fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>> = mascotaDao.observarPorDueno(duenoId)
    override fun observarMascota(id: Int): Flow<Mascota?> = mascotaDao.observarUna(id)
    override fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>> = mascotaDao.observarTodas()

    override suspend fun guardarMascota(m: Mascota) {
        // id = 0 -> nueva; id > 0 -> editar
        if (m.id == 0) mascotaDao.insertar(m) else mascotaDao.actualizar(m)
    }

    override suspend fun eliminarMascota(m: Mascota) = mascotaDao.eliminar(m)
    override suspend fun mascotaTieneCitas(id: Int): Boolean = mascotaDao.contarCitas(id) > 0

    // ---------- Citas ----------
    override fun observarHorasOcupadas(fecha: String): Flow<List<String>> = citaDao.observarHorasOcupadas(fecha)

    override suspend fun horarioOcupado(fecha: String, hora: String): Boolean =
        citaDao.contarOcupadas(fecha, hora) > 0

    override suspend fun crearCita(mascotaId: Int, servicioId: Int, fecha: String, hora: String, nota: String): Long {
        // Copia el precio del servicio en ese momento ("foto" del precio)
        val servicio = servicioDao.obtener(servicioId) ?: return -1
        val cita = Cita(
            mascotaId = mascotaId,
            servicioId = servicioId,
            fecha = fecha,
            hora = hora,
            nota = nota,
            estado = EstadoCita.PENDIENTE.name,
            precio = servicio.precio,
            fechaCreacion = System.currentTimeMillis()
        )
        return citaDao.insertar(cita)
    }

    override fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>> = citaDao.observarPorDueno(duenoId)
    override fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>> = citaDao.observarPorFecha(fecha)
    override fun observarCita(id: Int): Flow<CitaConDetalle?> = citaDao.observarUna(id)
    override fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>> = citaDao.observarHistorial(mascotaId)

    override suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita) = citaDao.actualizarEstado(id, estado.name)

    override suspend fun registrarAtencion(citaId: Int, diagnostico: String, tratamiento: String, peso: Double) {
        // Transacción: si algo falla, no se guarda nada a medias
        db.withTransaction {
            val cita = citaDao.obtener(citaId) ?: return@withTransaction
            citaDao.registrarAtencion(citaId, diagnostico, tratamiento, peso)
            mascotaDao.actualizarPeso(cita.mascotaId, peso)
        }
    }

    override fun observarUltimasVisitas(): Flow<List<UltimaVisita>> = citaDao.observarUltimasVisitas()

    // ---------- Panel ----------
    override fun observarCitasDeFecha(fecha: String): Flow<Int> = citaDao.contarPorFecha(fecha)
    override fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int> = citaDao.contarPorEstado(estado.name)
    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> = citaDao.contarPorServicio()
    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> = citaDao.ingresosDesde(fechaInicio)
    override fun observarAtencionesDesde(fechaInicio: String): Flow<Int> = citaDao.contarAtendidasDesde(fechaInicio)
}
