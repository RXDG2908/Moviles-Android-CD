package com.tecsup.huellitasvet.data.repository

import com.tecsup.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// [E] El contrato. Los ViewModels solo conocen esta interface.
// Lectura que actualiza la pantalla -> Flow (sin suspend). Escritura o acción única -> suspend.
interface VetRepository {
    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneCitas(id: Int): Boolean

    // TODO (Paso 4): descomenta cuando existan las entidades y relaciones (Pasos 1 y 2)
    // // Usuarios
    // suspend fun login(correo: String, clave: String): Usuario?
    // // Mascotas
    // fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>>
    // fun observarMascota(id: Int): Flow<Mascota?>
    // fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>>
    // suspend fun guardarMascota(m: Mascota)
    // suspend fun eliminarMascota(m: Mascota)
    // suspend fun mascotaTieneCitas(id: Int): Boolean
    // // Citas
    // fun observarHorasOcupadas(fecha: String): Flow<List<String>>
    // suspend fun crearCita(mascotaId: Int, servicioId: Int, fecha: String, hora: String, nota: String): Long
    // fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>>
    // fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>>
    // fun observarCita(id: Int): Flow<CitaConDetalle?>
    // fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>
    // suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita)
    // suspend fun registrarAtencion(citaId: Int, diagnostico: String, tratamiento: String, peso: Double)
    // // Panel
    // fun observarCitasDeFecha(fecha: String): Flow<Int>
    // fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int>
    // fun observarCitasPorServicio(): Flow<List<ConteoServicio>>
    // fun observarIngresosDesde(fechaInicio: String): Flow<Double>
}
