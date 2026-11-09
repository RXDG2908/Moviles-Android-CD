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

Inspirado en la tienda del juego, sobre Material 3:

- Colores y formas basados en la tienda de regalitos.shop: fondo azul rey, paneles y buscador azul marino con borde azul, acciones en amarillo eléctrico, chips en forma de píldora con su contador.
- Burbank Big Condensed (la del juego) solo en los títulos, inclinados y en mayúsculas; el resto del texto en Inter (licencia SIL Open Font License, https://github.com/rsms/inter).
- La tienda se divide como en el juego: «Destacados» (1500 V-Bucks o más, tarjetas grandes) y «Diarios».
- Cada tarjeta tiene el fondo y la franja del color de su rareza, con el nombre y el precio encima de la imagen. Los lotes y los descuentos llevan una etiqueta.
- Chips y etiquetas en forma de paralelogramo; botones y tarjetas con esquinas redondeadas.
- En horizontal la tienda usa más columnas.

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
- [ ] Capturas de login, lista, detalle, favoritos, panel Admin y notificación
- [ ] Fase 2 en la rama `mejora-ia`: pruebas unitarias del ViewModel, mejoras y `PROMPTS.md`
- [ ] Pull Request, versión `v1.0` y APK
