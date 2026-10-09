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
import java.time.LocalDate
import java.time.LocalTime

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

    // 6 médicos por especialidad. Cada uno atiende en 4 sedes, así cada sede
    // tiene 2 médicos de cada especialidad.
    val medicos = listOf(
        // Medicina General
        Medico(1, "Dr. Carlos Medina", 1, "Médico general", "10234", 4.7, 140, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(2, "Dra. Lucía Fernández", 1, "Médica general", "10876", 4.6, 98, "Disponible mañana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(13, "Dr. Raúl Quispe", 1, "Médico general", "15137", 4.8, 85, "Disponible mañana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(14, "Dra. Patricia Huamán", 1, "Médica general", "15274", 4.6, 110, "Disponible esta semana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(15, "Dr. Fernando Chávez", 1, "Médico general", "15411", 4.9, 72, "Disponible hoy", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(16, "Dra. Gabriela Flores", 1, "Médica general", "15548", 4.4, 95, "Disponible mañana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5)),
        // Pediatría
        Medico(3, "Dr. Jorge Salas", 2, "Pediatra", "11452", 4.8, 112, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(4, "Dra. Carmen Ruiz", 2, "Pediatra", "11978", 4.5, 64, "Disponible esta semana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(17, "Dra. Valeria Mendoza", 2, "Pediatra", "15685", 4.7, 130, "Disponible esta semana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(18, "Dr. Ricardo Paz", 2, "Pediatra", "15822", 4.5, 60, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(19, "Dra. Elena Gutiérrez", 2, "Pediatra", "15959", 4.8, 85, "Disponible mañana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(20, "Dr. Hugo Vásquez", 2, "Pediatra", "16096", 4.6, 110, "Disponible esta semana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5)),
        // Ginecología
        Medico(5, "Dra. Ana Torres", 3, "Ginecóloga", "12345", 4.9, 120, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(6, "Dra. Claudia Rojas", 3, "Ginecóloga", "12611", 4.8, 95, "Disponible mañana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(7, "Dr. Luis Ramírez", 3, "Ginecólogo", "12890", 4.7, 88, "Disponible hoy", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(8, "Dra. Mariana Soto", 3, "Ginecóloga", "13104", 4.6, 76, "Disponible esta semana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(21, "Dra. Natalia Cáceres", 3, "Ginecóloga", "16233", 4.9, 72, "Disponible hoy", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(22, "Dr. Óscar Villanueva", 3, "Ginecólogo", "16370", 4.4, 95, "Disponible mañana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5)),
        // Cardiología
        Medico(9, "Dr. Miguel Paredes", 4, "Cardiólogo", "13567", 4.8, 101, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(23, "Dra. Silvia Romero", 4, "Cardióloga", "16507", 4.7, 130, "Disponible esta semana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(24, "Dr. Javier Morales", 4, "Cardiólogo", "16644", 4.5, 60, "Disponible hoy", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(25, "Dra. Teresa Aguilar", 4, "Cardióloga", "16781", 4.8, 85, "Disponible mañana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(26, "Dr. Manuel Espinoza", 4, "Cardiólogo", "16918", 4.6, 110, "Disponible esta semana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(27, "Dra. Rocío Castillo", 4, "Cardióloga", "17055", 4.9, 72, "Disponible hoy", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5)),
        // Dermatología
        Medico(10, "Dra. Rosa Díaz", 5, "Dermatóloga", "14022", 4.7, 83, "Disponible mañana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(28, "Dr. Diego Herrera", 5, "Dermatólogo", "17192", 4.4, 95, "Disponible mañana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(29, "Dra. Andrea Salazar", 5, "Dermatóloga", "17329", 4.7, 130, "Disponible esta semana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(30, "Dr. Pablo Ríos", 5, "Dermatólogo", "17466", 4.5, 60, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(31, "Dra. Lorena Campos", 5, "Dermatóloga", "17603", 4.8, 85, "Disponible mañana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(32, "Dr. Martín Vega", 5, "Dermatólogo", "17740", 4.6, 110, "Disponible esta semana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5)),
        // Traumatología
        Medico(11, "Dr. Andrés Castro", 6, "Traumatólogo", "14455", 4.6, 70, "Disponible hoy", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(33, "Dra. Paola Navarro", 6, "Traumatóloga", "17877", 4.9, 72, "Disponible hoy", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(34, "Dr. Eduardo Ramos", 6, "Traumatólogo", "18014", 4.4, 95, "Disponible mañana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(35, "Dr. Gustavo Ortiz", 6, "Traumatólogo", "18151", 4.7, 130, "Disponible esta semana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(36, "Dra. Mónica Delgado", 6, "Traumatóloga", "18288", 4.5, 60, "Disponible hoy", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(37, "Dr. Alberto Cruz", 6, "Traumatólogo", "18425", 4.8, 85, "Disponible mañana", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5)),
        // Oftalmología
        Medico(12, "Dra. Sofía Vargas", 7, "Oftalmóloga", "14901", 4.9, 91, "Disponible esta semana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3, 4, 5)),
        Medico(38, "Dr. Sergio Palacios", 7, "Oftalmólogo", "18562", 4.6, 110, "Disponible esta semana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(2, 4)),
        Medico(39, "Dra. Verónica Peña", 7, "Oftalmóloga", "18699", 4.9, 72, "Disponible hoy", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(1, 3, 5)),
        Medico(40, "Dr. Felipe Montes", 7, "Oftalmólogo", "18836", 4.4, 95, "Disponible mañana", sedes = listOf(1, 2, 7, 8), diasAtencion = listOf(1, 2, 3)),
        Medico(41, "Dra. Diana Zevallos", 7, "Oftalmóloga", "18973", 4.7, 130, "Disponible esta semana", sedes = listOf(3, 4, 9, 10), diasAtencion = listOf(3, 4, 5)),
        Medico(42, "Dr. César Rivas", 7, "Oftalmólogo", "19110", 4.5, 60, "Disponible hoy", sedes = listOf(5, 6, 11, 12), diasAtencion = listOf(2, 4, 5))
    )

    // Sedes (locales) de la clínica en Lima y Callao
    val sedes = listOf(
        Sede(1, "SaludPlus Independencia", "Av. Túpac Amaru 456", "Independencia"),
        Sede(2, "SaludPlus La Molina", "Av. La Molina 789", "La Molina"),
        Sede(3, "SaludPlus Miraflores", "Av. José Larco 1020", "Miraflores"),
        Sede(4, "SaludPlus San Isidro", "Av. Javier Prado Este 1450", "San Isidro"),
        Sede(5, "SaludPlus Surco", "Av. Primavera 1350", "Santiago de Surco"),
        Sede(6, "SaludPlus Los Olivos", "Av. Carlos Izaguirre 865", "Los Olivos"),
        Sede(7, "SaludPlus San Juan de Lurigancho", "Av. Próceres de la Independencia 2150", "San Juan de Lurigancho"),
        Sede(8, "SaludPlus San Miguel", "Av. La Marina 2355", "San Miguel"),
        Sede(9, "SaludPlus Jesús María", "Av. Brasil 1230", "Jesús María"),
        Sede(10, "SaludPlus Ate", "Av. Nicolás Ayllón 4570", "Ate"),
        Sede(11, "SaludPlus Chorrillos", "Av. Defensores del Morro 1680", "Chorrillos"),
        Sede(12, "SaludPlus Callao", "Av. Sáenz Peña 345", "Callao")
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

    // Citas de ejemplo de otros pacientes para los próximos 28 días, así el calendario
    // muestra días con muchos, pocos o ningún cupo (como las butacas de un cine).
    // Tienen el teléfono vacío: no aparecen en "Mis citas" de ningún usuario.
    init {
        val hoy = LocalDate.now()
        var id = 1
        for (medico in medicos) {
            for (i in 0 until 28) {
                val dia = hoy.plusDays(i.toLong())
                if (dia.dayOfWeek.value !in medico.diasAtencion) continue
                // Cuántos de los 9 horarios ya están ocupados: 0, 2, 4, 7 o 9 (lleno)
                val ocupados = listOf(0, 2, 4, 7, 9)[(medico.id + dia.dayOfMonth) % 5]
                // Se reparten las horas ocupadas a lo largo del día (no siempre las primeras)
                val horas = horariosBase.filterIndexed { indice, _ ->
                    (indice * 5 + medico.id + dia.dayOfMonth) % 9 < ocupados
                }
                for (hora in horas) {
                    citas.add(Cita(id, "", medico.id, dia.toString(), hora, sedeId = medico.sedes.first()))
                    id++
                }
            }
        }
    }

    // ---------- Usuarios ----------

    // Si ya existe un usuario con el mismo teléfono o el mismo correo (any), no se registra.
    // Si no, se agrega a la lista (add).
    // NO toca usuarioActual: después de registrarse hay que iniciar sesión.
    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any {
                it.telefono == usuario.telefono || it.correo.equals(usuario.correo, ignoreCase = true)
            }
        ) return false
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

    // Inicio de sesión con correo O teléfono y contraseña (el que usa LoginScreen).
    // Busca el usuario cuyo correo o teléfono coincida (find); el correo no
    // distingue mayúsculas. Si existe, queda como usuarioActual (sesión iniciada).
    fun iniciarSesionConCorreoOTelefono(correoOTelefono: String, contrasena: String): Boolean {
        val dato = correoOTelefono.trim()
        val usuario = usuarios.find {
            (it.correo.equals(dato, ignoreCase = true) || it.telefono == dato) &&
                it.contrasena == contrasena
        }
        usuarioActual = usuario
        return usuario != null
    }

    // Cierra la sesión: ya no hay usuario actual ni sede elegida.
    fun cerrarSesion() {
        usuarioActual = null
        // La sede también se olvida: el próximo usuario elige la suya
        sedeActual = null
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
    // 2) se devuelven los horariosBase que no estén ocupados ni hayan pasado (filter)
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados && !horaYaPaso(fecha, it) }
    }

    // true si la fecha es hoy y esa hora ya pasó (no se puede reservar)
    fun horaYaPaso(fecha: String, hora: String): Boolean {
        return fecha == LocalDate.now().toString() && LocalTime.parse(hora).isBefore(LocalTime.now())
    }

    // true si el médico atiende ese día de la semana (diasAtencion)
    fun atiende(medicoId: Int, dia: LocalDate): Boolean {
        val medico = obtenerMedico(medicoId) ?: return false
        return dia.dayOfWeek.value in medico.diasAtencion
    }

    // Cupos libres de ese médico en ese día: 0 si no atiende o si ya está lleno
    fun cuposLibres(medicoId: Int, dia: LocalDate): Int {
        if (!atiende(medicoId, dia)) return 0
        return horariosDisponibles(medicoId, dia.toString()).size
    }

    // Primer día (desde hoy, hasta 4 semanas) en que el médico tiene cupos (find),
    // o null si no tiene ninguno
    fun proximoDiaConCupos(medicoId: Int): LocalDate? {
        val hoy = LocalDate.now()
        return (0 until 28).map { hoy.plusDays(it.toLong()) }.find { cuposLibres(medicoId, it) > 0 }
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
