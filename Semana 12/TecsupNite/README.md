# TecsupNite

Laboratorio 11-12 de Programación en Móviles (modalidad individual).
App Android en Kotlin con Jetpack Compose y Material 3 que muestra la tienda de cosméticos de Fortnite.

- **Alumno:** Renzo León
- **Tema:** Fortnite (API pública de https://fortnite-api.com, sin clave)
- **Paquete:** `com.tecsup.fortnite`

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

Probado en el emulador con la tienda real: PA-04 a PA-11 y PA-13 (primera apertura con notificación, detalle, favoritos, modo avión con y sin datos, sincronizar, limpiar caché y girar la pantalla).

Pendiente

- [x] Las 14 pruebas de aceptación (PA-01 a PA-14) pasan en el emulador con Firebase real; la sesión se mantiene al reabrir
- [x] Documentos `usuarios/{uid}` creados en Firestore para el admin y el usuario
- [x] Capturas de login, lista, detalle, favoritos, panel Admin y notificación (sección «Capturas»)
- [x] Fase 2 en la rama `mejora-ia`: 10 pruebas unitarias (`ListaViewModelTest` y `BusquedaTest`), búsqueda sin tildes, deslizar para actualizar y [`PROMPTS.md`](PROMPTS.md)
- [x] La Fase 2 se entrega en la rama `mejora-ia` (sin Pull Request: `main` y `mejora-ia` tienen historias separadas desde labs anteriores)
- [ ] Subir `main` y `mejora-ia` a GitHub y publicar la versión `v1.0` con el APK
