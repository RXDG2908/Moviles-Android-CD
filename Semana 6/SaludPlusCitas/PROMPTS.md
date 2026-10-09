# PROMPTS - Fase 2 (rama mejora-ia)

**Tarea:** Clínica SaludPlus — App Paciente (complementaria al Laboratorio 06)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code

**Mejora obligatoria:** calendario dinámico en la Pantalla 6 (Fecha y hora). En la Fase 1
los días eran una lista fija (15 al 19 de setiembre). En esta fase se generan con
`java.time.LocalDate`:

- Los próximos 5 días hábiles a partir de hoy (sin sábados, domingos ni días pasados).
- Las flechas `<` y `>` avanzan o retroceden una semana; no se puede retroceder antes de
  la semana actual.
- El mes y el año ("Octubre 2026") cambian según la semana mostrada.
- Al cambiar de día, los horarios disponibles se recalculan solos y la hora elegida se
  reinicia.
- La Pantalla 7 muestra la fecha en texto en español ("Martes 16 de setiembre 2026").
- El calendario no debe romper el bloqueo de horarios ya reservados.

---

## Prompt 1

> OK AHORA SI, REVISA LOS ULTIMOS COMMITS, HAGAMOS LA FASE CON IA

### Respuesta resumida

La IA revisó los últimos commits de `main` (el esqueleto completado en la Fase 1 y la guía
GLAB-S06), leyó la Fase 2 de la guía y trajo `main` a la rama `mejora-ia` con un merge,
porque la rama ya existía con las mejoras de la Semana 5 y de la TECSUP Store. Después hizo
la mejora en tres pasos, un commit por paso:

1. **Días hábiles reales con LocalDate** (`Calendario.kt`, `FechaHoraScreen.kt`,
   `app/build.gradle.kts`). Se borró la lista fija `dias` y se creó
   `ui/components/Calendario.kt` con:
   - `diasHabiles(desde, cantidad = 5)`: avanza día por día con `plusDays(1)` y solo
     guarda los que no son sábado ni domingo (`dayOfWeek`).
   - `nombreDiaCorto()` ("Lun", "Mar"...) y `mesYAnio()` ("Octubre 2026").
   - `fechaLarga()` para la Pantalla 7.

   Cada día se pasa a texto con `toString()`, que da `"yyyy-MM-dd"`: el mismo formato que
   usan `Cita.fecha`, la ruta `confirmar_cita/{medicoId}/{fecha}/{hora}` y
   `Repositorio.horariosDisponibles()`. Por eso el bloqueo de horarios reservados sigue
   funcionando sin tocar el Repositorio.
2. **Flechas por semana y mes dinámico** (`FechaHoraScreen.kt`). Un estado `semana`
   (0 = semana actual) decide desde qué día se calculan los 5 días hábiles:
   `diasHabiles(hoy.plusWeeks(semana))`. Las flechas son `IconButton`: `>` suma 1 y `<`
   resta 1, pero queda desactivada (`enabled = semana > 0`) y en gris en la semana actual.
   El título usa `mesYAnio(dias[0])`, así que cambia solo con la semana. Al tocar un día
   se guarda la fecha y la hora vuelve a `null`; como `horarios` se calcula con la fecha
   elegida, la grilla se recalcula sola.
3. **Fecha en español en la Pantalla 7** (`ConfirmarCitaScreen.kt`). La fila "Fecha"
   pasó de `fechaCorta(fecha)` ("16/09/2026") a `fechaLarga(fecha)`
   ("Miércoles 16 de setiembre 2026").

### Qué se tuvo que corregir

- **`LocalDate` no existe en todos los celulares del proyecto.** `java.time` llegó en
  Android 8 (API 26), pero el proyecto tiene `minSdk = 24`. En Android 7 la app se
  cerraría al abrir la Pantalla 6. Se activó *core library desugaring* en
  `app/build.gradle.kts` (`isCoreLibraryDesugaringEnabled = true` y la dependencia
  `desugar_jdk_libs`), que permite usar `java.time` desde la API 24.
- **"Septiembre" en vez de "Setiembre".** Si el nombre del mes se saca con el idioma del
  celular (`Locale`), sale "septiembre", o "September" si el celular está en inglés. Los
  nombres de días y meses se escribieron en listas propias, así salen siempre en español y
  con "Setiembre", como en la guía.
- **Cambiar de semana con un día ya elegido.** La guía solo pide reiniciar la hora al
  cambiar de día. Pero si el usuario elegía un día y avanzaba de semana, "Continuar"
  seguiría habilitado con una fecha que ya no se ve. Por eso las flechas también limpian el
  día y la hora elegidos.
- **El ejemplo de la guía.** La guía pone "Martes 16 de setiembre 2026", pero el 16/09/2026
  cae miércoles. La app muestra el día real porque lo calcula `LocalDate`.

### Comprobación de la lógica

Las funciones de fecha se probaron con Java (`jshell`) usando varias fechas de inicio:

| Hoy | Semana 0 | Semana 1 |
| --- | --- | --- |
| Vie 09/10/2026 | Octubre 2026: Vie 9, Lun 12, Mar 13, Mié 14, Jue 15 | Octubre 2026: Vie 16 … Jue 22 |
| Sáb 10/10/2026 | Octubre 2026: Lun 12 … Vie 16 (el sábado no aparece) | Octubre 2026: Lun 19 … Vie 23 |
| Mié 28/10/2026 | Octubre 2026: Mié 28, Jue 29, Vie 30, Lun 2, Mar 3 | **Noviembre 2026**: Mié 4 … Mar 10 |

`fechaLarga("2026-09-16")` devuelve "Miércoles 16 de setiembre 2026".

> **Pendiente:** compilar en Android Studio, probar en el emulador (incluido reservar un
> horario y volver a la misma fecha para ver que ya no aparece) y tomar las capturas en
> `capturas/fase2`.

---

## Respuesta para la sustentación

**¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico?**
Tres cosas. Primero, `LocalDate` necesita API 26 y el proyecto empieza en la API 24, así
que hubo que activar desugaring en Gradle. Segundo, el idioma del celular daría
"septiembre", así que los meses se escribieron a mano. Tercero, al cambiar de semana el
día elegido quedaría oculto pero seleccionado; por eso las flechas también reinician el
día y la hora.
