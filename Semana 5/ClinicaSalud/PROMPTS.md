# PROMPTS — Clínica Salud+ (rama mejora-ia)

La rama `mejora-ia` parte de `main`, que tiene la app completa hecha sin IA. Aquí se
agregaron **7 mejoras**, un bloque por commit: primero la mejora funcional que pide la
rúbrica y después los pilares de la programación orientada a objetos, igual que en la
rama con IA de la Semana 2.

Cada bloque lleva el prompt tal como se usó, lo que generó y lo que hubo que corregir.
Del bloque 2 en adelante, los prompts siguen la misma estructura de cuatro partes:

| Parte | Para qué sirve |
|---|---|
| **Contexto** | Qué es la app y en qué estado está antes del cambio |
| **Tarea** | Qué se pide, en una sola frase |
| **Restricciones** | Los límites que no se pueden cruzar |
| **Criterio de aceptación** | Cómo se comprueba que el resultado es correcto |

Después de cada bloque se revisó que el código usara solo lo visto en el curso, se
compiló y se probó en el emulador.

---

## Bloque 1 — Mejora funcional: cancelar citas

La mejora funcional se hizo en dos prompts cortos, cada uno diciendo dónde, qué y qué no
tocar.

### Prompt 1a — Menú de tres puntos en cada cita

> En `CitasScreen.kt` de ClinicaSalud: en cada tarjeta de cita con estado "Confirmada",
> agrega a la derecha un ícono de tres puntos (MoreVert) que abra un DropdownMenu con una
> sola opción, "Cancelar cita", con el ícono Delete y el texto en color de error. Por ahora
> la opción solo cierra el menú. Las citas "Completada" no llevan menú. Usa un estado
> `expanded` con remember para cada tarjeta. No cambies otras pantallas.

**Qué generó.** Un `Box` con el `IconButton` de tres puntos y el `DropdownMenu`, con un
`DropdownMenuItem` que tiene `leadingIcon` y el texto en rojo.

**Qué se corrigió.** Nada. Se revisó que cada tarjeta tenga su propio `expanded` dentro de
`items`: con uno solo para toda la lista, al tocar ⋮ se abrirían todos los menús a la vez.

### Prompt 1b — Confirmar y cancelar la cita

> En `CitasScreen.kt` de ClinicaSalud: cuando se toque "Cancelar cita" en el menú de tres
> puntos, cierra el menú y muestra un AlertDialog con el título "Cancelar cita", el texto
> "¿Seguro que quieres cancelar tu cita con {médico} el {fecha}, {hora}?" y dos botones:
> "No", que cierra el diálogo, y "Sí, cancelar", que quita la cita de la lista y cierra el
> diálogo. La lista llega desde AppNavigation: cambia el parámetro a `MutableList<Cita>`.
> Guarda con remember cuál cita se quiere cancelar. Usa Button y OutlinedButton para los
> botones del diálogo. No cambies otras pantallas.

**Qué generó.** El estado `citaACancelar`, la opción del menú que lo llena y el
AlertDialog que quita la cita.

**Qué se corrigió.** Nada. Como el prompt ya pedía `Button` y `OutlinedButton` (en la app
TECSUP Fit la IA había puesto `TextButton`, que no se vio en el curso), esta vez salió
bien a la primera.

> Se descartó un "estado vacío" para Mis citas: la cita de ejemplo está Completada y no se
> puede cancelar, así que la lista nunca queda vacía.

---

## Bloque 2 — POO: Agenda

> **Contexto.** App ClinicaSalud en Jetpack Compose. Las citas son un
> `mutableStateListOf<Cita>` creado en AppNavigation que se pasa suelto a Agendar y a Mis
> citas, y cada pantalla agrega o quita directamente.
>
> **Tarea.** Convierte el manejo de citas en una clase `Agenda` con su propio estado (la
> lista) y sus propias operaciones: agendar y cancelar.
>
> **Restricciones.** Sin ViewModel. El objeto se crea con remember en AppNavigation y se
> pasa a las pantallas. Ninguna pantalla hace `add` ni `remove` sobre la lista. La app debe
> verse igual.
>
> **Criterio de aceptación.** Agendar y cancelar funcionan igual que antes, y las pantallas
> solo llaman a métodos de `Agenda`.

**Qué generó.** `data/Agenda.kt` con `agendar()` y `cancelar()`. Agendar y Mis citas
reciben la agenda y solo llaman a sus funciones.

**Qué se corrigió.** Nada. La lista `citas` quedó pública dentro de la agenda, y se cierra
en el bloque 6.

---

## Bloque 3 — Abstracción: MedicoBase

> **Contexto.** Los médicos son una `data class Medico` (id, nombre, especialidad,
> calificación, reseñas, experiencia y descripción) en `Datos.kt`, y la usan Inicio, Perfil
> del médico, Agendar, Confirmación y Agenda.
>
> **Tarea.** Crea una clase abstracta `MedicoBase` que defina el contrato de todo médico, y
> una implementación concreta `MedicoGeneral` para los médicos actuales.
>
> **Restricciones.** La clase abstracta declara **qué** sabe decir un médico (su tipo de
> atención y sus indicaciones para la consulta), no **cómo**. Pantallas y Agenda trabajan
> con el tipo abstracto. Todavía no crees tipos distintos de médico. La app debe verse
> igual.
>
> **Criterio de aceptación.** `MedicoBase` no se puede instanciar, la lista `medicos` es
> `List<MedicoBase>` y todo funciona igual.

**Qué generó.** `data/MedicoBase.kt` con la clase abstracta (`abstract val tipoAtencion`
y `abstract fun indicaciones()`) y `MedicoGeneral`. La lista `medicos` quedó como
`List<MedicoBase>`.

**Qué se corrigió.** Nada.
