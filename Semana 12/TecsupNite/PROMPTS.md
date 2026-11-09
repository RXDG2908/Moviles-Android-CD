# Prompts utilizados - Fase 2 (rama mejora-ia)

**Laboratorio 11-12:** Compose + MVVM + Retrofit + Room + Firebase Auth (TecsupNite)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code (modelo Claude Opus 5.5)

**Mejoras de la Fase 2:**

1. Pruebas unitarias del `ListaViewModel` con un repositorio falso (requisito R12).
2. Búsqueda sin tildes ni mayúsculas, por nombre, sección y tipo.
3. Deslizar hacia abajo para actualizar la tienda.

**Condición que se mantuvo:** las pantallas no usan Retrofit, Room ni Firebase; el ViewModel solo conoce la interface `ItemRepository` y no recibe `Context`; los nombres de los métodos del contrato no cambiaron.

---

## Consulta 1 · Pruebas unitarias del ViewModel

- **Fecha:** 09/11/2026
- **Herramienta:** Claude Code (Claude Opus 5.5)
- **Prompt:**

  > Tengo un `ListaViewModel` que recibe un `ItemRepository` (interface) y un `Notificador` (interface). Escribe pruebas unitarias con JUnit 4 y kotlinx-coroutines-test, usando un repositorio falso en memoria y un notificador falso. Prueba: sincronización correcta (lista y notificación), error sin datos guardados (pantalla de error), error con datos guardados (aviso de sin conexión), marcar favorito y reintentar. No uses Mockito ni librerías extra.

- **Qué respondió (resumen):** un `RepositorioFalso` que guarda los items en un `MutableStateFlow` y deja elegir qué devuelve `refrescar()`, un `NotificadorFalso` que anota los avisos en una lista, y `ListaViewModelTest` con un `StandardTestDispatcher` puesto como `Dispatchers.Main`, porque `viewModelScope` usa el hilo principal y en las pruebas no existe.
- **Qué usé / qué cambié y por qué:**
  - Usé los dos falsos y las pruebas tal cual: el ViewModel ya dependía de interfaces, así que no hubo que tocar el código de la app para poder probarlo.
  - Agregué una sexta prueba para la mejora 3 (`deslizarParaActualizar`), que revisa que `refrescando` pase a `true` y vuelva a `false`.
  - Saqué los mensajes esperados («No hay conexión a internet», «Sin conexión: mostrando datos guardados») del ViewModel real para que la prueba compare exactamente lo que ve el usuario.
- **Qué aprendí:** que el repositorio como interface no es solo orden: es lo que permite cambiar la API y Room por algo en memoria y probar el ViewModel sin emulador ni internet. También que `advanceUntilIdle()` hace correr las corrutinas pendientes antes de revisar el estado.
- **Resultado:** `./gradlew testDebugUnitTest` → 6 pruebas de `ListaViewModelTest`, 0 fallas.

---

## Consulta 2 · Búsqueda sin tildes

- **Fecha:** 09/11/2026
- **Herramienta:** Claude Code (Claude Opus 5.5)
- **Prompt:**

  > En la pantalla de la tienda el buscador usa `titulo.contains(busqueda, ignoreCase = true)`. Si escribo "cucharina" no encuentra "Cucharína". Haz una función aparte, fácil de probar, que ignore tildes y mayúsculas, que acepte varias palabras y que busque también en la sección y el tipo. Agrega pruebas.

- **Qué respondió (resumen):** `Busqueda.kt` con `normalizar()` (usa `java.text.Normalizer` para separar las tildes de las letras y luego las borra) y `coincide(item, busqueda)`, que exige que todas las palabras aparezcan en el nombre, la sección o el tipo. Más `BusquedaTest` con 4 pruebas.
- **Qué usé / qué cambié y por qué:**
  - La función quedó fuera del composable para poder probarla con JUnit sin Compose.
  - Pedí que una búsqueda con solo espacios muestre todo, para que no quede la tienda vacía por un espacio de más.
- **Qué aprendí:** que una letra con tilde puede guardarse como la letra más un acento aparte (forma NFD), y que así se pueden quitar los acentos con una expresión regular.
- **Resultado:** 4 pruebas de `BusquedaTest`, 0 fallas. En el emulador, "cucharina" encuentra «Cucharína Cappuccina».

---

## Consulta 3 · Deslizar para actualizar

- **Fecha:** 09/11/2026
- **Herramienta:** Claude Code (Claude Opus 5.5)
- **Prompt:**

  > Quiero que en la tienda se pueda deslizar hacia abajo para sincronizar, como en las apps de redes sociales, con Material 3 y sin quitar la lista mientras carga. El estado de carga debe vivir en el ViewModel, no en la pantalla.

- **Qué respondió (resumen):** `PullToRefreshBox` de Material 3 alrededor de la grilla; en `ListaViewModel` un `StateFlow<Boolean>` llamado `refrescando` y la función `deslizarParaActualizar()`, que sincroniza con el mismo `repo.refrescar()`, muestra la notificación si sale bien o el aviso de sin conexión si falla.
- **Qué usé / qué cambié y por qué:**
  - La primera idea reutilizaba `refrescar()`, pero esa función puede poner la pantalla en «Cargando» o en «Error». Para deslizar se quiere lo contrario: que la lista siga a la vista. Por eso quedó una función aparte.
  - Se ignora un segundo deslizamiento mientras ya se está sincronizando, para no hacer dos llamadas a la API a la vez.
  - `PullToRefreshBox` todavía es experimental en Material 3, por eso lleva `@OptIn(ExperimentalMaterial3Api::class)`.
- **Qué aprendí:** que el estado de «cargando» de un gesto también es estado de la UI y va en el ViewModel como `StateFlow`, igual que la lista; la pantalla solo lo dibuja.
- **Resultado:** probado en el emulador: aparece el indicador, se sincroniza y llega la notificación «Se guardaron N elementos».
