# Prompts utilizados - Fase 2 (rama mejora-ia)

**Laboratorio 08:** MVVM + Room (HuellitasVet)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code (modelo Claude Opus 5.5)

**Mejoras elegidas (mínimo 3):** gráfico de peso de la mascota, reprogramar cita y búsqueda de pacientes.

**Condición obligatoria:** la View no usa DAO ni Repository; el ViewModel solo conoce la interface `VetRepository`; toda función nueva del Repository se declara primero en la interface. Ninguna mejora cambia ni agrega entidades, así que la `version` de la base sigue en 2.

Capturas de la Fase 2 en [`capturas/fase2`](capturas/fase2).

---

## Prompt 1 · Gráfico de peso de la mascota

**Texto enviado a la IA:**

> Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface). Agrega en la ficha de la mascota un gráfico de línea con el peso registrado en cada cita ATENDIDA. La consulta va en CitaDao, la función en VetRepository y su implementación, y la lista de puntos se expone como StateFlow en MascotaViewModel. La View solo dibuja el gráfico con Canvas. No uses XML ni librerías externas. Muéstrame solo los archivos que cambian.

**Qué generó la IA:**

- `model/PuntoPeso.kt`: clase `PuntoPeso(fecha, peso)`. Sus nombres coinciden con los alias del SELECT, igual que `ConteoServicio`.
- `CitaDao.observarPesos(mascotaId)`: `SELECT fecha, pesoRegistrado AS peso ... WHERE estado = 'ATENDIDA' AND pesoRegistrado IS NOT NULL ORDER BY fecha, hora`, que devuelve `Flow`.
- `VetRepository.observarPesos` (declarada primero en la interface) y su implementación en `VetRepositoryRoom`.
- `MascotaViewModel.pesos(mascotaId)`: `StateFlow<List<PuntoPeso>>` con `stateIn`.
- `ui/components/graficos/GraficoLineas.kt`: línea y puntos dibujados con `Canvas` y `Path`, con la primera y la última fecha debajo.
- `FichaMascotaScreen`: tarjeta *Evolución del peso* en la pestaña *Datos*, con el mínimo, el máximo y la cantidad de registros.

**Qué tuve que corregir yo:**

- La guía dice reutilizar `GraficoLineas` "del laboratorio de Canvas", pero ese componente no estaba en el proyecto. Se creó nuevo en `ui/components/graficos`, junto a `GraficoBarras` y `GraficoDona`, con el mismo estilo.
- Con una sola atención no hay línea, y si todos los pesos son iguales, la división `(v - mínimo) / (máximo - mínimo)` sería entre 0. Se revisaron los dos casos: con un punto se dibuja solo el punto, y con pesos iguales la línea queda al medio.
- El centro de cada punto usaba `Color.White`, que en modo oscuro resalta demasiado. Se cambió por `colorScheme.surface`.

**¿Respeta MVVM?:** Sí. La consulta está en el DAO, el Repository solo la expone, el ViewModel la convierte en `StateFlow` y la View solo dibuja la lista que recibe. `GraficoLineas` no sabe de mascotas ni de Room: recibe números y etiquetas.

---

## Prompt 2 · Reprogramar cita

**Texto enviado a la IA:**

> En HuellitasVet (Compose, MVVM, Room, VetRepository interface) agrega la opción de reprogramar una cita desde Mis citas. Solo para citas PENDIENTE o CONFIRMADA y respetando la regla de 2 horas, como al cancelar. El dueño elige otra fecha (de hoy a 7 días) y otra hora entre los horarios libres; el horario actual de esa misma cita no debe salir ocupado. Antes de guardar vuelve a validar que el horario siga libre. La actualización va en CitaDao, la función en VetRepository y su implementación, las reglas en un ReprogramarCitaViewModel y la pantalla solo observa StateFlow. Reutiliza FechaChip y HoraChip. Muéstrame solo los archivos que cambian.

**Qué generó la IA:**

- `CitaDao.reprogramar(id, fecha, hora)`: `UPDATE citas SET fecha, hora, estado = 'PENDIENTE'`. La cita vuelve a pendiente para que la clínica confirme el horario nuevo.
- `VetRepository.reprogramarCita` (en la interface y en `VetRepositoryRoom`).
- `ReprogramarCitaViewModel`:
  - observa la cita con `flatMapLatest`;
  - calcula las horas ocupadas del día con `combine`, quitando el horario actual de la misma cita;
  - aplica la regla de 2 horas a la cita actual y al horario nuevo;
  - no deja guardar el mismo horario;
  - vuelve a consultar `horarioOcupado` antes de guardar.
- `ReprogramarCitaScreen`: horario actual, chips de fecha, grilla de 18 horarios en filas de 4, resumen del horario nuevo, aviso y botón *Guardar nuevo horario*.
- Ruta `reprogramar_cita/{id}` en `Rutas` y `NavVet`, el ViewModel en `VetViewModelFactory` y el botón *Reprogramar* junto a *Cancelar* en `MisCitasScreen`.

**Qué tuve que corregir yo:**

- Al probarlo en el emulador, el mensaje rojo "Elige un horario distinto al actual" seguía en pantalla después de cambiar de fecha. Se corrigió para que `elegirFecha` y `elegirHora` borren el mensaje.
- La regla de 2 horas se aplicó también al horario nuevo. Si no, se podía mover una cita a dentro de 1 hora, y esa cita ya no se podría cancelar ni volver a mover.
- Con dos botones, la fila de acciones de `CitaCard` quedaba apretada junto al chip de estado. Se usó el parámetro `accionesEnLinea = false` que ya tenía el componente, para que los botones vayan en su propia fila.

**¿Respeta MVVM?:** Sí. Todas las reglas (estado, 2 horas, horario ocupado, horario distinto) están en el ViewModel. La pantalla solo pinta lo que el ViewModel expone y avisa con `elegirFecha`, `elegirHora` y `confirmar`. El ViewModel usa solo la interface `VetRepository`.

---

## Prompt 3 · Búsqueda de pacientes

**Texto enviado a la IA:**

> En la pantalla Pacientes del veterinario, la búsqueda filtra en memoria la lista completa de mascotas. Cámbiala para que la haga Room con una consulta SQL: texto en el nombre de la mascota, la raza o el nombre del dueño, y al mismo tiempo filtro por especie (null = todas). La consulta va en MascotaDao, la función en VetRepository y su implementación, y PacientesViewModel combina el texto y la especie y observa la consulta. Agrega en la View un contador de resultados, una X para borrar el texto y un botón "Limpiar filtros". Muéstrame solo los archivos que cambian.

**Qué generó la IA:**

- `MascotaDao.buscar(texto, especie)`: `SELECT m.* FROM mascotas m INNER JOIN usuarios u ...`, con `(:especie IS NULL OR m.especie = :especie)` y `LIKE '%' || :texto || '%'` sobre la mascota, la raza y el dueño. Devuelve `Flow<List<MascotaConDueno>>` con `@Transaction`.
- `VetRepository.buscarPacientes(texto, especie)`. La implementación pasa `especie?.name` y el texto sin espacios.
- `PacientesViewModel`: `combine(_texto, _especie)` con `flatMapLatest { repo.buscarPacientes(...) }`, el `StateFlow` `hayFiltros` y la función `limpiar()`.
- `PacientesScreen`: contador "N pacientes encontrados", botón *Limpiar filtros* y una **X** en el buscador. Para la X, `VetTextField` recibió el parámetro opcional `accion`, que se dibuja como `trailingIcon`.

**Qué tuve que corregir yo:**

- Al revisar el proyecto, el buscador con filtro por especie ya existía desde la Fase 1, pero filtraba en memoria. Para que la mejora aporte algo, la búsqueda pasó a Room, y se agregaron la raza, el contador y los botones para limpiar.
- `flatMapLatest` necesita `@OptIn(ExperimentalCoroutinesApi::class)`, como ya tenían `MisCitasViewModel` y `PedirCitaViewModel`.
- Se probó en el emulador: "luis" con *Gatos* deja solo a Michi, "bull" encuentra a Rocky por la raza y *Limpiar filtros* vuelve a mostrar los 4 pacientes.

**¿Respeta MVVM?:** Sí. El SQL está en el DAO, el Repository lo expone por la interface y el ViewModel decide qué consulta observar según el texto y la especie. La View no filtra nada: solo muestra la lista y el contador, y avisa con `buscar`, `filtrar` y `limpiar`.

---

## Reflexión (5 líneas)

- La IA mejoró la velocidad: armó las cuatro capas de cada mejora siguiendo el mismo patrón del ejemplo resuelto (Servicio).
- Lo que no hizo bien a la primera fueron los detalles de uso: un mensaje de error que no se borraba al cambiar de fecha y un color fijo (blanco) que no respetaba el modo oscuro.
- Su error más claro solo apareció al probar en el emulador, no al compilar: por eso cada mejora se probó en la app antes del commit.
- Aprendí que en MVVM cada mejora entra capa por capa (DAO, Repository con su interface, ViewModel y View), y que las reglas de negocio con fechas se prueban en el ViewModel y no en la pantalla.
- También que una consulta Room con `Flow`, junto con `flatMapLatest`, mantiene la pantalla actualizada sola cuando cambian los datos o los filtros.
