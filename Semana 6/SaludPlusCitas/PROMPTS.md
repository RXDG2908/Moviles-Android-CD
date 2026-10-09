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
