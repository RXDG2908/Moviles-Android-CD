# Prompts utilizados - Fase 2 (rama mejora-ia)

**Laboratorio 14:** Hilos en Android (CentroTareas)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code (modelo Claude Opus 5.5)

**Mejoras de la Fase 2:**

1. Progreso desde el Repository: la tarea simulada pasa al Repository y entrega su
   avance como un `Flow` (`flow { emit(...) }` con `flowOn(Dispatchers.Default)`).
2. Primos con progreso: el conteo de primos avisa el porcentaje mientras avanza y la
   tarjeta muestra una barra y el texto «Calculando... 39 %».
3. Comparación de tiempos: una tarjeta nueva con una tabla que acumula cada carga
   medida y el promedio de Una por una y de A la vez.

**Condición que se mantuvo:** la pantalla solo habla con su ViewModel, el ViewModel
solo conoce la interface `TareasRepository`, y el código final no usa GlobalScope,
runBlocking ni Thread.sleep. Toda captura de error vuelve a lanzar la
`CancellationException`.

## Resumen

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Mueve la tarea con progreso al Repository y que entregue el avance como un Flow con flowOn(Dispatchers.Default)» | Función `tareaConProgreso(): Flow<Float>` en la interface y en `TareasRepositoryLocal`; el ViewModel hace `collect` y actualiza `progreso` | Dejé el `Log.d("HILO", "Dentro…")` dentro del `flow` y quité el `withContext` del ViewModel | Así Logcat sigue mostrando `main` antes y después y `DefaultDispatcher-worker` dentro, igual que en la Fase 1, y el ViewModel ya no hace el trabajo pesado |
| «Haz que contar primos muestre el porcentaje mientras avanza, sin romper la cancelación» | Propuso emitir el avance en cada número | Cambié a emitir solo cada 1 % (`hasta / 100`) y usé `currentCoroutineContext().ensureActive()` dentro del `flow` | Emitir 5 millones de veces llenaba la pantalla de recomposiciones y hacía el cálculo mucho más lento; con 100 avisos basta para la barra |
| «Agrega una tabla que acumule los tiempos de Una por una y A la vez para ver la diferencia» | Lista de tiempos en el ViewModel y una tarjeta con filas | Agregué el promedio por modo y un botón Limpiar; el tiempo solo se guarda cuando la carga sale bien | Con el promedio se ve de un vistazo la diferencia de 5 s contra 3 s, y un error no tiene un tiempo útil que comparar |

---

## Prompt 1 - Progreso desde el Repository

**Lo que pedí:** que la tarea simulada de la tarjeta «Tarea con progreso» deje de
vivir en el ViewModel y pase al Repository, entregando el avance como un `Flow`.

**Lo que respondió la IA:** agregó `fun tareaConProgreso(): Flow<Float>` a la
interface. En `TareasRepositoryLocal` la función usa `flow { }` con 100 vueltas de
`delay(40)` y `emit(i / 100f)`, y termina con `.flowOn(Dispatchers.Default)`. En el
ViewModel, `tareaLarga()` ahora hace `repo.tareaConProgreso().collect { … }`.

**Lo que revisé y adapté:**
- El `Job`, el `finally` que apaga `corriendo` y `cancelar()` siguen igual: al cancelar
  el `Job` también se cancela el `collect` y el `flow`.
- El registro «Dentro» pasó al `flow` para seguir viendo el hilo de trabajo en Logcat.

**Prueba:** la barra avanza de 0 a 100 % en unos 4 s, el contador Tócame sigue
sumando y Logcat muestra `Antes: main`, `Dentro: DefaultDispatcher-worker-1` y
`Despues: main`.

## Prompt 2 - Primos con progreso

**Lo que pedí:** ver el porcentaje del conteo de primos mientras avanza.

**Lo que respondió la IA:** un `Flow<AvancePrimos>` en el Repository, donde
`AvancePrimos` lleva el porcentaje y, al final, la cantidad. `PrimosUi.Calculando`
pasó a tener un `porcentaje`.

**Lo que revisé y adapté:**
- Emitir solo cada 1 %, porque emitir por cada número era demasiado.
- Los `when` y comparaciones de la pantalla y del ViewModel pasaron de
  `== PrimosUi.Calculando` a `is PrimosUi.Calculando`, porque ya no es un objeto único.
- Se mantuvo `contarPrimos` en la interface para no perder lo hecho en la Fase 1.

**Prueba:** con 5 000 000 la barra avanza y llega a «348513 primos»; con 1000 sigue
dando 168; Cancelar a mitad deja el estado en Cancelado.

## Prompt 3 - Comparación de tiempos

**Lo que pedí:** una tabla que acumule los tiempos de las dos formas de cargar.

**Lo que respondió la IA:** un `StateFlow<List<RegistroTiempo>>` en el ViewModel, una
función privada `mostrarExito` que pone el resultado y guarda el tiempo, y una tarjeta
«Comparación de tiempos» con la tabla.

**Lo que revisé y adapté:**
- Agregué el promedio por modo y el botón Limpiar.
- La tabla vive en el ViewModel, así que no se pierde al rotar la pantalla.

**Prueba:** después de una carga Una por una y dos A la vez, la tabla muestra 5005,
3007 y 3002 ms, con promedios de 5005 ms y 3004 ms.
