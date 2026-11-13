# Dónde está cada cosa en TecsupNite

Esta guía sirve para presentar el Laboratorio 11-12 al profesor. Dice dónde está cada archivo, para qué sirve y en qué línea se cumple cada requisito de la guía. Si el profesor pregunta «¿dónde haces esto?», busca el requisito en la sección 4 y abre el archivo que indica.

- **Alumno:** Renzo León (trabajo individual)
- **App:** TecsupNite, la tienda de cosméticos de Fortnite (API pública fortnite-api.com, sin clave)
- **Paquete:** `com.tecsup.fortnite`
- **Repositorio:** https://github.com/RXDG2908/Moviles-Android-CD, carpeta `Semana 12/TecsupNite`
- **Versión publicada:** [release v1.0](https://github.com/RXDG2908/Moviles-Android-CD/releases/tag/v1.0), con el APK listo para instalar

## Contenido

1. [Qué entregar y dónde está](#1-qué-entregar-y-dónde-está)
2. [Las dos ramas: main y mejora-ia](#2-las-dos-ramas-main-y-mejora-ia)
3. [Mapa de carpetas](#3-mapa-de-carpetas)
4. [Cada requisito y su archivo](#4-cada-requisito-y-su-archivo)
5. [Para qué sirve cada archivo](#5-para-qué-sirve-cada-archivo)
6. [Firebase: qué se configuró](#6-firebase-qué-se-configuró)
7. [Guion de la demostración](#7-guion-de-la-demostración)
8. [Preguntas que puede hacer el profesor](#8-preguntas-que-puede-hacer-el-profesor)

## 1. Qué entregar y dónde está

Cada pieza de la entrega tiene un solo lugar. Esta tabla sirve para abrirla rápido:

| Qué | Dónde | Para qué sirve |
|---|---|---|
| Código de la app | `Semana 12/TecsupNite/` en ambas ramas | El proyecto de Android Studio |
| Informe en Word y PDF | `Semana 12/LAB12-LEON-RENZO-2026.docx` y `.pdf` (rama `main`) | El informe que se subió al aula virtual |
| APK para instalar | [Release v1.0](https://github.com/RXDG2908/Moviles-Android-CD/releases/tag/v1.0), archivo `TecsupNite-v1.0.apk` | Instalar la app sin compilarla |
| README | `Semana 12/TecsupNite/README.md` | Resumen, diseño, capturas, cómo compilar y tabla de pruebas |
| Capturas | `Semana 12/TecsupNite/capturas/` (8 imágenes) | Evidencia de cada pantalla |
| Prompts de la IA | `Semana 12/TecsupNite/PROMPTS.md` (solo `mejora-ia`) | Las 4 consultas a la IA de la Fase 2 |
| Pruebas unitarias | `app/src/test/...` (solo `mejora-ia`) | 15 pruebas que se corren sin emulador |
| Reglas de Firestore | `Semana 12/TecsupNite/firestore.rules` | Quién puede leer los roles |
| Entrega en Canvas | Curso Programación en Móviles, tarea «Laboratorio 12» | Intento 2 del 11/11/2026 con el PDF de 13 páginas |

El archivo `google-services.json` **no** está en GitHub a propósito: tiene la clave del proyecto de Firebase. Va dentro de `app/` en la computadora donde se compila.

## 2. Las dos ramas: main y mejora-ia

La guía pide dos fases y cada una vive en su rama:

- **`main` (Fase 1):** la app completa que pide la guía, con las 14 pruebas de aceptación pasadas. Aquí también está el informe.
- **`mejora-ia` (Fase 2):** todo lo de `main` más las mejoras hechas con IA. El APK del release sale de esta rama.

Lo que **solo** está en `mejora-ia`:

- **Pruebas unitarias:** `ListaViewModelTest` (6), `BusquedaTest` (4) y `LoginViewModelTest` (5)
- **Búsqueda sin tildes ni mayúsculas:** `ui/lista/Busqueda.kt`
- **Deslizar hacia abajo para actualizar:** en `ListaScreen.kt` y `ListaViewModel.kt`
- **Login más claro:** error debajo de cada campo, ver contraseña y mensajes según el error de Firebase (`LoginScreen.kt`, `LoginViewModel.kt`, `data/auth/FalloIngreso.kt`)
- **`PROMPTS.md`:** qué se le pidió a la IA y qué se cambió

Para mostrar una rama en Android Studio, cámbiala con el menú de Git (abajo a la derecha) y espera a que Gradle sincronice.

## 3. Mapa de carpetas

Todo el código Kotlin está en `app/src/main/java/com/tecsup/fortnite/`. En esta guía esa ruta se abrevia como `fortnite/`.

```text
Semana 12/
├── LAB12-LEON-RENZO-2026.docx / .pdf   informe (rama main)
└── TecsupNite/
    ├── README.md            resumen del proyecto
    ├── GUIA-PRESENTACION.md esta guía
    ├── PROMPTS.md           consultas a la IA (mejora-ia)
    ├── capturas/            8 capturas de la app
    ├── firebase.json, firestore.rules, .firebaserc
    └── app/
        ├── build.gradle.kts dependencias
        └── src/
            ├── main/
            │   ├── AndroidManifest.xml
            │   ├── res/font/   Burbank e Inter
            │   └── java/com/tecsup/fortnite/
            │       ├── data/remote/      Retrofit: llamar a la API
            │       ├── data/local/       Room: base de datos del celular
            │       ├── data/auth/        Firebase: login y rol
            │       ├── data/repository/  une la API con Room
            │       ├── data/mapper/      convierte los datos
            │       ├── domain/model/     Item, Sesion, Rol
            │       ├── notification/     notificación local
            │       └── ui/               pantallas y ViewModels
            └── test/   pruebas unitarias (mejora-ia)
```

Los datos siguen un solo camino, y por eso la app funciona sin internet:

```text
API de Fortnite → Retrofit → Repositorio → Room → ViewModel → Pantalla
```

La pantalla nunca lee la API directamente: siempre lee Room, y la API solo actualiza Room.

## 4. Cada requisito y su archivo

Las líneas son de la rama `mejora-ia`. Cuando en `main` cambian, van entre paréntesis.

| Req. | Qué pide la guía | Archivo y línea |
|---|---|---|
| R01 | Login con correo y contraseña, validando campos | `fortnite/ui/login/LoginViewModel.kt:82` validación (main: línea 36) · `fortnite/data/auth/FirebaseAuthRepository.kt:19` ingreso con Firebase (main: 14) |
| R02 | Dos roles guardados en Firestore | `fortnite/data/auth/FirebaseAuthRepository.kt:53` lee el campo `rol` (main: 38) · `fortnite/ui/navigation/AppNav.kt:146` muestra la pestaña Admin solo al admin (main: 139) |
| R03 | Lista con estados de carga, error y vacío | `fortnite/ui/UiState.kt:3` los tres estados · `fortnite/ui/lista/ListaScreen.kt:88` error y `:91` vacío (main: 83 y 86) |
| R04 | Detalle con el segundo endpoint | `fortnite/data/remote/ApiService.kt:17` endpoint de detalle · `fortnite/ui/detalle/DetalleViewModel.kt:28` lo pide · `fortnite/ui/navigation/AppNav.kt:176` abre la hoja (main: 166) |
| R05 | Arquitectura MVVM | `fortnite/data/repository/ItemRepository.kt` contrato · `fortnite/ui/ViewModelFactory.kt:16` crea los ViewModel · `fortnite/AppContainer.kt:15` arma las dependencias |
| R06 | Room como fuente de verdad | `fortnite/data/local/ItemEntity.kt:6` tabla · `fortnite/data/local/ItemDao.kt:9` consultas · `fortnite/data/local/AppDatabase.kt:6` base · `fortnite/data/repository/ItemRepositoryImpl.kt:23` API → Room |
| R07 | Favoritos que no se pierden | `fortnite/data/local/ItemDao.kt:33` marcar/quitar · `fortnite/data/repository/ItemRepositoryImpl.kt:25` guarda los favoritos antes de sincronizar · `fortnite/ui/favoritos/FavoritosScreen.kt:19` pantalla |
| R08 | Modo sin conexión | `fortnite/ui/lista/ListaViewModel.kt:55` aviso «Sin conexión» (main: 51) · `fortnite/ui/lista/ListaScreen.kt:153` muestra el aviso (main: 145) · `fortnite/data/repository/ItemRepositoryImpl.kt:40` atrapa la falta de internet |
| R09 | Notificación local | `fortnite/notification/NotificadorAndroid.kt:24` canal · `:43` abre la app al tocarla · `:56` muestra el aviso · `fortnite/MainActivity.kt:27` pide el permiso |
| R10 | Panel Admin | `fortnite/ui/admin/AdminScreen.kt:35` pantalla · `fortnite/ui/admin/AdminViewModel.kt:36` sincronizar y `:48` limpiar caché |
| R11 | Sesión que se mantiene y cerrar sesión | `fortnite/ui/login/LoginViewModel.kt:40` revisa la sesión al abrir (main: 29) · `fortnite/ui/navigation/AppNav.kt:115` botón de cerrar sesión (main: 108) |
| R12 | Pruebas del ViewModel (Fase 2) | `app/src/test/java/com/tecsup/fortnite/ui/lista/ListaViewModelTest.kt` · `Falsos.kt` repositorio falso (solo `mejora-ia`) |
| R13 | Mejora con IA documentada | `PROMPTS.md` y la rama `mejora-ia` |

Las 14 pruebas de aceptación (PA-01 a PA-14) están en la tabla del `README.md`, sección «Avance», y en la sección 7 del informe.

## 5. Para qué sirve cada archivo

Cada carpeta tiene una sola tarea. Los archivos marcados con ★ solo existen en `mejora-ia`.

### Arranque de la app

- **`TecsupNiteApp.kt`:** se ejecuta primero y crea el `AppContainer`
- **`AppContainer.kt`:** crea una sola vez la base de datos, Retrofit, Firebase y el notificador, y se los pasa a quien los necesite
- **`MainActivity.kt`:** pide el permiso de notificaciones y muestra la navegación
- **`AndroidManifest.xml`:** declara el permiso de internet y el de notificaciones

### `data/remote`: hablar con la API

- **`ApiService.kt`:** los dos endpoints, `v2/shop` (tienda de hoy) y `v2/cosmetics/br/{id}` (detalle)
- **`RetrofitClient.kt`:** configura Retrofit con la dirección `https://fortnite-api.com/`
- **`dto/ItemDto.kt`:** la forma del JSON que devuelve la API

### `data/local`: guardar en el celular

- **`ItemEntity.kt`:** la tabla `items` de Room, con el campo `esFavorito`
- **`ItemDao.kt`:** las consultas: listar, favoritos, contar, marcar favorito y reemplazar la tienda
- **`AppDatabase.kt`:** la base de datos `tecsupnite.db`, versión 2

### `data/auth`: login y roles

- **`AuthRepository.kt`:** el contrato: iniciar sesión, sesión actual y cerrar sesión
- **`FirebaseAuthRepository.kt`:** lo cumple con Firebase Authentication y lee el rol en Firestore
- ★ **`FalloIngreso.kt`:** separa el motivo del error: contraseña incorrecta, sin internet o muchos intentos

### `data/repository` y `data/mapper`: unir todo

- **`ItemRepository.kt`:** el contrato que usan los ViewModel
- **`ItemRepositoryImpl.kt`:** descarga la tienda, quita pistas, instrumentos y autos, conserva los favoritos y guarda en Room
- **`Mappers.kt`:** convierte JSON → tabla de Room → modelo `Item` que usa la pantalla

### `domain/model`

- **`Item.kt`:** un cosmético como lo ve la app
- **`Sesion.kt`:** el usuario conectado y su rol (`ADMIN` o `USUARIO`)

### `notification`

- **`Notificador.kt`:** el contrato, para poder usar uno falso en las pruebas
- **`NotificadorAndroid.kt`:** crea el canal «sincronizacion» y muestra «Se guardaron N elementos»

### `ui`: pantallas y ViewModels

- **`UiState.kt`:** los estados `Cargando`, `Exito` y `Error`
- **`ViewModelFactory.kt`:** crea cada ViewModel con lo que necesita
- **`navigation/AppNav.kt`:** decide si mostrar el login o la app, la barra inferior y la pestaña Admin
- **`login/`:** pantalla de ingreso y su ViewModel
- **`lista/`:** la tienda (`ListaScreen`), la tarjeta de cada cosmético (`ItemCard`) y su ViewModel; ★ `Busqueda.kt`
- **`detalle/`:** la hoja que se abre al tocar un cosmético
- **`favoritos/`:** la lista de favoritos
- **`admin/`:** el panel con los totales y los botones
- **`components/`:** piezas reutilizables: cargando, error con Reintentar, vacío y precio en V-Bucks
- **`theme/`:** colores, fuentes (Burbank solo en títulos, Inter en el resto) y formas

### Pruebas ★ (`app/src/test/java/com/tecsup/fortnite/`)

- **`ui/lista/Falsos.kt`:** un repositorio y un notificador falsos en memoria, sin internet ni Room
- **`ui/lista/ListaViewModelTest.kt`:** 6 pruebas: sincronizar, error sin datos, sin conexión con datos, favoritos, deslizar y reintentar
- **`ui/lista/BusquedaTest.kt`:** 4 pruebas de la búsqueda sin tildes
- **`ui/login/LoginViewModelTest.kt`:** 5 pruebas de la validación y los mensajes de error

Para correrlas, abre una terminal en `TecsupNite` y ejecuta:

```bash
gradlew testDebugUnitTest
```

## 6. Firebase: qué se configuró

El proyecto de Firebase se llama `tecsupnite`. Las capturas de la consola están en la sección 3 del informe (figuras 1 a 6). Se configuró esto:

1. App Android registrada con el paquete `com.tecsup.fortnite`
2. Authentication con correo y contraseña activado
3. Dos usuarios de prueba: `admin@tecsupnite.com` y `usuario@tecsupnite.com`
4. Firestore con la colección `usuarios`: un documento por usuario, con su UID como nombre y los campos `rol` y `nombre`
5. Reglas publicadas (`firestore.rules`): cada usuario solo lee su propio documento y nadie escribe desde la app

La contraseña de los usuarios de prueba no está en el repositorio. Tenla a mano antes de la demostración.

## 7. Guion de la demostración

La demostración dura unos 5 minutos si sigues este orden. Cada paso cubre una prueba de aceptación:

1. **Login con error (PA-03, PA-04):** deja los campos vacíos y pulsa Ingresar; luego pon una contraseña incorrecta
2. **Login como usuario (PA-01):** entra con `usuario@tecsupnite.com` y muestra que no aparece la pestaña Admin
3. **Tienda (PA-05):** muestra la carga, las secciones, el buscador, los filtros y la notificación «Se guardaron N elementos»
4. **Detalle (PA-06):** toca un cosmético y muestra la hoja con datos del segundo endpoint
5. **Favoritos (PA-07, PA-10):** marca uno, cierra la app, ábrela y revisa la pestaña Favoritos
6. **Sin internet (PA-08):** activa el modo avión y desliza para actualizar; aparece el aviso y la lista sigue
7. **Girar la pantalla (PA-13):** la tienda usa más columnas y no pierde nada
8. **Cerrar sesión y entrar como admin (PA-12, PA-02):** muestra la pestaña Admin
9. **Panel Admin (PA-11):** pulsa Limpiar caché (totales en 0) y luego Sincronizar
10. **Código:** abre los archivos de la sección 4 que pida el profesor
11. **Fase 2:** cambia a `mejora-ia`, muestra `PROMPTS.md` y corre las pruebas

Si el emulador falla, instala el APK del release en un celular con Android 8 o superior.

## 8. Preguntas que puede hacer el profesor

Cada respuesta indica el archivo que conviene abrir.

**¿Por qué la lista sale de Room y no de la API?**
Porque así la app funciona sin internet. La API solo actualiza Room y la pantalla observa Room. Abre `ItemRepositoryImpl.kt:23`.

**¿Cómo sabe la app si eres admin?**
Después del login lee el documento `usuarios/{uid}` en Firestore y mira el campo `rol`. El rol no está escrito en la app. Abre `FirebaseAuthRepository.kt:53`.

**¿Por qué no se pierden los favoritos al sincronizar?**
Antes de reemplazar la tienda se guardan los códigos de los favoritos, y luego se vuelven a marcar en los cosméticos nuevos. Abre `ItemRepositoryImpl.kt:25` y `ItemDao.kt:41`.

**¿Qué pasa si no hay internet?**
El repositorio atrapa el error de conexión. Si hay datos guardados se muestran con un aviso; si no hay, sale el error con Reintentar. Abre `ListaViewModel.kt:55`.

**¿Por qué usas interfaces para el repositorio y el notificador?**
Para que el ViewModel no dependa de Firebase, Retrofit ni Android. En las pruebas se cambian por versiones falsas. Abre `Falsos.kt`.

**¿Por qué la notificación es local y no push?**
Porque el aviso nace en el mismo celular, cuando la app termina de sincronizar. No hace falta un servidor. Abre `NotificadorAndroid.kt:56`.

**¿Qué hizo la IA y qué hiciste tú?**
La Fase 1 está en `main`. Las mejoras de la Fase 2 están en `mejora-ia` y cada consulta está en `PROMPTS.md`, con lo que se pidió y lo que se cambió.

**¿Por qué no hay pistas musicales ni autos?**
Se filtran al descargar la tienda para mostrar solo cosméticos de Battle Royale. Abre `ItemRepositoryImpl.kt:30`.
