package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.model.TipoServicio
import java.time.LocalDate

// Datos de prueba (ficticios, Ley 29733). Se cargan solo la primera vez.
object DatosIniciales {

    // Fecha relativa a hoy: dia(0) = hoy, dia(-7) = hace una semana, dia(2) = pasado mañana
    private fun dia(n: Long): String = LocalDate.now().plusDays(n).toString()

    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() > 0) return

        // ---------- Servicios ----------
        val servicioDao = db.servicioDao()
        // Mejora: cada servicio tiene su tipo (Salud, Estética, Prevención)
        val salud = TipoServicio.SALUD.name
        val estetica = TipoServicio.ESTETICA.name
        val prevencion = TipoServicio.PREVENCION.name
        val consulta = servicioDao.insertar(
            Servicio(nombre = "Consulta general", descripcion = "Revisión completa de la mascota", duracionMin = 30, precio = 60.0, icono = "heart", tipo = salud)
        ).toInt()
        val vacuna = servicioDao.insertar(
            Servicio(nombre = "Vacunación", descripcion = "Aplicación de vacunas", duracionMin = 30, precio = 45.0, icono = "syringe", tipo = prevencion)
        ).toInt()
        val bano = servicioDao.insertar(
            Servicio(nombre = "Baño de perro", descripcion = "Baño con champú, secado y perfume", duracionMin = 60, precio = 50.0, icono = "bath", tipo = estetica)
        ).toInt()
        servicioDao.insertarTodos(
            listOf(
                Servicio(nombre = "Control de peso", descripcion = "Pesaje y plan de alimentación", duracionMin = 15, precio = 30.0, icono = "weight", tipo = salud),
                Servicio(nombre = "Limpieza dental", descripcion = "Retiro de sarro con ultrasonido", duracionMin = 45, precio = 120.0, icono = "tooth", tipo = salud),
                Servicio(nombre = "Baño de gato", descripcion = "Baño en seco o con agua tibia", duracionMin = 45, precio = 45.0, icono = "bath", tipo = estetica),
                Servicio(nombre = "Corte de pelo", descripcion = "Corte a tijera o máquina según la raza", duracionMin = 60, precio = 55.0, icono = "scissors", tipo = estetica),
                Servicio(nombre = "Corte de uñas", descripcion = "Corte y limado de uñas", duracionMin = 15, precio = 20.0, icono = "paw", tipo = estetica),
                Servicio(nombre = "Desparasitación", descripcion = "Control de parásitos internos y externos", duracionMin = 15, precio = 25.0, icono = "syringe", tipo = prevencion),
                Servicio(nombre = "Microchip", descripcion = "Identificación con microchip", duracionMin = 15, precio = 70.0, icono = "paw", tipo = prevencion, activo = false)
            )
        )

        // ---------- Usuarios ----------
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Ana Torres", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name, telefono = "987654321", distrito = "Miraflores"),
                Usuario(nombre = "Luis Ramos", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name, telefono = "912345678", distrito = "Santiago de Surco"),
                Usuario(nombre = "Dra. Carla Vet", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name, telefono = "955111222", distrito = "San Isidro"),
                Usuario(nombre = "Dr. Mario Paz", correo = "mario@huellitas.test", clave = "admin", rol = Rol.ADMIN.name, telefono = "955333444", distrito = "San Borja"),
                // Mejora: administrador con control total
                Usuario(nombre = "Renzo Admin", correo = "admin@huellitas.test", clave = "admin", rol = Rol.ADMINISTRADOR.name, telefono = "999000111", distrito = "Lima Cercado")
            )
        )
        val ana = db.usuarioDao().login("ana@correo.test", "1234")!!.id
        val luis = db.usuarioDao().login("luis@correo.test", "1234")!!.id

        // ---------- Mascotas ----------
        val mascotaDao = db.mascotaDao()
        val luna = mascotaDao.insertar(
            Mascota(duenoId = ana, nombre = "Luna", especie = Especie.PERRO.name, raza = "Labrador", anioNacimiento = 2021, peso = 12.8, sexo = Sexo.HEMBRA.name)
        ).toInt()
        val max = mascotaDao.insertar(
            Mascota(duenoId = ana, nombre = "Max", especie = Especie.GATO.name, raza = "Siamés", anioNacimiento = 2020, peso = 4.5, sexo = Sexo.MACHO.name)
        ).toInt()
        val rocky = mascotaDao.insertar(
            Mascota(duenoId = luis, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog", anioNacimiento = 2019, peso = 20.0, sexo = Sexo.MACHO.name)
        ).toInt()
        val michi = mascotaDao.insertar(
            Mascota(duenoId = luis, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo", anioNacimiento = 2022, peso = 3.9, sexo = Sexo.HEMBRA.name)
        ).toInt()

        // ---------- Citas (cada fecha + hora es única) ----------
        val atendida = EstadoCita.ATENDIDA.name
        db.citaDao().insertarTodos(
            listOf(
                // Pasadas ATENDIDAS (historial clínico)
                Cita(mascotaId = luna, servicioId = consulta, fecha = dia(-60), hora = "10:00", nota = "Control anual", estado = atendida, precio = 60.0,
                    diagnostico = "Mascota sana, buen estado general", tratamiento = "Ninguno", pesoRegistrado = 12.0),
                Cita(mascotaId = luna, servicioId = vacuna, fecha = dia(-30), hora = "11:00", nota = "", estado = atendida, precio = 45.0,
                    diagnostico = "Vacuna antirrábica aplicada", tratamiento = "Reposo 24 horas", pesoRegistrado = 12.4),
                Cita(mascotaId = luna, servicioId = bano, fecha = dia(-7), hora = "15:00", nota = "Pelo largo", estado = atendida, precio = 50.0,
                    diagnostico = "Piel sana, sin pulgas", tratamiento = "Champú hipoalergénico", pesoRegistrado = 12.8),
                Cita(mascotaId = max, servicioId = consulta, fecha = dia(-20), hora = "09:30", nota = "No come bien", estado = atendida, precio = 60.0,
                    diagnostico = "Gastritis leve", tratamiento = "Dieta blanda por 5 días", pesoRegistrado = 4.5),
                Cita(mascotaId = rocky, servicioId = vacuna, fecha = dia(-10), hora = "16:00", nota = "", estado = atendida, precio = 45.0,
                    diagnostico = "Vacuna séxtuple aplicada", tratamiento = "Ninguno", pesoRegistrado = 20.0),
                // Hoy
                Cita(mascotaId = michi, servicioId = consulta, fecha = dia(0), hora = "17:00", nota = "Estornuda mucho", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
                Cita(mascotaId = max, servicioId = vacuna, fecha = dia(0), hora = "17:30", nota = "", estado = EstadoCita.CONFIRMADA.name, precio = 45.0),
                // Próximos días
                Cita(mascotaId = luna, servicioId = consulta, fecha = dia(1), hora = "10:00", nota = "Revisar oreja", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
                Cita(mascotaId = rocky, servicioId = bano, fecha = dia(2), hora = "11:30", nota = "", estado = EstadoCita.CONFIRMADA.name, precio = 50.0),
                Cita(mascotaId = michi, servicioId = vacuna, fecha = dia(3), hora = "09:00", nota = "", estado = EstadoCita.CANCELADA.name, precio = 45.0),
                Cita(mascotaId = max, servicioId = consulta, fecha = dia(5), hora = "15:30", nota = "Control", estado = EstadoCita.PENDIENTE.name, precio = 60.0)
            )
        )
    }
}
