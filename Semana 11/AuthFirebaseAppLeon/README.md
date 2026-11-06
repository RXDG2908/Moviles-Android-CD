# AuthFirebaseAppLeon — Mis Cursos (Laboratorio 11)

**Curso:** Programación Móvil — Tecsup
**Alumno:** Renzo León
**Docente:** Juan León S.
**Tema:** Firebase Authentication (Email/Password) + Cloud Firestore con Jetpack Compose

---

## 1. ¿Qué es esta app?

Una app Android que permite:

1. **Registrarse** con correo y contraseña.
2. **Iniciar sesión** con esas credenciales.
3. Ver una **pantalla de bienvenida** con el correo del usuario.
4. Administrar sus propios **cursos** (CRUD completo) guardados en la nube.

Cada usuario **solo ve y modifica sus propios cursos**. El filtrado se hace por el `uid` del usuario
autenticado, y además lo refuerzan las reglas de seguridad de Firestore.

---

## 2. Tecnologías

| Tecnología | Para qué se usa |
|---|---|
| Kotlin | Lenguaje de la app |
| Jetpack Compose + Material 3 | Interfaz de usuario |
| Navigation Compose 2.8.0 | Navegación entre Login, Registro y Home |
| Firebase Authentication | Registro e inicio de sesión (Email/Password) |
| Cloud Firestore | Base de datos en la nube para los cursos |
| Firebase BoM 33.1.0 | Mantiene alineadas las versiones de Firebase |
| Gradle (Kotlin DSL) + Google Services 4.4.2 | Compilación y conexión con Firebase |

Configuración: `minSdk 26`, `compileSdk / targetSdk 37`, paquete `com.tecsup.authfirebaseappleon`.

---

## 3. Estructura del proyecto

```
AuthFirebaseAppLeon/
├── build.gradle.kts              ← plugins del proyecto (apply false)
├── settings.gradle.kts           ← nombre del proyecto y repositorios
├── gradle/libs.versions.toml     ← catálogo de versiones
└── app/
    ├── build.gradle.kts          ← plugins, android{} y dependencias del módulo
    ├── google-services.json      ← configuración de Firebase (debe estar en app/)
    └── src/main/
        ├── AndroidManifest.xml   ← permiso de internet y MainActivity
        ├── java/com/tecsup/authfirebaseappleon/
        │   ├── MainActivity.kt   ← punto de entrada, inicia Firebase y el tema
        │   ├── AuthApp.kt        ← rutas y NavHost (Destinations)
        │   ├── LoginScreen.kt    ← inicio de sesión
        │   ├── RegisterScreen.kt ← registro de usuario
        │   ├── HomeScreen.kt     ← bienvenida + CRUD de cursos
        │   └── Curso.kt          ← modelo de datos del curso
        └── res/
            ├── drawable/tecsup.png        ← logo de Tecsup (HD)
            ├── drawable/tecsup_icon.png   ← icono de Tecsup (HD)
            └── mipmap-*/                  ← icono de la app
```


**Acceso rápido a los archivos principales** (clic para abrir):

| Archivo | Para qué sirve |
|---|---|
| [`MainActivity.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/MainActivity.kt) | Punto de entrada: Firebase y tema |
| [`AuthApp.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/AuthApp.kt) | Rutas y navegación |
| [`LoginScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt) | Inicio de sesión |
| [`RegisterScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt) | Registro de usuario |
| [`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt) | Bienvenida + CRUD de cursos |
| [`Curso.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/Curso.kt) | Modelo del curso |
| [`app/build.gradle.kts`](app/build.gradle.kts) | Gradle del módulo |
| [`build.gradle.kts`](build.gradle.kts) | Gradle del proyecto |
| [`libs.versions.toml`](gradle/libs.versions.toml) | Catálogo de versiones |
| [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml) | Manifest |
| [`google-services.json`](app/google-services.json) | Configuración de Firebase |

---

## 4. Cómo funciona

### 4.1 Arranque
`MainActivity` inicializa Firebase (`FirebaseApp.initializeApp`) y muestra `AuthApp()` dentro de un
tema Material 3 (morado como color principal y el celeste de Tecsup como secundario).

### 4.2 Navegación ([`AuthApp.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/AuthApp.kt))
Usa Navigation Compose con tres rutas definidas en el objeto `Destinations`:

```
login  ──(Regístrate aquí)──►  register
  │                               │
  └──(login correcto)──┐   ┌──(usuario creado)
                       ▼   ▼
                        home ──(Cerrar sesión)──► login
```

Al entrar al `home` se usa `popUpTo(...) { inclusive = true }`, así el botón *atrás* no regresa al login.

### 4.3 Login y Registro
- Los campos se guardan en estados `remember { mutableStateOf(...) }`.
- Se validan antes de llamar a Firebase y los mensajes se muestran con **Toast**:
  - `Complete todos los campos`
  - `Las contraseñas no coinciden`
  - `Mínimo 6 caracteres` (regla de Firebase)
  - `Inicio exitoso ✅` / `Usuario creado ✅`
  - `Error: …` si Firebase rechaza la operación
- Login: `signInWithEmailAndPassword`. Registro: `createUserWithEmailAndPassword`.
- Mientras Firebase responde, el botón se desactiva y muestra `Cargando...` o `Creando...`.
- El login incluye el pie de página `Renzo León - Tecsup`.

### 4.4 Home y CRUD de cursos ([`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt))
Muestra el saludo, `Sesión iniciada como: correo`, el botón **Cerrar sesión** y la lista **Mis Cursos**.

| Operación | Cómo se hace |
|---|---|
| **Create** | Botón *Nuevo curso* → diálogo → `collection("cursos").add(datos)` |
| **Read** | `addSnapshotListener` filtrado con `whereEqualTo("uid", uid)`; se actualiza en tiempo real |
| **Update** | Botón *Editar* → mismo diálogo → `document(id).set(datos)` |
| **Delete** | Botón *Eliminar* → `document(id).delete()` |

---

## 5. Datos en Firestore

Colección **`cursos`**, un documento por curso:

| Campo | Tipo | Descripción |
|---|---|---|
| `nombre` | string | Nombre del curso |
| `descripcion` | string | Descripción |
| `creditos` | number | Cantidad de créditos |
| `uid` | string | Id del usuario dueño (el mismo que aparece en Authentication) |

### Reglas de seguridad

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /cursos/{id} {
      allow create: if request.auth != null && request.resource.data.uid == request.auth.uid;
      allow read, update, delete: if request.auth != null && resource.data.uid == request.auth.uid;
    }
  }
}
```

Con estas reglas un usuario solo puede crear cursos a su nombre y leer, editar o borrar los suyos.

---

## 6. Configuración de Firebase (resumen)

1. Crear el proyecto en Firebase Console (`authfirebaseappleon`).
2. Registrar la app Android con el package name `com.tecsup.authfirebaseappleon`
   (debe coincidir con el `applicationId`).
3. Descargar `google-services.json` y colocarlo en **[`app/google-services.json`](app/google-services.json)**.
4. En **Authentication → Método de acceso** activar **Correo electrónico/Contraseña**.
5. En **Firestore Database** crear la base de datos `(default)` y publicar las reglas de la sección 5.

> `google-services.json` identifica tu proyecto de Firebase. La `api_key` no es un secreto fuerte, pero
> la seguridad real de los datos depende de las reglas de Firestore.

---

## 7. Cómo ejecutarla

1. Abrir la carpeta `AuthFirebaseAppLeon` en Android Studio (no la carpeta `app/`).
2. Esperar el *Gradle Sync*.
3. Elegir un emulador (por ejemplo Pixel) y ejecutar la configuración **app**.

También por terminal:

```bash
./gradlew :app:assembleDebug
```

---

## 8. Cómo ver los datos en vivo

En la consola de Firebase, dentro del proyecto:

- **Authentication → Usuarios:** cuentas registradas, fecha y último acceso.
- **Firestore Database → Datos:** colección `cursos`; los cambios de la app aparecen al instante.
- **Firestore Database → Reglas:** reglas y simulador.
- **Firestore Database → Uso:** lecturas y escrituras del día.

---

## 9. Pruebas realizadas (emulador Pixel)

- Registro de un usuario nuevo y entrada directa al Home.
- Inicio de sesión con el usuario creado.
- Toast al intentar ingresar con campos vacíos.
- Crear, editar y eliminar un curso.
- Un segundo usuario **no** ve los cursos del primero.
- Cerrar sesión regresa al login, y *atrás* desde el login sale de la app.

---

## 10. Qué pide la guía del profe y dónde está cada cosa

Guía: **LAB11 – Firebase Autenticación** (docente Juan León S.). Las rutas son relativas a esta carpeta;
`P/` equivale a `app/src/main/java/com/tecsup/authfirebaseappleon/`.

### 10.1 Reglas de nombres y proyecto

| Lo que pide la guía | Dónde está / cómo se cumple |
|---|---|
| El proyecto debe llevar el **apellido del alumno** | Nombre `AuthFirebaseAppLeon` (carpeta y `rootProject.name` en [`settings.gradle.kts`](settings.gradle.kts)) |
| Proyecto Kotlin + Jetpack Compose (Empty Compose Activity) | [`app/build.gradle.kts`](app/build.gradle.kts) → `buildFeatures { compose = true }` |
| **Package name** = `applicationId` = el de Firebase | [`app/build.gradle.kts` línea 19](app/build.gradle.kts#L19) (`applicationId`) y `namespace`; `google-services.json` → `package_name` |
| Minimum SDK 24 o 26 | [`app/build.gradle.kts` línea 22](app/build.gradle.kts#L22) (`minSdk = 26`) |
| Crear el proyecto en Firebase y registrar la app | Consola de Firebase: proyecto `authfirebaseappleon` |
| Descargar **`google-services.json`** y ponerlo dentro de `app/` | [`app/google-services.json`](app/google-services.json) |
| Activar **Email/Password** en Authentication | Consola de Firebase → Authentication → Método de acceso |

### 10.2 Gradle y dependencias

| Lo que pide la guía | Dónde está |
|---|---|
| Plugin `com.google.gms.google-services` **4.4.2** (nivel Proyecto, `apply false`) | [`build.gradle.kts` línea 4](build.gradle.kts#L4) y versión en [`gradle/libs.versions.toml` línea 31](gradle/libs.versions.toml#L31) |
| Aplicar el plugin en el módulo `app` | [`app/build.gradle.kts` línea 5](app/build.gradle.kts#L5) |
| `firebase-bom` **33.1.0** | [`app/build.gradle.kts` línea 51](app/build.gradle.kts#L51) |
| `firebase-auth-ktx` | [`app/build.gradle.kts` línea 52](app/build.gradle.kts#L52) |
| `navigation-compose` **2.8.0** | [`app/build.gradle.kts` línea 56](app/build.gradle.kts#L56) |
| Dependencias de Compose y AndroidX (por catálogo `libs`) | [`app/build.gradle.kts`](app/build.gradle.kts) bloque `dependencies` + [`gradle/libs.versions.toml`](gradle/libs.versions.toml) |
| Extra de la actividad: `firebase-firestore-ktx` | [`app/build.gradle.kts` línea 53](app/build.gradle.kts#L53) |
| Permiso de internet | [`app/src/main/AndroidManifest.xml` línea 5](app/src/main/AndroidManifest.xml#L5) |

### 10.3 Archivos de código pedidos

| Archivo pedido | Ubicación | Qué hace |
|---|---|---|
| [`MainActivity.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/MainActivity.kt) | [`P/MainActivity.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/MainActivity.kt) | Inicia Firebase ([línea 20](app/src/main/java/com/tecsup/authfirebaseappleon/MainActivity.kt#L20): `FirebaseApp.initializeApp(this)`) y llama a `AuthApp()` |
| [`AuthApp.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/AuthApp.kt) | [`P/AuthApp.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/AuthApp.kt) | Objeto `Destinations` ([línea 10](app/src/main/java/com/tecsup/authfirebaseappleon/AuthApp.kt#L10)) y `NavHost` ([línea 26](app/src/main/java/com/tecsup/authfirebaseappleon/AuthApp.kt#L26)) con login, register y home |
| [`LoginScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt) | [`P/LoginScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt) | Pantalla de Autenticación |
| [`RegisterScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt) | [`P/RegisterScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt) | Pantalla de Registro de Usuario |
| [`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt) | [`P/HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt) | Pantalla de Bienvenida (+ CRUD de cursos) |

### 10.4 Pantallas: lo que muestra la guía y dónde está en el código

**Login ([`LoginScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt))**
- Título superior "Programación Móvil - Tecsup" → [línea 46](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt#L46).
- Logo de Tecsup debajo del título, `R.drawable.tecsup` (120dp) → [línea 55](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt#L55); imagen en [`app/src/main/res/drawable/tecsup.png`](app/src/main/res/drawable/tecsup.png).
- "Iniciar Sesión 🔐", campos "Correo electrónico" y "Contraseña", botón "Ingresar" (muestra "Cargando...").
- Inicio de sesión con `signInWithEmailAndPassword` → [línea 105](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt#L105).
- Enlace "¿No tienes cuenta? Regístrate aquí" → va al registro.
- Pie de página "Renzo León - Tecsup" (la guía pone el nombre del profe; aquí va el del alumno) → [línea 134](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt#L134).

**Registro ([`RegisterScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt))**
- "Crear cuenta ✨", campos "Correo electrónico", "Contraseña" y "Confirmar contraseña".
- Validaciones: campos vacíos, "Las contraseñas no coinciden" ([línea 81](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt#L81)) y "Mínimo 6 caracteres".
- Alta de usuario con `createUserWithEmailAndPassword` → [línea 91](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt#L91).
- Botón "Registrarse" (muestra "Creando...") y enlace "← Volver al login".

**Bienvenida ([`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt))**
- "Bienvenido/a 🎉" y "Sesión iniciada como: correo" → [línea 83](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt#L83).
- Botón "Cerrar sesión" que ejecuta `auth.signOut()` → [línea 88](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt#L88).

**Mensajes (Toast), como en la guía:** "Complete todos los campos", "Inicio exitoso ✅",
"Usuario creado ✅", "Las contraseñas no coinciden", "Mínimo 6 caracteres" y "Error: …".

### 10.5 Actividad de laboratorio: "Mis Cursos – App con Autenticación y CRUD en la Nube"

| Requisito de la guía | Dónde está |
|---|---|
| Autenticación con Firebase Authentication (Email/Password) | [`LoginScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/LoginScreen.kt) y [`RegisterScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/RegisterScreen.kt) |
| Manejo de datos en la nube con **Cloud Firestore** | [`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt), colección `cursos`; modelo en [`P/Curso.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/Curso.kt) |
| Pantallas con Jetpack Compose | Todos los archivos `*Screen.kt` |
| **Crear** cursos | [`HomeScreen.kt` línea 177](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt#L177) (`ref.add(datos)`) |
| **Leer / visualizar** cursos | [`HomeScreen.kt` línea 41](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt#L41) (`addSnapshotListener`, tiempo real) |
| **Editar** cursos | [`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt) botón *Editar* + `set(datos)` sobre el mismo documento |
| **Eliminar** cursos | [`HomeScreen.kt` línea 148](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt#L148) (`.delete()`) |
| Cada usuario **solo ve sus propios cursos** | Filtro `whereEqualTo("uid", uid)` en [`HomeScreen.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/HomeScreen.kt) + reglas de seguridad de la sección 5 |

### 10.6 Extras (no pedidos, mejoran el diseño)
- Logo e icono de Tecsup en HD y como icono de la app ([`res/drawable/tecsup_icon.png`](app/src/main/res/drawable/tecsup_icon.png), `res/mipmap-*`).
- Colores: morado en botones y celeste de Tecsup ([`MainActivity.kt`](app/src/main/java/com/tecsup/authfirebaseappleon/MainActivity.kt)).
- Tarjetas de cursos con chip de créditos y botón flotante centrado "Nuevo curso".

> Los números de línea son de la versión actual del código y pueden moverse si se edita el archivo.
