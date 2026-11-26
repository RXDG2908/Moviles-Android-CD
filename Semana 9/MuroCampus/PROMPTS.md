# Prompts utilizados · Fase 2 (rama mejora-ia)

- **Laboratorio 09:** Servicios Web con Retrofit (MuroCampus)
- **Alumno:** Renzo Raúl León Fernández
- **Asistente de IA usado:** Claude Code (modelo Claude Opus 5.5)

## Mejoras de la Fase 2

1. **Borrar publicación:** botón Eliminar en el detalle con `@DELETE("posts/{id}")`, que revisa el código de respuesta con `Response<Unit>`. Cambia la API, el Repository, el ViewModel y la View.
2. **Búsqueda:** un campo que filtra por título las publicaciones ya descargadas, sin volver a llamar a la red. Cambia el ViewModel y la View.
3. **Encabezado con interceptor:** un `Interceptor` de OkHttp que agrega `Accept: application/json` a todas las peticiones. Cambia el RetrofitClient.
4. **Pulido de la interfaz:** avatares de color por usuario, iconos y animaciones, sin cambiar la lógica.

**Condición que se mantuvo:**
- La pantalla solo habla con su ViewModel y nunca llama a la API.
- El ViewModel solo conoce la interface `PostRepository`: no usa Retrofit, `PostApi`, `HttpException` ni `Context`.
- El Repository sigue devolviendo `Result` y vuelve a lanzar la `CancellationException`.

## Resumen

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un botón Eliminar en el detalle con @DELETE que revise el código de respuesta con Response<Unit>» | `borrar()` en `PostApi` con `Response<Unit>`. En el ViewModel revisaba `respuesta.isSuccessful` y lanzaba el error | Pasé la revisión del código al Repository: si no es exitoso lanza `HttpException(respuesta)` y `llamar` lo convierte en `Result.failure` | El ViewModel no puede conocer `Response` ni `HttpException`. Así el error 404 o 500 llega como `Result` y sale el mismo mensaje "Error del servidor (código)" |
| (mismo prompt) Mostrar que se borró | Un `Snackbar` lanzado desde el `LaunchedEffect` que observa el borrado | Lo lancé con `rememberCoroutineScope` y después limpié el estado | Al limpiar el estado, el `LaunchedEffect` se reiniciaba y cortaba el `Snackbar` antes de mostrarse. Lo probé en el emulador y no aparecía |
| «Agrega un campo de búsqueda que filtre por título las publicaciones ya descargadas» | Filtraba la lista dentro de `PantallaLista` con `filter` | Lo moví al ViewModel: `listaVisible` combina la lista y el texto con `combine` + `stateIn` | Filtrar es lógica y no va en el Composable. Además, así la búsqueda sigue funcionando junto con los chips de usuario |
| «Agrega un Interceptor de OkHttp que ponga Accept: application/json en todas las peticiones» | Un `Interceptor` agregado después del de registro | Lo puse **antes** del `HttpLoggingInterceptor` | Los interceptores se ejecutan en orden. Si va después, Logcat no muestra el encabezado y no se puede comprobar |
| «Mejora el diseño con iconos, animaciones y efectos sin tocar la lógica» | Avatares de colores, `animateItem` en la lista, comentarios que entran deslizándose, check con rebote al publicar y FAB extendido | Usé solo iconos de `material-icons-core` (Delete, Email, Search, CheckCircle, Warning) | No hace falta agregar la dependencia grande de iconos extendidos para cinco iconos |

---

## Prompt 1 · Borrar con DELETE

**Lo que pedí:** un botón Eliminar en el detalle que llame a `DELETE posts/{id}` y revise el código de respuesta.

**Lo que respondió la IA:**
- En `PostApi` agregó `@DELETE("posts/{id}") suspend fun borrar(@Path("id") id: Int): Response<Unit>`.
- En el ViewModel puso un `borrar(id)` que revisaba `respuesta.isSuccessful`.

**Lo que revisé y adapté:**
- La revisión del código pasó a `PostRepositoryRemote.borrar`. Si `isSuccessful` es falso, lanza `HttpException(respuesta)`, y la función `llamar` (la misma de la Fase 1) lo convierte en `Result.failure`.
- El ViewModel solo recibe `Result<Unit>`.
- JSONPlaceholder no borra de verdad. Por eso, al salir bien, el ViewModel quita la publicación de la lista que ya está en pantalla, en lugar de volver a descargarla.

**Prueba:**
- Logcat muestra `--> DELETE .../posts/1` y `<-- 200`.
- La app vuelve a la lista sin la publicación 1 y muestra el aviso «Publicación 1 eliminada».

## Prompt 2 · Búsqueda por título

**Lo que pedí:** un campo de búsqueda que filtre por título las publicaciones ya descargadas.

**Lo que respondió la IA:** un `OutlinedTextField` en la lista, que filtraba `e.datos` dentro del Composable.

**Lo que revisé y adapté:**
- El filtro pasó al ViewModel: `busqueda` es un `StateFlow<String>` y `listaVisible` sale de `combine(_lista, _busqueda)` con `stateIn`.
- La pantalla ahora lee `listaVisible`.
- El campo tiene una lupa y una X para borrar el texto.

**Prueba:** al escribir «dolor» solo quedan las publicaciones con esa palabra en el título. No hay ninguna petición nueva en Logcat.

## Prompt 3 · Interceptor con el encabezado Accept

**Lo que pedí:** un `Interceptor` que agregue `Accept: application/json` a todas las peticiones.

**Lo que respondió la IA:** un `Interceptor { cadena -> ... }` que copia la petición con `.header("Accept", "application/json")` y llama a `cadena.proceed(...)`.

**Lo que revisé y adapté:** lo registré antes del `HttpLoggingInterceptor`, para que el registro muestre la petición ya modificada.

**Prueba:** Logcat (filtro OkHttp) muestra `Accept: application/json` debajo de cada `--> GET`.

## Prompt 4 · Pulido de la interfaz

**Lo que pedí:** mejorar el diseño con iconos, animaciones y efectos, sin tocar la lógica.

**Lo que respondió la IA:**
- Avatar circular con un color fijo por usuario.
- Tarjetas con sombra que se deslizan al filtrar o borrar (`Modifier.animateItem()`).
- Texto de carga que late.
- Comentarios que entran con `AnimatedVisibility`.
- Check con rebote (`Animatable` + `spring`) al publicar.
- Error con icono de alerta y botón Reintentar con icono.
- Botón flotante extendido «Publicar» y chips con un check.

**Lo que revisé y adapté:**
- Comprobé que todos los iconos estén en `material-icons-core`, que ya viene con Material 3.
- Revisé que ningún efecto llame al ViewModel ni a la red.
