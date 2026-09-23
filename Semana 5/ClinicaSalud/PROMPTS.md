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

---

## Bloque 4 — Herencia: MedicoEspecialista y MedicoPediatra

> **Contexto.** Existe la clase abstracta `MedicoBase`, con una sola implementación
> genérica, `MedicoGeneral`.
>
> **Tarea.** Crea dos subclases que modelen los médicos reales de la clínica:
> `MedicoEspecialista` y `MedicoPediatra`.
>
> **Restricciones.** Cada subclase aporta un atributo propio con sentido. El especialista
> tiene un requisito para la consulta (algo que el paciente debe traer o hacer). El
> pediatra tiene la edad máxima de los pacientes que atiende. Ana Torres y Rosa Díaz son
> especialistas; Luis Vega es pediatra. Elimina `MedicoGeneral`. La app debe verse igual.
>
> **Criterio de aceptación.** La lista `medicos` se construye con las dos subclases y la
> app se ve igual.

**Qué generó.** `MedicoEspecialista` (con `requisito`) y `MedicoPediatra` (con
`edadMaxima`), cada una con su propio `tipoAtencion` e `indicaciones()`. Se eliminó
`MedicoGeneral`.

**Qué se corrigió.** Nada.

---

## Bloque 5 — Polimorfismo: cada médico responde a su manera

> **Contexto.** `MedicoEspecialista` y `MedicoPediatra` heredan de `MedicoBase`, pero las
> pantallas no aprovechan la diferencia.
>
> **Tarea.** Haz que cada tipo responda a su manera. El Perfil del médico muestra su tipo
> de atención y sus indicaciones; Agendar muestra la duración de la consulta, que depende
> del tipo de médico.
>
> **Restricciones.** Agrega en `MedicoBase` una función abierta `duracionConsulta()` que
> devuelva 20 minutos por defecto; el especialista la cambia a 30. Prohibido usar `is`,
> `when` sobre el tipo o cualquier comprobación de clase. No cambies la navegación.
>
> **Criterio de aceptación.** El mismo código del Perfil del médico y de Agendar muestra
> textos distintos para Ana Torres y para Luis Vega.

**Qué generó.** `open fun duracionConsulta()` en la base (20 minutos) y la versión del
especialista (30). El Perfil del médico muestra una tarjeta con el tipo de atención y las
indicaciones, y Agendar muestra "Consulta de N minutos".

**Qué se corrigió.** Nada. Ana Torres muestra "Especialista · Para tu consulta: trae tu
último electrocardiograma · Consulta de 30 minutos", y Luis Vega, "Pediatría · Atiende
pacientes de hasta 14 años · Consulta de 20 minutos". El pediatra no reemplaza
`duracionConsulta()`, así que usa la de la base: también es polimorfismo.

---

## Bloque 6 — Encapsulamiento: estado cerrado y validaciones

> **Contexto.** Ya hay abstracción, herencia y polimorfismo, pero la lista `citas` de
> `Agenda` es pública y mutable, se pueden agendar dos citas a la misma fecha y hora, y un
> médico se puede crear sin nombre o con una calificación fuera de rango.
>
> **Tarea.** Cierra el estado: las citas solo cambian a través de la agenda, y la agenda
> valida.
>
> **Restricciones.** La lista interna es privada y hacia afuera se entrega solo de
> lectura. `agendar` rechaza una fecha u hora que no estén entre las opciones y no permite
> dos citas confirmadas a la misma fecha y hora (devuelve si se pudo o no). `cancelar` solo
> cancela citas confirmadas. `MedicoBase` valida al construirse que el nombre no esté vacío
> y que la calificación esté entre 0 y 5. Si la agenda rechaza la cita, Agendar muestra un
> mensaje en rojo y no navega.
>
> **Criterio de aceptación.** Ninguna pantalla puede modificar la lista (no compila si se
> intenta), agendar dos citas a la misma fecha y hora muestra el mensaje, y lo demás
> funciona igual.

**Qué generó.** En `Agenda`, la lista pasó a `private val lista` y hacia afuera queda
`val citas: List<Cita>` de solo lectura. `agendar()` valida con `require` y devuelve
`false` si ya hay una cita confirmada a esa fecha y hora. `cancelar()` solo quita las
confirmadas. En `MedicoBase`, un bloque `init` con dos `require`. Agendar muestra "Ya
tienes una cita el Vie 27 a las 10:30 am".

**Qué se corrigió.** Nada. Para comprobar el criterio se agregó a propósito
`agenda.citas.add(...)` en una pantalla: no compiló ("Unresolved reference"), y se quitó.
