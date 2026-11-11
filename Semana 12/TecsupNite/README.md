# TecsupNite

Laboratorio 11-12 de Programación en Móviles (modalidad individual).
App Android en Kotlin con Jetpack Compose y Material 3 que muestra la tienda de cosméticos de Fortnite.

- **Alumno:** Renzo León
- **Tema:** Fortnite (API pública de https://fortnite-api.com, sin clave)
- **Paquete:** `com.tecsup.fortnite`
- **Variante:** tienda de hoy (`/v2/shop`) con detalle por cosmÃ©tico; sin pistas musicales, instrumentos ni autos.
- **Reparto de tareas:** trabajo individual; todo el proyecto (cÃ³digo, Firebase, pruebas, README y capturas) lo hizo Renzo LeÃ³n.

## Qué hace

- Login con correo y contraseña (Firebase Authentication) con dos roles: `admin` y `usuario`.
- Lista de la tienda de hoy con foto, rareza, precio en V-Bucks, buscador y chips por tipo.
- Detalle de un cosmético (segundo endpoint).
- Favoritos que se guardan en el celular y se conservan al sincronizar.
- Modo sin conexión: sin internet muestra lo guardado con un aviso.
- Notificación local «Se guardaron N elementos» al terminar de sincronizar.
- Panel Admin (solo admin): total guardado, total de favoritos, Sincronizar y Limpiar caché.
- Sesión que se mantiene al reabrir y botón Cerrar sesión.

Se descartan las pistas musicales, los instrumentos y los autos de la tienda; solo se muestran cosméticos de Battle Royale. Los packs aparecen como «Lote», con su nombre, su imagen y el precio del pack; el detalle muestra el primer cosmético e indica que el precio es del lote. Si un cosmético también se vende solo, se muestra solo ese y no el lote.

Los favoritos se conservan aunque ya no estén en la tienda de hoy, por eso también pueden aparecer en la lista.

## Diseño

Basado en la tienda de regalitos.shop (proyecto propio, reglas en `WEB.md` de ese repo), adaptado a Android con Material 3:

- **Colores** (tokens de la web): azul tienda `#0a63d8` en degradado sólido, sin dibujos de fondo; paneles `rgba(5,22,138,.55)`; chips con borde `#00c3fe`; amarillo `#fdff00` como único acento de acción.
- **Fuentes**: Burbank (la del juego) solo en títulos, inclinada -6° y en mayúsculas; Inter en el resto (licencia SIL OFL, https://github.com/rsms/inter). Los precios van en Inter negra itálica.
- **Formas**: botones en paralelogramo sin radio; paneles 20 dp, tarjetas 16 dp, campos 10 dp; chips en píldora con su contador.
- **Orden de la tienda** (igual que la web): cabecera con «Cambia en» (cuenta regresiva al cambio de tienda), búsqueda y orden (Como en Fortnite / Menor precio / Mayor precio), filtros por tipo y por «Con descuento» / «Termina hoy», resumen «N objetos en M secciones» y las secciones reales de la tienda (One Piece, Star Wars…).
- **Tarjetas** (Plantilla Prime de la web): fondo con los colores oficiales de cada oferta en el 85 % de abajo, la imagen asoma por arriba y se difumina para leer el precio; solo llevan el tipo y el precio (el nombre va en el detalle), placas «X de descuento» y «Termina hoy», y botón amarillo para favoritos.
- **Barra inferior**: la pestaña activa en amarillo con una raya arriba.
- **Hoja del producto** (como el `<dialog class="hoja">` de la web, skill `law-of-figure-ground`): se abre encima de la tienda con un velo detrás; borde celeste, imagen arriba con los colores oficiales que se funden abajo, tipo y «Termina hoy», nombre en Burbank, precio grande, descripción, fichas de detalles (rareza, sección, serie, llegada) y los botones «A favoritos» / «Volver» siempre abajo.

## Capturas

Tomadas en el emulador con la tienda real del 09/11/2026 y la cuenta admin.

| Login | Tienda de hoy | Secciones y favoritos |
|---|---|---|
| ![Login](capturas/01-login.jpg) | ![Tienda](capturas/02-tienda.jpg) | ![Secciones](capturas/03-tienda-secciones.jpg) |

| Hoja del producto | Favoritos | Notificación | Panel Admin |
|---|---|---|---|
| ![Detalle](capturas/04-detalle.jpg) | ![Favoritos](capturas/05-favoritos.jpg) | ![Notificación](capturas/06-notificacion.jpg) | ![Admin](capturas/07-admin.jpg) |

## Endpoints

| Uso | Endpoint |
| --- | --- |
| Lista | `GET https://fortnite-api.com/v2/shop?language=es` |
| Detalle | `GET https://fortnite-api.com/v2/cosmetics/br/{id}?language=es` |

## Arquitectura (MVVM)

```
data/remote   ApiService, RetrofitClient, dto/ItemDto
data/local    ItemEntity, ItemDao, AppDatabase (Room)
data/auth     AuthRepository (interface), FirebaseAuthRepository
data/repository  ItemRepository (interface), ItemRepositoryImpl
data/mapper   Dto -> Entity -> Item
domain/model  Item, Sesion, Rol
notification  Notificador (interface), NotificadorAndroid
ui/           pantallas y ViewModels: login, lista, detalle, favoritos, admin
```

Room es la fuente de verdad: la lista que ve el usuario sale siempre de Room y Retrofit solo la actualiza.

## Cómo compilar

1. Abrir la carpeta `TecsupNite` en Android Studio.
2. Crear un proyecto en Firebase con el paquete `com.tecsup.fortnite` y descargar `google-services.json` dentro de `app/` (no se sube a Git).
3. En Firebase activar Authentication con correo y contraseña, y crear Firestore.
4. Crear los usuarios de prueba (ver abajo) y pegar las reglas de seguridad.
5. Ejecutar en un emulador con Google Play.

## Usuarios de prueba

Se crean en Authentication → Usuarios. La contraseña la define quien los crea.

| Rol | Correo |
| --- | --- |
| Admin | `admin@tecsupnite.com` |
| Usuario | `usuario@tecsupnite.com` |

En Firestore, colección `usuarios`, un documento por usuario con el **UID** como ID y los campos `rol` (`admin` o `usuario`) y `nombre`.

Reglas de Firestore:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

## Avance

Fase 1 (rama `main`, sin IA)

- [x] H1 a H4: proyecto base, estructura MVVM y lista
- [x] H5 a H7: Retrofit, lista con estados y detalle
- [x] H8 a H11: Room, favoritos y modo sin conexión
- [x] H12 a H15: login con roles, panel Admin y notificación local
- [x] H16: este README

Pruebas de aceptaciÃ³n de la guÃ­a, hechas en el emulador `Lab03` con la tienda real y Firebase real:rnrn| CÃ³digo | Caso | Resultado |rn|---|---|---|rn| PA-01 | Ingresar con el usuario `usuario` | Entra a la tienda; no ve la pestaÃ±a Admin |rn| PA-02 | Ingresar con el usuario `admin` | Entra y ve la pestaÃ±a Admin con el panel |rn| PA-03 | ContraseÃ±a incorrecta | Mensaje claro; no entra |rn| PA-04 | Correo o clave vacÃ­os | ValidaciÃ³n en pantalla; no llama a Firebase |rn| PA-05 | Primera apertura con internet | Cargando, luego la lista y la notificaciÃ³n Â«Se guardaron N elementosÂ» |rn| PA-06 | Tocar un elemento | Detalle con datos del segundo endpoint |rn| PA-07 | Marcar favorito y cerrar la app | Al reabrir sigue en Favoritos |rn| PA-08 | Modo aviÃ³n con datos guardados | Se ve la lista con el aviso de sin conexiÃ³n |rn| PA-09 | Modo aviÃ³n sin datos | Pantalla de error con Reintentar |rn| PA-10 | Sincronizar despuÃ©s de marcar un favorito | El favorito no se pierde |rn| PA-11 | Admin pulsa Limpiar cachÃ© | Totales en 0; al sincronizar vuelven los datos |rn| PA-12 | Cerrar sesiÃ³n y reabrir | Vuelve al login |rn| PA-13 | Girar la pantalla | Se mantiene el estado; la tienda usa mÃ¡s columnas |rn| PA-14 | Negar el permiso de notificaciones | La app funciona normal, sin avisos |

Estado de la entrega

- [x] Las 14 pruebas de aceptaciÃ³n pasan (tabla de arriba)
- [x] Documentos `usuarios/{uid}` creados en Firestore para el admin y el usuario
- [x] Capturas de login, lista, detalle, favoritos, panel Admin y notificaciÃ³n (secciÃ³n Â«CapturasÂ»)
- [x] Fase 2 hecha en la rama `mejora-ia` (pruebas unitarias, mejoras con IA y `PROMPTS.md`); se entrega en esa rama, sin Pull Request porque `main` y `mejora-ia` tienen historias separadas desde labs anteriores
- [x] `main` y `mejora-ia` subidas a GitHub
- [ ] Publicar la versiÃ³n `v1.0` en GitHub (Releases) con el APK de depuraciÃ³n adjunto
