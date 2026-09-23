# PROMPTS — TECSUP Fit (rama mejora-ia)

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

## Bloque 1 — Mejora funcional: cancelar reservas

La mejora funcional se hizo en tres prompts cortos, cada uno diciendo dónde, qué y qué
no tocar.

### Prompt 1a — Cancelar una reserva con AlertDialog

> En la app TecsupFit (Jetpack Compose), archivo `ReservasScreen.kt`: en cada tarjeta de
> reserva con estado "Confirmada", agrega a la derecha un IconButton con el ícono Delete en
> color de error. Al tocarlo, muestra un AlertDialog con el título "Cancelar reserva", el
> texto "¿Seguro que quieres cancelar tu reserva de {clase} ({horario})?" y dos botones:
> "No", que solo cierra el diálogo, y "Sí, cancelar", que quita esa reserva de la lista y
> cierra el diálogo. Las reservas "Completada" no llevan botón. La lista llega como
> parámetro desde AppNavigation (es un mutableStateListOf): cambia el tipo del parámetro a
> `MutableList<Reserva>` para poder quitar. Guarda en un estado con remember cuál reserva se
> quiere cancelar. No uses ViewModel y no cambies ninguna otra pantalla.

**Qué generó.** El botón de papelera en las reservas confirmadas, un estado
`reservaACancelar` que guarda la reserva elegida (o `null` si no hay diálogo) y el
AlertDialog que la quita de la lista.

**Qué se corrigió.** Los botones del diálogo venían como `TextButton`, que no se vio en
el curso. Se cambiaron por `OutlinedButton` ("No") y `Button` ("Sí, cancelar").

### Prompt 1b — Aviso cuando no quedan reservas próximas

> En `ReservasScreen.kt` de TecsupFit: si no hay ninguna reserva con estado "Confirmada",
> muestra arriba de la lista una tarjeta con el texto "No tienes reservas próximas" y un
> botón "Ver clases" que lleve a Inicio usando la misma función `irA` de `BarraInferior.kt`.
> Para saber si hay confirmadas, recorre la lista con un `for`. Las reservas completadas se
> siguen mostrando debajo. No cambies el diálogo de cancelar ni otras pantallas.

**Qué generó.** El recorrido con `for` que decide si hay confirmadas y la tarjeta con el
botón "Ver clases".

**Qué se corrigió.** La lógica estaba bien, pero el bloque que quedó dentro de la nueva
`Column` no estaba reindentado y costaba leerlo. Se reescribió el archivo con la sangría
correcta, sin cambiar lo que hace.

> Primero se había pensado en un "estado vacío" para cuando no hubiera reservas, pero la
> reserva de ejemplo está Completada y no se puede cancelar, así que la lista nunca queda
> vacía. Por eso el aviso mira solo las reservas confirmadas.

### Prompt 1c — Reservas activas reales en el Perfil

> En `PerfilScreen.kt` de TecsupFit: cambia la primera tarjeta de estadísticas para que
> muestre cuántas reservas con estado "Confirmada" tiene el usuario en este momento, en vez
> del número fijo 14, con la etiqueta "Reservas activas". La lista de reservas debe llegar
> como parámetro desde AppNavigation (la misma que usan Detalle y Reservas). La tarjeta de
> racha queda igual. Solo modifica `PerfilScreen.kt` y la llamada en `AppNavigation.kt`.

**Qué generó.** El parámetro con la lista y el número calculado con
`reservas.count { it.estado == "Confirmada" }`.

**Qué se corrigió.** `count { }` no se vio en el curso. Se cambió por un `for` que suma
uno por cada reserva confirmada, igual que el recorrido del prompt 1b.

**Resultado.** El Perfil muestra 0 al empezar, 2 después de reservar dos clases y 1
después de cancelar una.

---

## Bloque 2 — POO: GestorReservas

> **Contexto.** App TecsupFit en Jetpack Compose. Las reservas son un
> `mutableStateListOf<Reserva>` creado en AppNavigation que se pasa suelto a Detalle,
> Reservas y Perfil, y cada pantalla hace sus propias operaciones: agregar, quitar, recorrer
> para contar las confirmadas o para saber si hay alguna.
>
> **Tarea.** Convierte el manejo de reservas en una clase `GestorReservas` con su propio
> estado (la lista) y sus propias operaciones: reservar, cancelar, contar las activas y
> saber si hay confirmadas.
>
> **Restricciones.** Sin ViewModel. Crea el objeto con remember en AppNavigation y pásalo
> a las pantallas. Las pantallas no deben volver a recorrer la lista ni agregar o quitar
> directamente. La app debe verse y comportarse igual. Código que un alumno de cuarto ciclo
> pueda explicar.
>
> **Criterio de aceptación.** Reservar, cancelar, el aviso de Reservas y el contador del
> Perfil funcionan igual que antes, y ninguna pantalla contiene un `for` ni un `add` o
> `remove` sobre la lista.

**Qué generó.** `data/GestorReservas.kt` con `reservar()`, `cancelar()`,
`reservasActivas()` y `hayConfirmadas()`. Las pantallas reciben el gestor y solo llaman a
sus funciones.

**Qué se corrigió.** Nada. Se notó que la lista `reservas` seguía siendo pública dentro del
gestor, pero se dejó así a propósito: cerrarla es el trabajo del bloque 6.

---

## Bloque 3 — Abstracción: ClaseGimnasio

> **Contexto.** Las clases del gimnasio son una `data class Clase` (id, nombre, día,
> horario, sala, duración, descripción, cupos y horarios) en `Datos.kt`, y la usan Inicio,
> Detalle, Confirmación y GestorReservas.
>
> **Tarea.** Crea una clase abstracta `ClaseGimnasio` que defina el contrato de toda clase
> del gimnasio, y una implementación concreta `ClaseGrupal` para las clases que ya existen.
>
> **Restricciones.** La clase abstracta declara **qué** sabe decir una clase (su tipo y un
> detalle extra), no **cómo**. Las pantallas y el gestor deben trabajar con el tipo
> abstracto. Todavía no crees tipos distintos de clase. La app debe verse igual.
>
> **Criterio de aceptación.** `ClaseGimnasio` no se puede instanciar, la lista `clases` es
> `List<ClaseGimnasio>` y todo funciona igual.

**Qué generó.** `data/ClaseGimnasio.kt` con la clase abstracta (`abstract val tipo` y
`abstract fun detalleExtra()`) y `ClaseGrupal`. La lista `clases` quedó como
`List<ClaseGimnasio>`.

**Qué se corrigió.** Nada.

---

## Bloque 4 — Herencia: ClaseCardio y ClaseBienestar

> **Contexto.** Existe la clase abstracta `ClaseGimnasio`, con una sola implementación
> genérica, `ClaseGrupal`.
>
> **Tarea.** Crea dos subclases que hereden de `ClaseGimnasio` y modelen los tipos reales
> de clases del gimnasio: `ClaseCardio` y `ClaseBienestar`.
>
> **Restricciones.** Cada subclase aporta un atributo propio con sentido: las calorías
> aproximadas que se queman (cardio) y el nivel recomendado (bienestar). Cross Training,
> Spinning y Box son cardio; Yoga funcional y Pilates son bienestar. Elimina `ClaseGrupal`.
> Lo que ya se muestra en pantalla no cambia.
>
> **Criterio de aceptación.** La lista `clases` se construye con las dos subclases y la app
> se ve igual.

**Qué generó.** `ClaseCardio` (con `calorias`) y `ClaseBienestar` (con `nivel`), cada una
con su propio `tipo` y `detalleExtra()`. Se eliminó `ClaseGrupal`.

**Qué se corrigió.** Nada.

---

## Bloque 5 — Polimorfismo: cada clase responde a su manera

> **Contexto.** `ClaseCardio` y `ClaseBienestar` heredan de `ClaseGimnasio`, pero las
> pantallas todavía no aprovechan la diferencia: todas las clases se ven igual.
>
> **Tarea.** Haz que cada tipo responda a su manera y que se vea en la app. En Inicio, cada
> tarjeta muestra el tipo de clase; en Detalle se muestra el detalle extra y una
> recomendación propia de cada tipo.
>
> **Restricciones.** Agrega en `ClaseGimnasio` una función abierta `recomendacion()` con
> una recomendación general, que cada subclase reemplaza. Prohibido usar `is`, `when` sobre
> el tipo o cualquier comprobación de clase en las pantallas. No cambies la navegación.
>
> **Criterio de aceptación.** El mismo código de Detalle muestra textos distintos para
> Spinning y para Yoga, sin preguntar de qué tipo es cada clase.

**Qué generó.** `open fun recomendacion()` en la clase base y su versión propia en cada
subclase. Inicio muestra el tipo junto al horario, y Detalle muestra una tarjeta con el
tipo, el detalle extra y la recomendación.

**Qué se corrigió.** Nada. Se comprobó que las pantallas no tienen ningún `is` ni `when`
sobre el tipo. Spinning muestra "Cardio · Quema aproximada: 500 kcal · Trae agua y una
toalla", y Yoga, "Bienestar · Nivel recomendado: Principiante · Usa ropa cómoda y trae tu
mat", con el mismo código.

---

## Bloque 6 — Encapsulamiento: estado cerrado y validaciones

> **Contexto.** Ya hay abstracción, herencia y polimorfismo, pero el estado sigue expuesto:
> la lista `reservas` de `GestorReservas` es pública y mutable, cualquier pantalla podría
> agregar o quitar sin validar, y una clase se puede crear sin nombre o sin horarios.
>
> **Tarea.** Cierra el estado: que las reservas solo cambien a través de las operaciones
> del gestor, y que esas operaciones validen.
>
> **Restricciones.** La lista interna es privada y hacia afuera se entrega solo de lectura.
> `reservar` rechaza un horario que no sea de esa clase y no deja reservar dos veces la
> misma clase en el mismo horario (devuelve si se pudo o no). `cancelar` solo cancela
> reservas confirmadas. `ClaseGimnasio` valida al construirse que el nombre no esté vacío y
> que tenga al menos un horario. Si se intenta reservar algo repetido, Detalle muestra un
> mensaje en rojo y no navega.
>
> **Criterio de aceptación.** Ninguna pantalla puede modificar la lista directamente (no
> compila si se intenta), reservar la misma clase y horario dos veces muestra el mensaje, y
> todo lo demás funciona igual.

**Qué generó.** En `GestorReservas`, la lista pasó a `private val lista` y hacia afuera
queda `val reservas: List<Reserva>` de solo lectura. `reservar()` valida con `require` y
devuelve `false` si la reserva ya existe. `cancelar()` solo quita las confirmadas. En
`ClaseGimnasio`, un bloque `init` con dos `require`. Detalle muestra "Ya tienes esta clase
reservada en ese horario".

**Qué se corrigió.** Nada. Para comprobar el criterio se agregó a propósito
`gestor.reservas.add(...)` en una pantalla: no compiló ("Unresolved reference"), y se
quitó.

---

## Bloque 7 — Integración y documentación

> **Contexto.** Los seis bloques anteriores están aplicados, cada uno en su propio commit.
>
> **Tarea.** Revisa el conjunto, prueba el flujo completo y documenta en el README de
> Semana 5 qué pilar de la POO quedó en qué parte del código, junto con los prompts de cada
> bloque en PROMPTS.md.
>
> **Restricciones.** La documentación señala clases y funciones concretas, no habla de los
> pilares en general. Los prompts se transcriben tal como se usaron, con lo que hubo que
> corregir. No se cambia código.
>
> **Criterio de aceptación.** Alguien que abra el código encuentra cada pilar sin buscarlo
> a ciegas, y el flujo completo funciona.

**Qué generó.** La sección "Rama mejora-ia" del README de Semana 5, con la tabla de
pilares, la estructura de clases y las capturas, y este archivo completo.

**Verificación final.** En el emulador: filtrar clases, ver el detalle de cada tipo,
reservar, intentar reservar lo mismo otra vez, ver la reserva en Mis reservas, cancelarla
con el diálogo, ver el aviso "No tienes reservas próximas" y el contador del Perfil
bajando a 0.
