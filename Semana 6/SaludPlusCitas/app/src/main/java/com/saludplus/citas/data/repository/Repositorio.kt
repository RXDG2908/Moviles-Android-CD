package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

// Repositorio único de la app (object = una sola instancia para todas las pantallas).
// Todos los datos viven en colecciones en memoria: NO usar base de datos
// (ni Room, ni SQLite, ni Firebase). Se pierden al cerrar la app.
// No cambiar los nombres ni los parámetros de las funciones.
object Repositorio {

    // ---------- Colecciones ----------

    val usuarios = mutableListOf(
        Usuario("Juan Pérez", "987654321", "juan@correo.com", "123456")
    )

    // Usuario con la sesión iniciada (null si nadie inició sesión)
    var usuarioActual by mutableStateOf<Usuario?>(null)

    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención integral"),
        Especialidad(2, "Pediatría", "Niños y adolescentes"),
        Especialidad(3, "Ginecología", "Salud de la mujer"),
        Especialidad(4, "Cardiología", "Corazón y vasos sanguíneos"),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas"),
        Especialidad(6, "Traumatología", "Huesos y articulaciones"),
        Especialidad(7, "Oftalmología", "Salud visual")
    )

    val medicos = listOf(
        Medico(1, "Dr. Carlos Medina", 1, "Médico general", "10234", 4.7, 140, "Disponible hoy"),
        Medico(2, "Dra. Lucía Fernández", 1, "Médica general", "10876", 4.6, 98, "Disponible mañana"),
        Medico(3, "Dr. Jorge Salas", 2, "Pediatra", "11452", 4.8, 112, "Disponible hoy"),
        Medico(4, "Dra. Carmen Ruiz", 2, "Pediatra", "11978", 4.5, 64, "Disponible esta semana"),
        Medico(5, "Dra. Ana Torres", 3, "Ginecóloga", "12345", 4.9, 120, "Disponible hoy"),
        Medico(6, "Dra. Claudia Rojas", 3, "Ginecóloga", "12611", 4.8, 95, "Disponible mañana"),
        Medico(7, "Dr. Luis Ramírez", 3, "Ginecólogo", "12890", 4.7, 88, "Disponible hoy"),
        Medico(8, "Dra. Mariana Soto", 3, "Ginecóloga", "13104", 4.6, 76, "Disponible esta semana"),
        Medico(9, "Dr. Miguel Paredes", 4, "Cardiólogo", "13567", 4.8, 101, "Disponible hoy"),
        Medico(10, "Dra. Rosa Díaz", 5, "Dermatóloga", "14022", 4.7, 83, "Disponible mañana"),
        Medico(11, "Dr. Andrés Castro", 6, "Traumatólogo", "14455", 4.6, 70, "Disponible hoy"),
        Medico(12, "Dra. Sofía Vargas", 7, "Oftalmóloga", "14901", 4.9, 91, "Disponible esta semana")
    )

    // Horarios de atención de un día. Los ya reservados no deben mostrarse.
    val horariosBase = listOf(
        "08:00", "08:30", "09:00",
        "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00"
    )

    // mutableStateListOf: cuando se agrega o quita una cita, las pantallas
    // que la leen se vuelven a dibujar solas.
    val citas = mutableStateListOf<Cita>()

    // ---------- Usuarios ----------

    // TODO: si ya existe un usuario con el mismo teléfono (any), devolver false.
    //  Si no, agregarlo a "usuarios" (add) y devolver true.
    fun registrarUsuario(usuario: Usuario): Boolean {
        return false
    }

    // TODO: buscar en "usuarios" el que tenga ese teléfono y contraseña (find).
    //  Si existe, guardarlo en usuarioActual y devolver true; si no, false.
    fun iniciarSesion(telefono: String, contrasena: String): Boolean {
        return false
    }

    // TODO: dejar usuarioActual en null.
    fun cerrarSesion() {
    }

    // ---------- Especialidades ----------

    // TODO: devolver las especialidades cuyo nombre contenga el texto
    //  (filter + contains, sin importar mayúsculas). Texto vacío = todas.
    fun buscarEspecialidades(texto: String): List<Especialidad> {
        return emptyList()
    }

    // TODO: devolver las primeras "cantidad" especialidades (take).
    fun especialidadesDestacadas(cantidad: Int = 3): List<Especialidad> {
        return emptyList()
    }

    // TODO: buscar la especialidad por id (find).
    fun obtenerEspecialidad(id: Int): Especialidad? {
        return null
    }

    // ---------- Médicos ----------

    // TODO: buscar el médico por id (find).
    fun obtenerMedico(id: Int): Medico? {
        return null
    }

    // TODO: médicos de esa especialidad (filter), ordenados por calificación
    //  de mayor a menor (sortedByDescending).
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return emptyList()
    }

    // TODO: igual que medicosPorEspecialidad, pero además filtrando por
    //  nombre que contenga el texto.
    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return emptyList()
    }

    // ---------- Citas ----------

    // TODO: horarios libres de ese médico en esa fecha.
    //  1) De "citas", quedarse con las del médico y fecha (filter) y sacar sus horas (map).
    //  2) Devolver los "horariosBase" que NO estén en esas horas ocupadas (filter).
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        return horariosBase
    }

    // TODO: si no hay usuario con sesión, devolver null.
    //  Si ya existe una cita para ese médico, fecha y hora (any), devolver null.
    //  Si no, crear la Cita con un id nuevo, agregarla a "citas" (add) y devolverla.
    fun agendarCita(medicoId: Int, fecha: String, hora: String, motivo: String): Cita? {
        return null
    }

    // TODO: buscar la cita por id (find).
    fun obtenerCita(id: Int): Cita? {
        return null
    }

    // TODO: citas del usuario actual (filter por teléfono), ordenadas por
    //  fecha y luego por hora (sortedWith + compareBy / thenBy).
    fun citasDelUsuario(): List<Cita> {
        return emptyList()
    }

    // TODO (reto extra): quitar la cita con ese id (removeIf) y devolver si se quitó.
    fun cancelarCita(citaId: Int): Boolean {
        return false
    }
}
