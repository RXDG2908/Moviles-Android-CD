# PROMPTS - Fase 2 (rama mejora-ia)

**Laboratorio 06:** Menú - Navegación (TECSUP Store)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code (modelo Claude Opus 5.5)

**Mejora obligatoria:** agregar un badge con contador en el ítem "Favoritos" del drawer,
que muestre cuántos productos marcó el usuario como favorito desde el DropdownMenu de
cada producto. Una acción en el DropdownMenu debe verse reflejada en el Drawer.

---

## Prompt 1

> procede con la FASE 2, donde seguiras a pie de la letra el md donde dice fase 2, aqui
> agregaras lo que te pide y los prompts en un md luego los commits de la fase 2, luego
> toma las capturas correspondientes a la fase 1 y luego a la fase 2, y guardalas en el
> mismo proyecto para que pueda armar mi word mas adelante

### Respuesta resumida

La IA dividió la mejora en tres pasos, con un commit por paso:

1. **Lista de favoritos compartida** (`AppNavegacion.kt`, `HomeScreen.kt`, `TarjetaProducto.kt`).
   La lista `favoritos` (ids de productos) se crea con `mutableStateListOf` en
   `AppNavegacion`, que está por encima del NavHost y del drawer. `TarjetaProducto` ya
   no decide nada: recibe `esFavorito` y avisa con `onCambiarFavorito`. La opción del
   DropdownMenu cambia a "Quitar de favoritos" cuando el producto ya está marcado, y la
   tarjeta muestra un corazón morado junto al precio.
2. **Badge en el drawer** (`AppDrawer.kt`). `AppDrawer` recibe `cantidadFavoritos =
   favoritos.size` y `ItemMenu` recibe un `contador` opcional. Si es mayor que 0, el
   `NavigationDrawerItem` dibuja un `Badge` morado en su parámetro `badge`; con 0
   favoritos no se ve nada.
3. **Pantalla Favoritos con la lista real** (`FavoritosScreen.kt`). Filtra `productos`
   con los ids de `favoritos` y los muestra con la misma `TarjetaProducto`. Desde ahí
   también se pueden quitar, y el badge baja solo porque es la misma lista.

Además tomó las capturas de la Fase 1 (desde `main`) y de la Fase 2 y las guardó en
`capturas/fase1` y `capturas/fase2`.

### Qué se tuvo que corregir

- **Error de compilación en el paso 2.** Al agregar el parámetro `contador` a
  `ItemMenu`, la llamada `ItemMenu("Cerrar sesion", false, onCerrarSesion)` dejó de
  compilar: el tercer argumento ahora era `contador` (un `Int`) y no `onClick`. Se
  corrigió pasando el argumento por nombre: `ItemMenu("Cerrar sesion", false, onClick =
  onCerrarSesion)`.
- **La rama `mejora-ia` ya existía.** Tenía las mejoras con IA de la Semana 5 (TecsupFit,
  ClinicaSalud y la guía), así que no se podía crear de nuevo con `git checkout -b`
  sin perder ese trabajo. En vez de borrarla, se trajo `main` a la rama con un merge
  ("Trae la Fase 1 de TecsupStore (Semana 6) desde main"), y la Fase 2 se hizo encima.
- **Capturas.** La primera captura del DropdownMenu salió sin el menú porque se tomó
  durante la animación de apertura; se repitió esperando un poco más.

---

## Prompt 2

> cierra mi sesion de git de todo el dispositivo, olvida el pdf solo termina con la fase 2

### Respuesta resumida

Se descartó el PDF y se terminó la Fase 2: el commit del paso 2 con la corrección, la
pantalla Favoritos, este archivo y las capturas. Después se hizo `git push origin
mejora-ia` y al final se cerró la sesión de git del equipo.

### Qué se tuvo que corregir

- El emulador se había cerrado a mitad del trabajo, así que se volvió a abrir para
  comprobar el badge y tomar las capturas de la Fase 2.

---

## Respuestas para la sustentación

**¿Cómo se enteró el contador del drawer de lo que pasa en el DropdownMenu de cada
producto?** La lista de favoritos no vive en la tarjeta. Si estuviera ahí con `remember`,
cada tarjeta tendría su propia copia y el drawer no podría leerla. Por eso se subió a
`AppNavegacion`, que es el padre común del drawer y de las pantallas. Las tarjetas
reciben `esFavorito` y avisan con `onCambiarFavorito`. El drawer recibe `favoritos.size`.
Como la lista es un `mutableStateListOf`, al agregar o quitar un id Compose vuelve a
dibujar todo lo que la lee: la tarjeta, el badge y la pantalla Favoritos.

**¿Por qué el badge desaparece con 0 favoritos?** Dentro del parámetro `badge` del
`NavigationDrawerItem` solo se dibuja el `Badge` si `contador > 0`. Mostrar un "0" no
aporta nada.

## Capturas

| Captura | Qué muestra |
| --- | --- |
| `capturas/fase2/01-drawer-sin-favoritos.png` | Drawer sin badge (0 favoritos) |
| `capturas/fase2/02-favorito-agregado.png` | Audifonos marcado: corazón junto al precio |
| `capturas/fase2/03-drawer-badge-1.png` | Badge con 1 |
| `capturas/fase2/04-inicio-con-favoritos.png` | Varios productos marcados |
| `capturas/fase2/05-dropdown-quitar-de-favoritos.png` | La opción cambia a "Quitar de favoritos" |
| `capturas/fase2/06-drawer-badge-3.png` | Badge con 3 |
| `capturas/fase2/07-pantalla-favoritos.png` | Pantalla Favoritos con los 3 productos |
| `capturas/fase2/08-drawer-favoritos-activo.png` | Favoritos activo en el drawer, con badge |
| `capturas/fase2/09-favorito-quitado.png` | Se quitó uno desde la pantalla Favoritos |
| `capturas/fase2/10-drawer-badge-2.png` | El badge baja a 2 |
