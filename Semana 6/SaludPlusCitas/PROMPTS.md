# PROMPTS - Fase 2 (rama mejora-ia)

**Tarea:** Clínica SaludPlus — App Paciente
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code

**Mejora obligatoria:** calendario dinámico en la Pantalla 6 (Fecha y hora) con
`java.time.LocalDate`.

---

## Prompt 1

> OK AHORA SI, REVISA LOS ULTIMOS COMMITS, HAGAMOS LA FASE CON IA

### Respuesta resumida

La IA leyó la Fase 2 de la guía y la hizo en tres commits:

1. **Días hábiles con LocalDate** (`ui/components/Calendario.kt`, `FechaHoraScreen.kt`).
   Se borró la lista fija de días. `diasHabiles()` avanza desde hoy con `plusDays(1)` y
   guarda solo los días que no son sábado ni domingo, hasta tener 5. Cada día se pasa a
   texto con `toString()` (`"yyyy-MM-dd"`), el mismo formato que usa
   `Repositorio.horariosDisponibles()`, así que el bloqueo de horarios reservados sigue
   funcionando.
2. **Flechas por semana y mes dinámico** (`FechaHoraScreen.kt`). Un estado `semana`
   (0 = semana actual) calcula los días con `diasHabiles(hoy.plusWeeks(semana))`. La
   flecha `>` suma una semana y la `<` resta una, pero está desactivada en la semana actual
   (`enabled = semana > 0`). El mes y el año salen de `mesYAnio(dias[0])`. Al tocar un día,
   `horaElegida` vuelve a `null` y los horarios se recalculan solos.
3. **Fecha en español en la Pantalla 7** (`ConfirmarCitaScreen.kt`). La fecha se muestra
   con `fechaLarga()`, por ejemplo "Miércoles 16 de setiembre 2026".

### Qué se tuvo que corregir

- Los nombres de los meses se escribieron en una lista propia porque con el idioma del
  celular sale "septiembre" y la guía usa "setiembre".

---

## Prompt 2

> Sigue a pie de la letra lo que pide, solo agrega lo que esta en la guía, no inventarse nada

### Respuesta resumida

La IA quitó lo que había agregado sin que la guía lo pidiera: la configuración de
desugaring en `app/build.gradle.kts`, el reinicio del día elegido al cambiar de semana, el
color gris de la flecha desactivada y las descripciones de los íconos de las flechas.

### Qué se tuvo que corregir

- La primera versión agregaba cosas que la guía no pedía; se dejaron solo los puntos de la
  mejora obligatoria.

---

# Mejoras pedidas por el profesor

Lo que pidió el profesor (texto original en `Semana 6/mejorasprofe.md`):

> UNA VEZ REGISTRADO, NO PUEDE MANDAR A MANDAR CITA DIRECTAMENTE, SINO MANDAR A RE-LOGIN.
> Una vez ingresado, agregar nuevo "Mis doctores", ver lista de doctores, organizados por
> categorías/especialidad.
> ESCALAR: agregar SEDES (Independencia o Molina); cuando se elija SEDE, recién ahí entrará
> a mis citas, y mis citas salen de la pestaña principal. Antes de agendar cita, primero
> salen los locales.

## Prompt 3

> Hay un md llamado mejoras profe, implementar esas en la misma rama, Mejora-ia

Junto con este mensaje se usó el prompt completo de `Semana 6/mejorasprofe.md` (sección
"Prompt para Claude Code"). Ese prompt explica el proyecto, las reglas y las 4 mejoras, en
orden y con un commit por mejora.

### Respuesta resumida

1. **Re-login después del registro** (commit "Obliga a iniciar sesion despues de
   registrarse"). Registro ya no entra a la app: navega a Login con
   `popUpTo(Rutas.REGISTRO) { inclusive = true }`, deja el teléfono escrito (la ruta de
   Login recibe `telefono` como argumento opcional) y muestra "Cuenta creada, inicia
   sesión". `registrarUsuario` no toca `usuarioActual`. Las pantallas de agendamiento
   llaman a `SesionIniciada()` (en `Componentes.kt`), que manda a Login si no hay sesión.
2. **Sedes antes de agendar** (commit "Agrega sedes y seleccion de sede antes de agendar
   cita"). Se agregaron:
   - el modelo `Sede`;
   - en el Repositorio: `sedes` (Independencia y La Molina), `sedeActual`,
     `seleccionarSede()`, `obtenerSede()` y `medicosPorSede()`;
   - el campo `sedes` en `Medico` y `sedeId` en `Cita`, ambos con valor por defecto;
   - la pantalla `SedesScreen` ("Elige tu sede").

   "Agendar cita" ahora pasa siempre por Sedes. Médicos muestra solo los de la sede
   elegida. Confirmar cita y Cita agendada muestran la sede y su dirección en vez de la
   dirección fija.
3. **Mis citas fuera del inicio y por sede** (commit "Mueve mis citas fuera del inicio y
   las filtra por sede"). Se quitó el mosaico "Mis citas" del Inicio. La pestaña "Citas"
   de la barra inferior lleva primero a Sedes si todavía no se eligió una. Mis citas
   muestra arriba la sede con "Cambiar sede" y lista solo las citas de esa sede con
   `citasDelUsuarioPorSede()`.
4. **Mis doctores** (commit "Agrega mis doctores agrupados por especialidad"). Nuevo
   mosaico en el Inicio y nueva pantalla `MisDoctoresScreen`. Muestra todos los médicos
   agrupados con `medicosAgrupadosPorEspecialidad()` (`groupBy` + `sortedByDescending`),
   con un encabezado por especialidad y un buscador por nombre (`filter` + `contains`,
   `ignoreCase`) que filtra en la pantalla. Al tocar un médico se va
   a Fecha y hora (pasando antes por Sedes si hace falta).

### Qué se tuvo que corregir

- **La rama.** El prompt pedía una rama nueva `mejoras-profe`, pero se trabajó en
  `mejora-ia` porque así se pidió en el mensaje.
- **El destino después de elegir la sede.** Primero se pasaba la ruta
  `"medicos/{especialidadId}"` como argumento de la ruta de Sedes, pero la `/` y las
  llaves rompen la URL de navegación. Se cambió por nombres simples (`IR_A_ESPECIALIDADES`,
  `IR_A_MEDICOS`, `IR_A_MIS_CITAS`, `IR_A_FECHA_HORA`) más un `id`.
- **El mosaico "Mis doctores".** La mejora 3 pide ponerlo en lugar de "Mis citas", pero la
  pantalla se crea en la mejora 4. Para que ningún commit tenga un botón que lleve a una
  pantalla que no existe, el mosaico se agregó en el commit de la mejora 4.
- **Médicos que no atienden en la sede elegida.** Desde Mis doctores se podía ir a
  agendar con un médico que no atiende en la sede ya elegida, y la cita quedaba guardada
  en esa sede. Ahora, si el médico no atiende ahí, Sedes muestra solo las sedes de ese
  médico (`sedesDelMedico()`).
- **Sin compilar en la nube.** El entorno de la IA no tiene Android SDK, así que no pudo
  correr `./gradlew assembleDebug`. Se revisó el código a mano (imports, paréntesis y
  llaves, y que las funciones de `Rutas` y `Repositorio` existan). Falta compilar y probar
  en Android Studio.

## Prompt 4

> Este si es con IA, seguir al pie de la letra todo, IA potenciada

### Respuesta resumida

La IA revisó las 4 mejoras punto por punto contra `mejorasprofe.md` y encontró una
diferencia: la función `medicosAgrupadosPorEspecialidad()` tenía un parámetro `texto`
para el buscador, pero el prompt la pide sin parámetros, con el tipo
`Map<Especialidad, List<Medico>>`. Se dejó sin parámetros, y el buscador de
`MisDoctoresScreen` filtra en la pantalla cada grupo por nombre (`filter` + `contains`,
`ignoreCase`) y oculta las especialidades que quedan vacías.

### Qué se tuvo que corregir

- La firma de `medicosAgrupadosPorEspecialidad()`, para que sea igual a la del prompt.
