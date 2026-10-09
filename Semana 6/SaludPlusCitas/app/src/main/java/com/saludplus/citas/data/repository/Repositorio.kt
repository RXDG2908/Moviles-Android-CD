package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Sede
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
        Medico(1, "Dr. Carlos Medina", 1, "Médico general", "10234", 4.7, 140, "Disponible hoy", listOf(1)),
        Medico(2, "Dra. Lucía Fernández", 1, "Médica general", "10876", 4.6, 98, "Disponible mañana", listOf(2)),
        Medico(3, "Dr. Jorge Salas", 2, "Pediatra", "11452", 4.8, 112, "Disponible hoy", listOf(1)),
        Medico(4, "Dra. Carmen Ruiz", 2, "Pediatra", "11978", 4.5, 64, "Disponible esta semana", listOf(2)),
        Medico(5, "Dra. Ana Torres", 3, "Ginecóloga", "12345", 4.9, 120, "Disponible hoy", listOf(1)),
        Medico(6, "Dra. Claudia Rojas", 3, "Ginecóloga", "12611", 4.8, 95, "Disponible mañana", listOf(2)),
        Medico(7, "Dr. Luis Ramírez", 3, "Ginecólogo", "12890", 4.7, 88, "Disponible hoy", listOf(1)),
        Medico(8, "Dra. Mariana Soto", 3, "Ginecóloga", "13104", 4.6, 76, "Disponible esta semana", listOf(2)),
        Medico(9, "Dr. Miguel Paredes", 4, "Cardiólogo", "13567", 4.8, 101, "Disponible hoy"),
        Medico(10, "Dra. Rosa Díaz", 5, "Dermatóloga", "14022", 4.7, 83, "Disponible mañana"),
        Medico(11, "Dr. Andrés Castro", 6, "Traumatólogo", "14455", 4.6, 70, "Disponible hoy"),
        Medico(12, "Dra. Sofía Vargas", 7, "Oftalmóloga", "14901", 4.9, 91, "Disponible esta semana")
    )

    // Sedes (locales) de la clínica
    val sedes = listOf(
        Sede(1, "SaludPlus Independencia", "Av. Túpac Amaru 456", "Independencia"),
        Sede(2, "SaludPlus La Molina", "Av. La Molina 789", "La Molina")
    )

    // Sede elegida por el paciente (null si todavía no eligió)
    var sedeActual by mutableStateOf<Sede?>(null)

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

    // Si ya existe un usuario con el mismo teléfono (any), no se registra.
    // Si no, se agrega a la lista (add).
    // NO toca usuarioActual: después de registrarse hay que iniciar sesión.
    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.telefono == usuario.telefono }) return false
        usuarios.add(usuario)
        return true
    }

    // Busca el usuario con ese teléfono y contraseña (find).
    // Si existe, queda como usuarioActual (sesión iniciada).
    fun iniciarSesion(telefono: String, contrasena: String): Boolean {
        val usuario = usuarios.find { it.telefono == telefono && it.contrasena == contrasena }
        usuarioActual = usuario
        return usuario != null
    }

    // Cierra la sesión: ya no hay usuario actual.
    fun cerrarSesion() {
        usuarioActual = null
    }

    // ---------- Sedes ----------

    // Busca la sede por id (find).
    fun obtenerSede(id: Int): Sede? {
        return sedes.find { it.id == id }
    }

    // Guarda la sede elegida como sedeActual.
    fun seleccionarSede(id: Int) {
        sedeActual = obtenerSede(id)
    }

    // Sedes donde atiende ese médico (filter).
    fun sedesDelMedico(medicoId: Int): List<Sede> {
        val medico = obtenerMedico(medicoId) ?: return emptyList()
        return sedes.filter { it.id in medico.sedes }
    }

    // ---------- Especialidades ----------

    // Especialidades cuyo nombre contiene el texto (filter + contains).
    // Con el texto vacío devuelve todas.
    fun buscarEspecialidades(texto: String): List<Especialidad> {
        return especialidades.filter { it.nombre.contains(texto.trim(), ignoreCase = true) }
    }

    // Las primeras especialidades de la lista (take).
    fun especialidadesDestacadas(cantidad: Int = 3): List<Especialidad> {
        return especialidades.take(cantidad)
    }

    // Busca la especialidad por id (find).
    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    // ---------- Médicos ----------

    // Busca el médico por id (find).
    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    // Médicos de la especialidad (filter), de mejor a menor calificación
    // (sortedByDescending).
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    // Médicos de la especialidad que atienden en esa sede (filter), de mejor a menor calificación.
    fun medicosPorSede(sedeId: Int, especialidadId: Int): List<Medico> {
        return medicosPorEspecialidad(especialidadId).filter { sedeId in it.sedes }
    }

    // Igual que medicosPorEspecialidad, pero además por nombre (contains).
    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto.trim(), ignoreCase = true) }
    }

    // Todos los médicos agrupados por especialidad (groupBy), cada grupo de mejor
    // a menor calificación (sortedByDescending).
    fun medicosAgrupadosPorEspecialidad(): Map<Especialidad, List<Medico>> {
        val grupos = medicos
            .sortedByDescending { it.calificacion }
            .groupBy { it.especialidadId }

        // Se arma el mapa en el orden de la lista de especialidades
        val resultado = mutableMapOf<Especialidad, List<Medico>>()
        for (especialidad in especialidades) {
            val lista = grupos[especialidad.id]
            if (lista != null) {
                resultado[especialidad] = lista
            }
        }
        return resultado
    }

    // ---------- Citas ----------

    // Horarios libres de ese médico en esa fecha:
    // 1) horas ya ocupadas = citas del médico y fecha (filter) -> sus horas (map)
    // 2) se devuelven los horariosBase que no estén ocupados (filter)
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    // Crea y guarda la cita del usuario actual.
    // Devuelve null si no hay sesión o si ese horario ya está tomado (any).
    fun agendarCita(medicoId: Int, fecha: String, hora: String, motivo: String): Cita? {
        val usuario = usuarioActual ?: return null
        val ocupado = citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora }
        if (ocupado) return null

        val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1
        // La cita se guarda con la sede elegida (0 si no hay sede)
        val sedeId = sedeActual?.id ?: 0
        val cita = Cita(nuevoId, usuario.telefono, medicoId, fecha, hora, motivo.trim(), sedeId = sedeId)
        citas.add(cita)
        return cita
    }

    // Busca la cita por id (find).
    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    // Citas del usuario actual (filter por teléfono), ordenadas por fecha
    // y luego por hora (sortedWith + compareBy / thenBy).
    fun citasDelUsuario(): List<Cita> {
        val telefono = usuarioActual?.telefono ?: return emptyList()
        return citas
            .filter { it.telefonoUsuario == telefono }
            .sortedWith(compareBy<Cita> { it.fecha }.thenBy { it.hora })
    }

    // Citas del usuario actual en esa sede (filter), ordenadas por fecha
    // y luego por hora, igual que citasDelUsuario.
    fun citasDelUsuarioPorSede(sedeId: Int): List<Cita> {
        val telefono = usuarioActual?.telefono ?: return emptyList()
        return citas
            .filter { it.telefonoUsuario == telefono && it.sedeId == sedeId }
            .sortedWith(compareBy<Cita> { it.fecha }.thenBy { it.hora })
    }

    // TODO (reto extra): quitar la cita con ese id (removeIf) y devolver si se quitó.
    fun cancelarCita(citaId: Int): Boolean {
        return false
    }
}
