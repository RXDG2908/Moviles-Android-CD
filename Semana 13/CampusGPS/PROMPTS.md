# Prompts utilizados - Fase 2 (rama mejora-ia)

**Laboratorio 13:** Localización geográfica (CampusGPS)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code (modelo Claude Opus 5.5)

**Mejoras de la Fase 2:**

1. Filtrar por categoría: chips Estudio, Comida, Eventos y Servicios que muestran
   solo esos lugares en el mapa y en la lista.
2. Radio de cercanía: selector de 100, 200 o 500 m que dibuja un círculo alrededor
   de mi posición y marca en la lista los lugares que quedan dentro.
3. Seguimiento en vivo: `requestLocationUpdates` convertido en un `Flow` con
   `callbackFlow`, con un botón para iniciar y detener.

**Condición que se mantuvo:** la pantalla solo habla con su ViewModel; el ViewModel
solo conoce las interfaces `UbicacionRepository` y `LugaresRepository` y no recibe
`Context`; ningún Composable calcula distancias ni decide qué lugares se muestran.

## Resumen

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un filtro por categoría con chips…» | Filtro con `combine` en el ViewModel y una fila de `FilterChip` | Las categorías salen del Repository y no de una lista escrita a mano; si el lugar elegido queda oculto, se quita la selección | Para no repetir datos y que el mapa no viaje a un lugar que ya no se ve |
| «Agrega un radio de cercanía de 100, 200 o 500 m…» | `Circle` en el mapa y un selector de chips | Descarté comparar los metros dentro de `ListaLugares`; el ViewModel publica los ids que quedan dentro | Comparar distancias es lógica y en MVVM no va en el Composable |
| «Agrega seguimiento en vivo con requestLocationUpdates y callbackFlow…» | Función `seguirPosicion(): Flow<Posicion>` en el Repository y un `Job` en el ViewModel | Agregué `awaitClose` con `removeLocationUpdates` y que el permiso recuerde si se pidió para seguir o para buscar una vez | Sin eso el GPS seguiría encendido al detener, y tras aceptar el permiso la app haría la acción equivocada |

---

## Consulta 1 · Filtrar por categoría

- **Fecha:** 18/11/2026
- **Herramienta:** Claude Code (Claude Opus 5.5)
- **Prompt:**

  > En CampusGPS (Compose + MVVM + Repository) agrega un filtro por categoría con
  > chips: Todas, Estudio, Comida, Eventos y Servicios. Al elegir uno, el mapa y la
  > lista muestran solo esos lugares. El filtro tiene que estar en el ViewModel, no
  > en el Composable. El lugar más cercano en verde debe salir de los lugares que se
  > ven. No cambies los nombres de las funciones que ya existen.

- **Qué respondió (resumen):** guardar la categoría elegida en un `MutableStateFlow`
  en el ViewModel y armar la lista visible con `combine` entre todos los lugares y
  la categoría. Para la pantalla, un composable `FiltroCategorias` con una fila de
  `FilterChip` con scroll horizontal.
- **Qué usé / qué cambié y por qué:**
  - Usé el `combine` tal cual: así `lugares` sigue siendo la misma propiedad que ya
    usaban el mapa, la lista y `masCercano`, y no hubo que tocar esas partes.
  - Las categorías de los chips salen de `lugaresRepo.obtenerLugares()` sin repetir,
    en vez de escribirlas a mano. Si se agrega un lugar nuevo con otra categoría,
    aparece su chip solo.
  - Agregué que, si el lugar seleccionado queda oculto por el filtro, se quite la
    selección. Sin esto la cámara se quedaba en un lugar que ya no estaba en la lista.
  - Tocar el chip ya elegido lo desmarca y vuelve a mostrar todas.
- **Qué aprendí:** que un filtro es un estado más del ViewModel. La pantalla no
  filtra nada: solo dice qué chip se tocó y dibuja la lista que recibe.

## Consulta 2 · Radio de cercanía

- **Fecha:** 18/11/2026
- **Herramienta:** Claude Code (Claude Opus 5.5)
- **Prompt:**

  > Agrega un selector de radio de 100, 200 o 500 m. Dibuja un Circle de Maps
  > Compose alrededor de mi posición con ese radio, y en la lista marca los lugares
  > que están dentro. Respeta MVVM: la pantalla no debe comparar distancias.

- **Qué respondió (resumen):** un `Circle` dentro del `GoogleMap` con el centro en mi
  posición y el radio en metros, un composable `SelectorRadio` con chips, y en la
  tarjeta comparar `metros <= radio` para mostrar el aviso.
- **Qué usé / qué cambié y por qué:**
  - Usé el `Circle` y el selector como los propuso.
  - Descarté la comparación dentro de la tarjeta: aunque compila, es lógica en el
    Composable. La moví al ViewModel como `dentroDelRadio`, un `StateFlow` con los
    ids de los lugares que quedan dentro, y la tarjeta solo pregunta si su id está
    en ese conjunto.
  - El radio respeta el filtro de categoría, porque `dentroDelRadio` se arma con la
    lista visible.
  - Tocar el radio ya elegido lo quita.
- **Qué aprendí:** que el radio del `Circle` va en metros, igual que las distancias
  de `Location.distanceBetween`, así que el círculo dibujado y la marca de la lista
  coinciden.

## Consulta 3 · Seguimiento en vivo

- **Fecha:** 18/11/2026
- **Herramienta:** Claude Code (Claude Opus 5.5)
- **Prompt:**

  > Agrega seguimiento en vivo: usa requestLocationUpdates de
  > FusedLocationProviderClient convertido en un Flow con callbackFlow, dentro del
  > Repository. En el ViewModel, un botón Iniciar/Detener que cancele el Flow. Cada
  > posición nueva debe actualizar el mapa, las distancias y el lugar más cercano.
  > El ViewModel no puede recibir Context.

- **Qué respondió (resumen):** agregar `fun seguirPosicion(): Flow<Posicion>` a la
  interface `UbicacionRepository`. En `UbicacionRepositoryGoogle`, un `callbackFlow`
  que crea un `LocationRequest` cada 3 segundos y un `LocationCallback` que hace
  `trySend` de cada posición. En el ViewModel, un `Job` que colecciona el Flow y
  reutiliza `actualizarDistancias`.
- **Qué usé / qué cambié y por qué:**
  - Usé el `callbackFlow` y el `Job` como los propuso.
  - Me aseguré de que tuviera `awaitClose { removeLocationUpdates(callback) }`. Sin
    eso, al pulsar Detener el Flow se cancela pero Android sigue mandando
    posiciones y gastando batería.
  - Agregué `.catch` para que un error del GPS muestre el mismo `MensajeError` de la
    Fase 1 y apague el seguimiento, en vez de cerrar la app.
  - El botón necesita permiso igual que «Obtener mi ubicación». Agregué un estado
    `quiereSeguir` en la pantalla para que, al aceptar el permiso, se inicie el
    seguimiento y no una búsqueda de una sola vez.
  - Mientras sigue en vivo, «Obtener mi ubicación» queda deshabilitado y el texto
    dice «En vivo».
- **Qué aprendí:** que `callbackFlow` sirve para convertir una API de callbacks en
  un `Flow`, y que cancelar el `Job` del ViewModel es lo que dispara `awaitClose`.
  Por eso la interface del Repository cambia, pero el ViewModel sigue sin conocer
  a Google Play Services.

---

## Revisión de MVVM después de las mejoras

| Regla | Cómo quedó |
|---|---|
| La View solo habla con su ViewModel | `PantallaMapa` llama a `filtrar`, `elegirRadio`, `iniciarSeguimiento` y `detenerSeguimiento`; `FiltroCategorias`, `SelectorRadio` y `ListaLugares` no conocen el ViewModel |
| El ViewModel no recibe `Context` | El seguimiento vive en `UbicacionRepositoryGoogle`; el ViewModel solo usa la interface |
| Las distancias no se calculan en Composables | El filtro, el radio y el orden se calculan en el ViewModel |
