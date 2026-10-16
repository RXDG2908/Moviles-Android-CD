package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Sexo
import java.time.LocalDate

// Datos de prueba (ficticios, Ley 29733). Se cargan solo la primera vez.
object DatosIniciales {

    // Fecha relativa a hoy: dia(0) = hoy, dia(-7) = hace una semana, dia(2) = pasado mañana
    private fun dia(n: Long): String = LocalDate.now().plusDays(n).toString()

    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() > 0) return

        // ---------- Servicios ----------
        val servicioDao = db.servicioDao()
        val consulta = servicioDao.insertar(
            Servicio(nombre = "Consulta general", descripcion = "Revisión completa de la mascota", duracionMin = 30, precio = 60.0, icono = "heart")
        ).toInt()
        val vacuna = servicioDao.insertar(
            Servicio(nombre = "Vacunación", descripcion = "Aplicación de vacunas", duracionMin = 30, precio = 45.0, icono = "syringe")
        ).toInt()
        val bano = servicioDao.insertar(
            Servicio(nombre = "Baño y corte", descripcion = "Baño, secado y corte de pelo", duracionMin = 60, precio = 50.0, icono = "scissors")
        ).toInt()
        servicioDao.insertar(
            Servicio(nombre = "Desparasitación", descripcion = "Control de parásitos", duracionMin = 15, precio = 25.0, activo = false, icono = "syringe")
        )

        // ---------- Usuarios (ids 1, 2 y 3 en ese orden) ----------
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Ana Torres", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Luis Ramos", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Dra. Carla Vet", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
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
